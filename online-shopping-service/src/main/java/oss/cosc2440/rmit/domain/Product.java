package oss.cosc2440.rmit.domain;

import oss.cosc2440.rmit.seedwork.Helpers;

import java.util.ArrayList;
import java.util.List;
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

  public void decreaseQuantity() {
    this.quantity--;
  }

  public void increaseQuantity() {
    this.quantity++;
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
  public String serialize() {
    List<String> fields = new ArrayList<>() {{
      add(id.toString());
      add(name);
      add(description);
      add(String.valueOf(quantity));
      add(String.valueOf(price));
      add(String.valueOf(weight));
      add(type.toString());
      add(taxType.toString());
      add(String.valueOf(canUseAsGift));
    }};
    return String.join(",", fields);
  }

  /**
   * override static method Domain.deserialize
   *
   * @param data serialized string data
   * @return new instance of Product
   */
  public static Product deserialize(String data) {
    if (Helpers.isNullOrEmpty(data))
      throw new IllegalArgumentException("data to deserialize should not be empty!");
    String[] fields = data.split(",", 9);
    return new Product(UUID.fromString(fields[0]),
        fields[1],
        fields[2],
        Integer.parseInt(fields[3]),
        Double.parseDouble(fields[4]),
        Double.parseDouble(fields[5]),
        ProductType.valueOf(fields[6]),
        TaxType.valueOf(fields[7]),
        Boolean.parseBoolean(fields[8]));
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