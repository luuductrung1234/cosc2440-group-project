package oss.cosc2440.rmit.domain;

import java.util.UUID;

/**
 * @author Luu Duc Trung - S3951127
 */
public class DigitalProduct extends Product {
  /**
   * Constructor
   */
  public DigitalProduct(String name, String description, int quantity, double price, TaxType taxType, boolean canUseAsGift) {
    this(UUID.randomUUID(), name, description, quantity, price, taxType, canUseAsGift);
  }

  public DigitalProduct(UUID id, String name, String description, int quantity, double price, TaxType taxType, boolean canUseAsGift) {
    super(id, name, description, quantity, price, taxType, canUseAsGift);
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
