package oss.cosc2440.rmit.domain;

/**
 * @author Luu Duc Trung - S3951127
 */
public class PhysicalProduct extends Product {
  /**
   * physical product attributes
   */
  private double weight;

  /**
   * Constructor
   */
  public PhysicalProduct(String name, String description, int quantity, double price, double weight) {
    super(name, description, quantity, price);
    this.weight = weight;
  }

  public void update(String description, int quantity, double price, double weight) {
    this.description = description;
    this.quantity = quantity;
    this.price = price;
    this.weight = weight;
  }

  public void update(String description, double price, double weight) {
    this.description = description;
    this.price = price;
    this.weight = weight;
  }

  // Getter methods
  public double getWeight() {
    return weight;
  }

  // Override methods
  @Override
  public Product splitOne() {
    if(quantity < 1)
      throw new IllegalStateException("Not enough quantity to split");
    this.decreaseQuantity();
    return new PhysicalProduct(this.getName(), this.description, 1, this.price, this.weight);
  }

  @Override
  public ProductType getType() {
    return ProductType.PHYSICAL;
  }

  @Override
  public String toString() {
    return String.format("%s - %s", ProductType.PHYSICAL, this.getName());
  }
}
