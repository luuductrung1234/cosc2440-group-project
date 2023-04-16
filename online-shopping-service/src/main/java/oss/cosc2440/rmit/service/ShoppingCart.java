package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.PhysicalProduct;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.domain.ProductType;
import oss.cosc2440.rmit.seedwork.Helpers;
import oss.cosc2440.rmit.seedwork.Logger;

import java.util.*;

/**
 * @author Luu Duc Trung - S3951127
 */
public class ShoppingCart {
  /**
   * shopping cart static/constants
   */
  private static final double BASE_FEE = 0.1;

  /**
   * shopping cart dependencies
   */
  private final ProductService productService;

  /**
   * shopping cart attributes
   */
  private UUID id;
  private final Set<String> products;

  // Constructor
  public ShoppingCart(ProductService productService) {
    this.id = UUID.randomUUID();
    this.productService = productService;
    this.products = new HashSet<>();
  }

  public UUID getId() {
    return id;
  }

  public int totalQuantity() {
    return this.products.size();
  }

  public String getItem(int index) {
    return new ArrayList<>(products).get(index);
  }

  /**
   * <p>Add the product with the given name to the shopping cart.</p>
   * <ul>
   *  <li>if the quantity available of the product is zero, do nothing and return false;</li>
   *  <li>if the product with the given name exists in the cart already, do nothing and return false;</li>
   *  <li>otherwise, decrease the quantity available of that product by one, add the product name to the cart, and return true.</li>
   * </ul>
   */
  public boolean addItem(String productName) {
    Optional<String> existedProductOpt = this.products.stream()
        .filter(p -> p.equals(productName)).findFirst();
    if (existedProductOpt.isPresent())
      return false;

    Optional<Product> productOpt = productService.reserveProduct(productName, this.id);
    if (productOpt.isEmpty())
      return false;

    Product product = productOpt.get();
    this.products.add(product.getName());
    return true;
  }

  /**
   * <p>Remove the product with the given name from the shopping cart.</p>
   * <ul>
   *  <li>if the product with the given name does not exist in the cart, do nothing and return false;</li>
   *  <li>otherwise, increase the quantity available of that product by one, remove the product name from the cart, and return true.</li>
   * </ul>
   */
  public boolean removeItem(String productName) {
    Optional<String> existedProductOpt = this.products.stream()
        .filter(p -> p.equals(productName)).findFirst();
    if (existedProductOpt.isEmpty())
      return false;

    Optional<Product> productOpt = productService.rollbackProduct(productName, this.id);
    if (productOpt.isEmpty())
      return false;

    this.products.remove(existedProductOpt.get());
    return true;
  }

  /**
   * <p>Calculate and return the total price amount of all products in the cart.</p>
   * <p>Furthermore, if a cart contains physical products, you must add the shipping fee to the total price before returning it.</p>
   */
  public double cartAmount() {
    return totalAmount() + shippingFee();
  }

  public boolean setItemMessage(String productName, String message) {
    Optional<String> existedProductOpt = this.products.stream()
        .filter(p -> p.equals(productName)).findFirst();
    if (existedProductOpt.isEmpty())
      return false;

    Optional<Product> productOpt = productService.findReservedProductByName(this.id, productName);
    if (productOpt.isEmpty())
      return false;

    Product product = productOpt.get();
    product.setMessage(message);
    return true;
  }

  /**
   * total weight of all physical products
   */
  public double totalWeight() {
    Collection<Product> physicalProducts = productService.listAllByNamesAndType(products, ProductType.PHYSICAL);
    return physicalProducts.stream()
        .map(p -> (PhysicalProduct) p)
        .mapToDouble(PhysicalProduct::getWeight).sum();
  }

  public void printDetail() {
    System.out.printf("You have %d item(s) in cart\n\n", this.products.size());
    System.out.printf("%-7s %-30s %-10s %-20s %-15s %-10s\n", "No.", "product", "quantity", "price", "weight", "message");
    System.out.println("-".repeat(100));
    if (products.isEmpty())
      Logger.printInfo("empty cart, let's buy something...");
    int itemNo = 0;
    for (String name : products) {
      Product product = productService.findReservedProductByName(this.id, name).get();

      String message =  product.getMessage() == null ? "-" : product.getMessage();

      String weight = "-";
      if (product.getType().equals(ProductType.PHYSICAL))
        weight = String.format("%.2f", ((PhysicalProduct) product).getWeight());

      System.out.printf("%-7s %-30s %-10s %-20s %-15s %-10s\n", itemNo, product.getName(), 1,
          Helpers.toString(product.getPrice(), "USD", true), weight, message);
      itemNo++;
    }
    System.out.println("-".repeat(100));

    System.out.printf("%-33s %-15s %-10s\n", "", "Total Price:",
        Helpers.toString(this.totalAmount(), "USD", true));
    System.out.printf("%-33s %-15s %-10s\n", "", "Shipping Fee:",
        Helpers.toString(this.shippingFee(), "USD", true));
    System.out.printf("%-33s %-15s %-10s\n", "", "Total Amount:",
        Helpers.toString(this.cartAmount(), "USD", true));
  }

  /**
   * total of all products' price
   */
  private double totalAmount() {
    return productService.listAllByNames(products).stream()
        .mapToDouble(Product::getPrice).sum();
  }

  /**
   * The shipping fee is calculated as:
   * <br/>
   * shipping fee = (total weight of all physical products) * (base fee)
   */
  private double shippingFee() {
    return totalWeight() * BASE_FEE;
  }
}
