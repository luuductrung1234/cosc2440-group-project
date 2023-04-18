package oss.cosc2440.rmit.domain;

import java.util.UUID;

public class PercentCoupon extends Coupon<Integer> {
  public PercentCoupon(UUID uuid, String code, Integer value, UUID targetProduct) {
    super(uuid, code, value, targetProduct);
  }

  @Override
  public CouponType getType() {
    return CouponType.PERCENT;
  }

  @Override
  public String toString() {
    return String.format("%s - %s", CouponType.PERCENT, this.getCode());
  }
}
