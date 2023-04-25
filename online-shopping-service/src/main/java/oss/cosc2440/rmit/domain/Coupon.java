package oss.cosc2440.rmit.domain;

import oss.cosc2440.rmit.seedwork.Helpers;

import java.util.UUID;

public class Coupon extends Domain<UUID> {

  /**
   * Coupon attributes
   */
  private final String code;
  private final CouponType type;
  private final double value;
  private final UUID targetProduct;

  /**
   * Constructor
   */
  public Coupon(UUID uuid, String code, CouponType type, double value, UUID targetProduct) {
    super(uuid);
    this.code = code;
    this.type = type;
    this.value = value;
    this.targetProduct = targetProduct;
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
    String[] fields = data.split(",", 6);

    if (!fields[0].equalsIgnoreCase(Coupon.class.getSimpleName()))
      return null;

    return new Coupon(UUID.fromString(fields[1]),
        fields[2],
        CouponType.valueOf(fields[3]),
        Double.parseDouble(fields[4]),
        UUID.fromString(fields[5]));
  }

  // Getter methods
  public CouponType getType() {
    return type;
  }

  public double getValue() {
    return value;
  }

  public String getCode() {
    return code;
  }

  public UUID getTargetProduct() {
    return targetProduct;
  }
}
