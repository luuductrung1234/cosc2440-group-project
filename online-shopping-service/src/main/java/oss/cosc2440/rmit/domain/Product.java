package oss.cosc2440.rmit.domain;

import java.util.UUID;

/**
 * @author Luu Duc Trung - S3951127
 */
public class Product extends Domain<UUID> {

  /**
   * product attributes
   */
  private String name;
  private String description;
  private int quantity;
  private double price;
  private double weight;
  private final ProductType type;
  private TaxType taxType;
  private boolean canUseAsGift;

  /**
   * Constructor
   */
  public Product(String name, String description, int quantity, double price, double weight, ProductType type, TaxType taxType, boolean canUseAsGift) {
    this(UUID.randomUUID(), name, description, quantity, price, weight, type, taxType, canUseAsGift);
  }

  public Product(UUID id, String name, String description, int quantity, double price, double weight, ProductType type, TaxType taxType, boolean canUseAsGift) {
    super(id);
    this.name = name;
    this.description = description;
    this.quantity = quantity;
    this.price = price;
    this.weight = weight;
    this.type = type;
    this.taxType = taxType;
    this.canUseAsGift = canUseAsGift;
  }

  public void decreaseQuantity(int quantity) {
    this.quantity -= quantity;
  }

  public void increaseQuantity(int quantity) {
    this.quantity += quantity;
  }

  public void update(String name, String description, int quantity, double price, double weight, TaxType taxType, boolean canUseAsGift) {
    this.name = name;
    this.description = description;
    this.quantity = quantity;
    this.price = price;
    this.weight = weight;
    this.taxType = taxType;
    this.canUseAsGift = canUseAsGift;
  }

  @Override
  public String toString() {
    return String.format("%-8s - %s", this.getType(), this.getName());
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

  public double getWeight() {
    return weight;
  }

  public ProductType getType() {
    return type;
  }

  public TaxType getTaxType() {
    return taxType;
  }

  public boolean canUseAsGift() {
    return canUseAsGift;
  }
}