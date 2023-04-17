package oss.cosc2440.rmit.domain;

import java.util.UUID;

/**
 * @author Luu Duc Trung - S3951127
 */
public class DigitalProduct extends Product {
  /**
   * Constructor
   */
  public DigitalProduct(String name, String description, int quantity, double price, boolean canUseAsGift) {
    this(UUID.randomUUID(), name, description, quantity, price, canUseAsGift);
  }

  public DigitalProduct(UUID id, String name, String description, int quantity, double price, boolean canUseAsGift) {
    super(id, name, description, quantity, price, canUseAsGift);
  }

  public void update(String name, String description, int quantity, double price, boolean canUseAsGift) {
    this.name = name;
    this.description = description;
    this.quantity = quantity;
    this.price = price;
    this.canUseAsGift = canUseAsGift;
  }

  // Override methods
  @Override
  public ProductType getType() {
    return ProductType.DIGITAL;
  }

  @Override
  public String toString() {
    return String.format("%s - %s", ProductType.DIGITAL, this.getName());
  }
}
