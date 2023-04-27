package oss.cosc2440.rmit.service;

/**
 * @author Group 8
 */

import oss.cosc2440.rmit.domain.CartItem;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.domain.ShoppingCart;
import oss.cosc2440.rmit.seedwork.Deserializer;
import oss.cosc2440.rmit.seedwork.Helpers;
import oss.cosc2440.rmit.seedwork.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manage a collection of shopping carts (sorted by total weight, by default)
 */
public class CartService {
  private final List<ShoppingCart> carts;

  // Constructor
  public CartService(String pathToFile) {
    carts = new ArrayList<>();
    try {
      carts.addAll(Deserializer.read(pathToFile, ShoppingCart.class));
      List<CartItem> cartItems = Deserializer.read(pathToFile, CartItem.class);
      carts.forEach(c -> c.setItems(cartItems.stream().filter(i -> i.getCartId().equals(c.getId())).collect(Collectors.toList())));
    } catch (IOException e) {
      Logger.printWarning("Fail to load data from file");
    }
  }

  public List<ShoppingCart> listAll() {
    return carts.stream()
        .sorted(Comparator.comparingDouble(ShoppingCart::totalWeight))
        .collect(Collectors.toList());
  }

  public Optional<ShoppingCart> findById(UUID id) {
    return listAll().stream().filter(c -> c.getId().equals(id)).findFirst();
  }

  public void syncProductInfo(Product product) {
    List<ShoppingCart> carts = listAll().stream().filter(c -> !c.isPurchased()).collect(Collectors.toList());
    carts.forEach(c -> c.syncProductInfo(product));
  }

  public void submit(ShoppingCart cart) {
    carts.add(cart);
  }

  public void printReceipt(ShoppingCart cart, boolean printToFile) {
    cart.purchase();

    if (printToFile) {
      printToFile(cart);
    } else {
      print(cart);
    }
  }

  private void print(ShoppingCart cart) {
    // Print to console
    System.out.println("\n\n===============================================");
    System.out.println("=================== RECEIPT ===================");

    System.out.printf("Date of purchase: %s%n", Helpers.toString(cart.getDateOfPurchase()));
    System.out.println("Items:");
    cart.getItems().forEach(item -> {
      System.out.printf("%s - $%.2f x %d%n", item.getProductName(), item.getProductPrice(), item.getQuantity());
    });

    System.out.printf("\nSubtotal: $%.2f%n", cart.totalOriginAmount());
    System.out.printf("Tax: $%.2f%n", cart.totalTax());
    System.out.printf("Shipping fee: $%.2f%n", cart.shippingFee());
    System.out.printf("Total Discount: $%.2f%n", cart.totalDiscount());
    System.out.printf("Total: $%.2f%n", cart.totalAmount());

    System.out.println("\nThank you for shopping with us!");
    System.out.println("===============================================");
    System.out.println("===============================================");
    System.out.println();
  }

  private void printToFile(ShoppingCart cart) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Please enter file name to print to: ");
    String fileName = scanner.nextLine();
    fileName = Helpers.attachFileExtension(fileName);

    String directory;
    File directoryFile;

    Logger.printInfo("If you don't want to specify storage directory, skip following input.\n " +
        "Directory {HOME}/Downloads/ will be used by default.\n");
    /*
    prompts user for a location to store there file
    EXAMPLE -
    C:\Users\(USERNAME)\Downloads\receipt
    */
    while (true) {
      System.out.print("Please enter directory to store the file: ");
      directory = scanner.nextLine();

      if(Helpers.isNullOrEmpty(directory)) {
        directory = System.getProperty("user.home") + File.separator + "Downloads";
        break;
      }

      directoryFile = new File(directory);
      if (directoryFile.exists() && directoryFile.isDirectory()) {
        break;
      } else {
        System.err.println("Error: The directory you entered is not valid.");
        System.out.println("Do you want to enter the directory again or print to console instead? (E/C)");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("C")) {
          print(cart);
          return;
        }
      }
    }

    String filePath = directory + File.separator + fileName;

    try (FileWriter writer = new FileWriter(filePath)) {
      writer.write("======== RECEIPT ========\n\n");

      writer.write(String.format("Date of purchase: %s\n\n", Helpers.toString(cart.getDateOfPurchase())));

      writer.write("Items:\n");
      cart.getItems().forEach(item -> {
        try {
          writer.write(String.format("%s - $%.2f x %d\n", item.getProductName(), item.getProductPrice(), item.getQuantity()));
        } catch (IOException e) {
          e.printStackTrace();
        }
      });

      writer.write(String.format("\nSubtotal: $%.2f\n", cart.totalOriginAmount()));
      writer.write(String.format("Tax: $%.2f\n", cart.totalTax()));
      writer.write(String.format("Shipping fee: $%.2f\n", cart.shippingFee()));
      writer.write(String.format("Total Discount: $%.2f\n", cart.totalDiscount()));
      writer.write(String.format("Total: $%.2f\n\n", cart.totalAmount()));

      writer.write("Thank you for shopping with us!\n");
      writer.write("=========================");
      System.out.printf("Receipt successfully created at %s%n", filePath);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
