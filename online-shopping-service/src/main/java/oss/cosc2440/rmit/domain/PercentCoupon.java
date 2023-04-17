package oss.cosc2440.rmit.domain;

import java.util.List;
import java.util.UUID;

public class PercentCoupon extends Coupon<Integer> {
  public PercentCoupon(UUID uuid, String code, Integer value, List<UUID> targetProducts) {
    super(uuid, code, value, targetProducts);
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
