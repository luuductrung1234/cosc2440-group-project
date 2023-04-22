package oss.cosc2440.rmit.repository;

import oss.cosc2440.rmit.domain.CartItem;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.domain.ProductType;
import oss.cosc2440.rmit.domain.ShoppingCart;

import java.io.*;
import java.time.Instant;
import java.util.*;

public class FileCartRepositoryImpl extends BaseFileRepository implements CartRepository {

  private final ProductRepository productRepository;

  public FileCartRepositoryImpl(String pathToDataFile) {
    super(pathToDataFile);
    this.productRepository = productRepository;
  }

  // Method to list all shopping carts stored in the data file
  @Override
  public List<ShoppingCart> listAll() {
    List<ShoppingCart> carts = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(getDataFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] values = line.split(",");
        UUID cartId = UUID.fromString(values[0]);
        String couponCode = values[1];
        Instant dateOfPurchase = Instant.parse(values[2]);

        // Create a set of cart items for each shopping cart
        Set<CartItem> items = new HashSet<>();
        for (int i = 3; i < values.length; i += 4) {
          UUID productId = UUID.fromString(values[i]);
          Product product = productRepository.findById(productId)
                  .orElseThrow(() -> new IllegalStateException("Product not found for id " + productId));
          String productName = product.getName();
          double price = Double.parseDouble(values[i + 1]);
          int quantity = Integer.parseInt(values[i + 2]);
          ProductType productType = product.getType();
          int weight = productType == ProductType.PHYSICAL ? ((int) Math.round(Double.parseDouble(values[i + 3]))) : 0;

          // Add the cart item to the set of items for the current shopping cart
          items.add(new CartItem(productId, productName, price, quantity, productType, weight));
        }
        // Create a new shopping cart object with the extracted data and add it to the list of carts
        ShoppingCart cart = new ShoppingCart(cartId, items, couponCode, dateOfPurchase);
        carts.add(cart);
      }
    } catch (IOException e) {
      // If there is an error reading from the data file, print an error message
      System.out.println("Failed to read from data file: " + getDataFile().getAbsolutePath());
    }
    return carts;
  }

  // Method to find a shopping cart by its ID
  @Override
  public Optional<ShoppingCart> findById(UUID id) {
    try (BufferedReader reader = new BufferedReader(new FileReader(getDataFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] values = line.split(",");
        UUID cartId = UUID.fromString(values[0]);
        if (cartId.equals(id)) {
          String couponCode = values[1];
          Instant dateOfPurchase = Instant.parse(values[2]);

          Set<CartItem> items = new HashSet<>();
          for (int i = 3; i < values.length; i += 4) {
            UUID productId = UUID.fromString(values[i]);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalStateException("Product not found for id " + productId));
            String productName = product.getName();
            double price = Double.parseDouble(values[i + 1]);
            int quantity = Integer.parseInt(values[i + 2]);
            ProductType productType = product.getType();
            int weight = productType == ProductType.PHYSICAL ? ((int) Math.round(Double.parseDouble(values[i + 3]))) : 0;

            items.add(new CartItem(productId, productName, price, quantity, productType, weight));
          }
          ShoppingCart cart = new ShoppingCart(cartId, items, couponCode, dateOfPurchase);
          return Optional.of(cart);
        }
      }
    } catch (IOException e) {
      System.out.println("Failed to read from data file: " + getDataFile().getAbsolutePath());
    }
    return Optional.empty();
  }

  // Method to save a shopping cart to the data file
  @Override
  public boolean add(ShoppingCart cart) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(getDataFile(), true))) {
      StringBuilder sb = new StringBuilder();
      sb.append(cart.getId()).append(",")
              .append(cart.getCouponCode()).append(",")
              .append(cart.getDateOfPurchase()).append(",");
      for (CartItem item : cart.getItems()) {
        sb.append(item.getProductId()).append(",")
                .append(item.getPrice()).append(",")
                .append(item.getQuantity()).append(",");
        if (item.getProductType() == ProductType.PHYSICAL) {
          sb.append(item.getWeight());
        }
        sb.append(",");
      }
      sb.deleteCharAt(sb.length() - 1);
      writer.write(sb.toString());
      writer.newLine();
      return true;
    } catch (IOException e) {
      System.out.println("Failed to write to data file: " + getDataFile().getAbsolutePath());
      return false;
    }
  }

  @Override
  public boolean delete(UUID id) {
    List<ShoppingCart> carts = listAll();
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(getDataFile()))) {
      for (ShoppingCart cart : carts) {
        if (!cart.getId().equals(id)) {
          StringBuilder sb = new StringBuilder();
          sb.append(cart.getId()).append(",")
                  .append(cart.getCouponCode()).append(",")
                  .append(cart.getDateOfPurchase()).append(",");
          for (CartItem item : cart.getItems()) {
            sb.append(item.getProductId()).append(",")
                    .append(item.getPrice()).append(",")
                    .append(item.getQuantity()).append(",");
            if (item.getProductType() == ProductType.PHYSICAL) {
              sb.append(item.getWeight());
            }
            sb.append(",");
          }
          sb.deleteCharAt(sb.length() - 1);
          writer.write(sb.toString());
          writer.newLine();
        }
      }
      return true;
    } catch (IOException e) {
      System.out.println("Failed to write to data file: " + getDataFile().getAbsolutePath());
      return false;
    }
  }

  @Override
  public boolean update(ShoppingCart cart) {
    if (delete(cart.getId())) {
      return add(cart);
    }
    return false;
  }
}