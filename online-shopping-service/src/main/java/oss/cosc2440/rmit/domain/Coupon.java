package oss.cosc2440.rmit.domain;

import java.util.UUID;

public abstract class Coupon<T> extends Domain<UUID> {

  /**
   * Coupon attributes
   */
  private final String code;
  private final T value;
  private final UUID targetProduct;

  /**
   * Constructor
   */
  public Coupon(UUID uuid, String code, T value, UUID targetProduct) {
    super(uuid);
    this.code = code;
    this.value = value;
    this.targetProduct = targetProduct;
  }

  // Getter methods
  public abstract CouponType getType();

  public T getValue() {
    return value;
  }

  public String getCode() {
    return code;
  }

  public UUID getTargetProduct() {
    return targetProduct;
  }
}
