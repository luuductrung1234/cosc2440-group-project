package oss.cosc2440.rmit.domain;

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
