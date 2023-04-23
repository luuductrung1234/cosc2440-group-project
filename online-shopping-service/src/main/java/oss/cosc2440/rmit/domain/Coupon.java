package oss.cosc2440.rmit.domain;

import oss.cosc2440.rmit.seedwork.Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Coupon extends Domain<UUID> {

  /**
   * Coupon attributes
   */
  private final String code;
  private final CouponType type;
  private final double value;
  private final UUID targetProduct;

  /**
   * Constructor
   */
  public Coupon(UUID uuid, String code, CouponType type, double value, UUID targetProduct) {
    super(uuid);
    this.code = code;
    this.type = type;
    this.value = value;
    this.targetProduct = targetProduct;
  }

  @Override
  public String serialize() {
    List<String> fields = new ArrayList<>() {{
      add(id.toString());
      add(code);
      add(type.toString());
      add(String.valueOf(value));
      add(targetProduct.toString());
    }};
    return String.join(",", fields);
  }

  /**
   * override static method Domain.deserialize
   *
   * @param data serialized string data
   * @return new instance of Product
   */
  public static Coupon deserialize(String data) {
    if (Helpers.isNullOrEmpty(data))
      throw new IllegalArgumentException("data to deserialize should not be empty!");
    String[] fields = data.split(",", 5);
    return new Coupon(UUID.fromString(fields[0]),
        fields[1],
        CouponType.valueOf(fields[2]),
        Double.parseDouble(fields[3]),
        UUID.fromString(fields[4]));
  }

  @Override
  public String toString() {
    return String.format("%s - %s", this.getType(), this.getCode());
  }

  // Getter methods
  public CouponType getType() {
    return type;
  }

  public double getValue() {
    return value;
  }

  public String getCode() {
    return code;
  }

  public UUID getTargetProduct() {
    return targetProduct;
  }
}
