package oss.cosc2440.rmit.domain;

import java.util.UUID;

/**
 * @author Luu Duc Trung - S3951127
 */
public abstract class Product extends Domain<UUID> {

  /**
   * product attributes
   */
  private String name;
  private String description;
  private int quantity;
  private double price;
  private TaxType taxType;
  private boolean canUseAsGift;

  /**
   * Constructor
   */
  public Product(String name, String description, int quantity, double price, TaxType taxType, boolean canUseAsGift) {
    this(UUID.randomUUID(), name, description, quantity, price, taxType, canUseAsGift);
  }

  public Product(UUID id, String name, String description, int quantity, double price, TaxType taxType,
      boolean canUseAsGift) {
    super(id);
    this.name = name;
    this.description = description;
    this.quantity = quantity;
    this.price = price;
    this.taxType = taxType;
    this.canUseAsGift = canUseAsGift;
  }

  public void decreaseQuantity() {
    this.quantity--;
  }

  public void increaseQuantity() {
    this.quantity++;
  }

  public void update(String name, String description, int quantity, double price, boolean canUseAsGift) {
    this.name = name;
    this.description = description;
    this.quantity = quantity;
    this.price = price;
    this.canUseAsGift = canUseAsGift;
  }

  public abstract ProductType getType();

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

  public TaxType getTaxType() {
    return taxType;
  }

  public boolean canUseAsGift() {
    return canUseAsGift;
  }

}