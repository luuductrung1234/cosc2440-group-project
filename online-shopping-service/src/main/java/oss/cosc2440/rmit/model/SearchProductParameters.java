package oss.cosc2440.rmit.model;

import oss.cosc2440.rmit.domain.ProductType;

public class SearchProductParameters {
  private String name;
  private ProductType type;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProductType getType() {
    return type;
  }

  public void setType(ProductType type) {
    this.type = type;
  }
}
