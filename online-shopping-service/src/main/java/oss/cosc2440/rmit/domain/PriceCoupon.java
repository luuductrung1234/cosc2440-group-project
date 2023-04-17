package oss.cosc2440.rmit.domain;

import java.util.List;
import java.util.UUID;

public class PriceCoupon extends Coupon<Double> {
  public PriceCoupon(UUID uuid, String code, Double value, List<UUID> targetProducts) {
    super(uuid, code, value, targetProducts);
  }

  @Override
  public CouponType getType() {
    return CouponType.PRICE;
  }

  @Override
  public String toString() {
    return String.format("%s - %s", CouponType.PRICE, this.getCode());
  }
}
