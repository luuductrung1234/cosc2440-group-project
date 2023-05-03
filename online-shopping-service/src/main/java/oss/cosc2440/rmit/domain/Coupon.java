package oss.cosc2440.rmit.domain;

/**
 * @author Group 8
 */

import oss.cosc2440.rmit.seedwork.Helpers;

import java.util.UUID;

public class Coupon extends Domain<UUID> {

  /**
   * Coupon attributes
   */
  private final String code;
  private final CouponType type;
  private double price;
  private int percent;
  private final UUID targetProduct;

  /**
   * Constructor
   */
  public Coupon(String code, CouponType type, double price, int percent, UUID targetProduct) {
    super(UUID.randomUUID());
    this.code = code;
    this.type = type;
    this.price = price;
    this.percent = percent;
    this.targetProduct = targetProduct;
  }

  public Coupon(UUID uuid, String code, CouponType type, double price, int percent, UUID targetProduct) {
    super(uuid);
    this.code = code;
    this.type = type;
    this.price = price;
    this.percent = percent;
    this.targetProduct = targetProduct;
  }

  public void update(double price, int percent) {
    this.price = price;
    this.percent = percent;
  }

  @Override
  public String toString() {
    return String.format("%s - %s", this.getType(), this.getCode());
  }

  /**
   * override static method Domain deserialize
   *
   * @param data serialized string data
   * @return new instance of Product
   */
  public static Coupon deserialize(String data) {
    if (Helpers.isNullOrEmpty(data))
      throw new IllegalArgumentException("data to deserialize should not be empty!");
    String[] fields = data.split(",", 7);

    if (!fields[0].equalsIgnoreCase(Coupon.class.getSimpleName()))
      return null;

    return new Coupon(UUID.fromString(fields[1]),
        fields[2],
        CouponType.valueOf(fields[3]),
        Double.parseDouble(fields[4]),
        Integer.parseInt(fields[5]),
        UUID.fromString(fields[6]));
  }

  // Getter methods
  public CouponType getType() {
    return type;
  }

  public double getValue() {
    return this.type == CouponType.PRICE ? this.price : this.percent;
  }

  public String getCode() {
    return code;
  }

  public UUID getTargetProduct() {
    return targetProduct;
  }
}
