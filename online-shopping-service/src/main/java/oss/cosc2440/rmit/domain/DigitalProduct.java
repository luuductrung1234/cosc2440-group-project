package oss.cosc2440.rmit.domain;

/**
 * @author Luu Duc Trung - S3951127
 */
public class DigitalProduct extends Product {
  /**
   * Constructor
   */
  public DigitalProduct(String name, String description, int quantity, double price) {
    super(name, description, quantity, price);
  }

  public void update(String description, int quantity, double price) {
    this.description = description;
    this.quantity = quantity;
    this.price = price;
  }

  public void update(String description, double price) {
    this.description = description;
    this.price = price;
  }

  // Override methods
  @Override
  public Product splitOne() {
    if(quantity < 1)
      throw new IllegalStateException("Not enough quantity to split");
    this.decreaseQuantity();
    return new DigitalProduct(this.getName(), this.description, 1, this.price);
  }

  @Override
  public ProductType getType() {
    return ProductType.DIGITAL;
  }

  @Override
  public String toString() {
    return String.format("%s - %s", ProductType.DIGITAL, this.getName());
  }
}
