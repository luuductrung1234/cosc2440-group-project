package oss.cosc2440.rmit.domain;

import java.util.List;
import java.util.UUID;

public abstract class Coupon<T> extends Domain<UUID> {

  /**
   * Coupon attributes
   */
  private final String code;
  private final T value;
  private final List<UUID> targetProducts;

  /**
   * Constructor
   */
  public Coupon(UUID uuid, String code, T value, List<UUID> targetProducts) {
    super(uuid);
    this.code = code;
    this.value = value;
    this.targetProducts = targetProducts;
  }

  // Getter methods
  public abstract CouponType getType();

  public T getValue() {
    return value;
  }

  public String getCode() {
    return code;
  }

  public List<UUID> getTargetProducts() {
    return targetProducts;
  }
}
