package oss.cosc2440.rmit.model;

/**
 * @author Group 8
 */

import oss.cosc2440.rmit.domain.CouponType;
import oss.cosc2440.rmit.seedwork.constraint.GreaterThan;
import oss.cosc2440.rmit.seedwork.constraint.NotNull;

import java.util.UUID;

public class UpdateCouponModel {
  @NotNull
  private UUID id;

  @NotNull(message = "Given price must not be null")
  @GreaterThan(value = 0, message = "Given price must be greater than 0")
  private Double price;

  @NotNull(message = "Given price must not be null")
  @GreaterThan(value = 0, message = "Given price must be greater than 0")
  private Integer percent;

  @NotNull(message = "Given type must not be null")
  private CouponType type;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

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
}
