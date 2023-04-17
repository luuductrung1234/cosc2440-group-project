package oss.cosc2440.rmit.domain;

import java.util.UUID;

/**
 * @author Luu Duc Trung - S3951127
 */
public abstract class Product extends Domain<UUID> {

  /**
   * product attributes
   */
  protected String name;
  protected String description;
  protected int quantity;
  protected double price;
  protected boolean canUseAsGift;

  /**
   * Constructor
   */
  public Product(String name, String description, int quantity, double price, boolean canUseAsGift) {
    this(UUID.randomUUID(), name, description, quantity, price, canUseAsGift);
  }

  public Product(UUID id, String name, String description, int quantity, double price, boolean canUseAsGift) {
    super(id);
    this.name = name;
    this.description = description;
    this.quantity = quantity;
    this.price = price;
    this.canUseAsGift = canUseAsGift;
  }

  public void decreaseQuantity() {
    this.quantity--;
  }

  public void increaseQuantity() {
    this.quantity++;
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

  public boolean canUseAsGift() {
    return canUseAsGift;
  }
}
