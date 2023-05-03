package oss.cosc2440.rmit.model;

/**
 * @author Group 8
 */

import oss.cosc2440.rmit.domain.CouponType;
import oss.cosc2440.rmit.seedwork.constraint.GreaterThan;
import oss.cosc2440.rmit.seedwork.constraint.NotNull;

import java.util.UUID;

public class CreateCouponModel {
  @NotNull(message = "Given price must not be null")
  @GreaterThan(value = 0, message = "Given price must be greater than 0")
  private Double price;

  @NotNull(message = "Given price must not be null")
  @GreaterThan(value = 0, message = "Given price must be greater than 0")
  private Integer percent;

  @NotNull(message = "Given type must not be null")
  private CouponType type;

  @NotNull(message = "Given target product must not be null")
  private UUID targetProduct;

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Integer getPercent() {
    return percent;
  }

  public void setPercent(Integer percent) {
    this.percent = percent;
  }

  public CouponType getType() {
    return type;
  }

  public void setType(CouponType type) {
    this.type = type;
  }

  public UUID getTargetProduct() {
    return targetProduct;
  }

  public void setTargetProduct(UUID targetProduct) {
    this.targetProduct = targetProduct;
  }
}
