package oss.cosc2440.rmit.model;

import oss.cosc2440.rmit.domain.ProductType;
import oss.cosc2440.rmit.domain.TaxType;

public class SearchProductParameters {
  private String name;
  private ProductType type;
  private TaxType taxType;
  private Double fromPrice;
  private Double toPrice;
  private ProductSort sortedBy;

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

  public TaxType getTaxType() {
    return taxType;
  }

  public void setTaxType(TaxType taxType) {
    this.taxType = taxType;
  }

  public Double getFromPrice() {
    return fromPrice;
  }

  public void setFromPrice(Double fromPrice) {
    this.fromPrice = fromPrice;
  }

  public Double getToPrice() {
    return toPrice;
  }

  public void setToPrice(Double toPrice) {
    this.toPrice = toPrice;
  }

  public ProductSort getSortedBy() {
    return sortedBy;
  }

  public void setSortedBy(ProductSort sortedBy) {
    this.sortedBy = sortedBy;
  }
}
