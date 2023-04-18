package oss.cosc2440.rmit.domain;

import java.util.UUID;

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
  public PhysicalProduct(String name, String description, int quantity, double price, TaxType taxType, double weight, boolean canUseAsGift) {
    super(UUID.randomUUID(), name, description, quantity, price, taxType, canUseAsGift);
    this.weight = weight;
  }

  public PhysicalProduct(UUID id, String name, String description, int quantity, double price, TaxType taxType, double weight, boolean canUseAsGift) {
    super(id, name, description, quantity, price, taxType, canUseAsGift);
    this.weight = weight;
  }

  public void update(String name, String description, int quantity, double price, double weight, boolean canUseAsGift) {
    super.update(name, description, quantity, price, canUseAsGift);
    this.weight = weight;
  }

  // Getter methods
  public double getWeight() {
    return weight;
  }

  // Override methods
  @Override
  public ProductType getType() {
    return ProductType.PHYSICAL;
  }

  @Override
  public String toString() {
    return String.format("%s - %s", ProductType.PHYSICAL, this.getName());
  }
}
