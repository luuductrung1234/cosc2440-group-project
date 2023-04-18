package oss.cosc2440.rmit.domain;

import java.util.UUID;

public class PriceCoupon extends Coupon<Double> {
  public PriceCoupon(UUID uuid, String code, Double value, UUID targetProduct) {
    super(uuid, code, value, targetProduct);
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
