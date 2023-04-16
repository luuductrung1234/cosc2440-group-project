package oss.cosc2440.rmit.domain;

/**
 * @author Luu Duc Trung - S3951127
 */
public abstract class Product implements Gift, Splittable<Product> {

  /**
   * product attributes
   */
  private String name;
  protected String description;
  protected int quantity;
  protected double price;
  private String message;

  /**
   * Constructor
   */
  public Product(String name, String description, int quantity, double price) {
    this.name = name;
    this.description = description;
    this.quantity = quantity;
    this.price = price;
  }

  public void decreaseQuantity() {
    this.quantity--;
  }

  public void increaseQuantity() {
    this.quantity++;
  }

  public abstract ProductType getType();

  @Override
  public void setMessage(String msg) {
    this.message = msg;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  // Getter methods
  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public int getQuantity() {
    return quantity;
  }

  public double getPrice() {
    return price;
  }
}
