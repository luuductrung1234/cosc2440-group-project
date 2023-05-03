package oss.cosc2440.rmit.model;

/**
 * @author Group 8
 */

import oss.cosc2440.rmit.domain.CouponType;

public class SearchCouponParameter {
  private String code;
  private CouponType type;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public CouponType getType() {
    return type;
  }

  public void setType(CouponType type) {
    this.type = type;
  }
}
