package oss.cosc2440.rmit.model;

import oss.cosc2440.rmit.domain.ProductType;
import oss.cosc2440.rmit.domain.TaxType;
import oss.cosc2440.rmit.seedwork.constraint.GreaterOrEqual;
import oss.cosc2440.rmit.seedwork.constraint.GreaterThan;
import oss.cosc2440.rmit.seedwork.constraint.Length;
import oss.cosc2440.rmit.seedwork.constraint.NotNull;

public class CreateProductModel {
  @NotNull(message = "Given name must not be null")
  @Length(max = 100, min = 1, message = "Given name must have valid length between 1 and 100 characters.")
  private String name;

  @NotNull(message = "Given description must not be null")
  @Length(max = 500, min = 1, message = "Given description must have valid length between 1 and 500 characters.")
  private String description;

  @NotNull(message = "Given quantity must not be null")
  @GreaterOrEqual(value = 1, message = "Given quantity must be greater than or equal to 1")
  private Integer quantity;

  @NotNull(message = "Given price must not be null")
  @GreaterThan(value = 0, message = "Given price must be greater than 0")
  private Double price;

  @NotNull(message = "Given type must not be null")
  private ProductType type;

  @NotNull(message = "Given weight must not be null")
  @GreaterThan(value = 0, message = "Given weight must be greater than 0")
  private Double weight;

  @NotNull(message = "Given type must not be null")
  private TaxType taxType;

  @NotNull(message = "Given choice must not be null")
  private boolean canUseAsGift;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public ProductType getType() {
    return type;
  }

  public void setType(ProductType type) {
    this.type = type;
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public TaxType getTaxType(){
    return taxType;
  }

  public void setTaxType(TaxType type){
    this.taxType = type;
  }

  public boolean canUseAsGift() {
    return canUseAsGift;
  }
}
