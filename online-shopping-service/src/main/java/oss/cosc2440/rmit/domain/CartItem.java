package oss.cosc2440.rmit.domain;

/**
* @author Group 8
*/

import oss.cosc2440.rmit.seedwork.Constants;
import oss.cosc2440.rmit.seedwork.Helpers;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItem extends Domain<UUID> implements Gift, Splittable<CartItem> {

  /**
   * cart item attributes
   */
  private final UUID cartId;
  private final UUID productId;
  private String productName;
  private double productPrice;
  private double productWeight;
  private TaxType taxType;
  private int quantity;
  private String couponCode;
  private double couponValue;
  private CouponType couponType;
  private String message;

  /**
   * Constructor
   */
  public CartItem(UUID cartId,
                  UUID productId,
                  String productName,
                  double productPrice,
                  double productWeight,
                  TaxType taxType,
                  int quantity) {
    this(UUID.randomUUID(),
        cartId,
        productId,
        productName,
        productPrice,
        productWeight,
        taxType,
        quantity,
        null, 0, null, null);
  }

  public CartItem(UUID id,
                  UUID cartId,
                  UUID productId,
                  String productName,
                  double productPrice,
                  double productWeight,
                  TaxType taxType,
                  int quantity,
                  String couponCode,
                  double couponValue,
                  CouponType couponType,
                  String message) {
    super(id);
    this.cartId = cartId;
    this.productId = productId;
    this.productName = productName;
    this.productPrice = productPrice;
    this.productWeight = productWeight;
    this.taxType = taxType;
    this.quantity = quantity;
    this.couponCode = couponCode;
    this.couponValue = couponValue;
    this.couponType = couponType;
    this.message = message;
  }

  /**
   * Get total amount of cart item after apply coupon + tax
   */
  public BigDecimal getAmount() {
    BigDecimal originAmount = getOriginAmount();
    BigDecimal couponAmount = getDiscountAmount();
    BigDecimal taxAmount = getTaxAmount();
    return originAmount.subtract(couponAmount).add(taxAmount);
  }

  /**
   * Get total amount of cart item before apply coupon + tax
   */
  public BigDecimal getOriginAmount() {
    return BigDecimal.valueOf(productPrice).multiply(BigDecimal.valueOf(quantity));
  }

  /**
   * Get discount (calculated amount based on applied coupon)
   */
  public BigDecimal getDiscountAmount() {
    if (couponType == null) return BigDecimal.ZERO;
    BigDecimal couponAmount = BigDecimal.ZERO;
    switch (this.couponType) {
      case PRICE:
        couponAmount = BigDecimal.valueOf(couponValue);
        break;
      case PERCENT:
        couponAmount = BigDecimal.valueOf(productPrice)
            .multiply(BigDecimal.valueOf(couponValue))
            .divide(BigDecimal.valueOf(100));
        break;
    }
    return couponAmount.multiply(BigDecimal.valueOf(quantity));
  }

  /**
   * Get discount (calculated amount based on product's tax type)
   */
  public BigDecimal getTaxAmount() {
    BigDecimal taxAmount = BigDecimal.ZERO;
    switch (this.taxType) {
      case TAX_FREE:
        taxAmount = BigDecimal.valueOf(productPrice)
            .multiply(BigDecimal.valueOf(Constants.TAX_FREE_AMOUNT))
            .divide(BigDecimal.valueOf(100));
        break;
      case NORMAL_TAX:
        taxAmount = BigDecimal.valueOf(productPrice)
            .multiply(BigDecimal.valueOf(Constants.NORMAL_TAX_AMOUNT))
            .divide(BigDecimal.valueOf(100));
        break;
      case LUXURY_TAX:
        taxAmount = BigDecimal.valueOf(productPrice)
            .multiply(BigDecimal.valueOf(Constants.LUXURY_TAX_AMOUNT))
            .divide(BigDecimal.valueOf(100));
        break;
    }
    return taxAmount.multiply(BigDecimal.valueOf(quantity));
  }

  /**
   * Get total weight of cart item
   */
  public double getItemWeight() {
    return productWeight * quantity;
  }

  public void increaseQuantity(int quantity) {
    this.quantity += quantity;
  }

  public void decreaseQuantity(int quantity) {
    this.quantity -= quantity;
  }

  public boolean appliedCoupon() {
    return !Helpers.isNullOrEmpty(couponCode)
        && couponType != null
        && couponValue > 0;
  }

  public void applyCoupon(Coupon coupon) {
    this.couponCode = coupon.getCode();
    this.couponValue = coupon.getValue();
    this.couponType = coupon.getType();
  }

  public void clearCoupon() {
    this.couponCode = null;
    this.couponValue = 0;
    this.couponType = null;
  }

  /**
   * Sync new product information to snapshot fields
   */
  public void syncProductInfo(String productName, double productPrice, double productWeight, TaxType taxType) {

    this.productName = productName;
    this.productPrice = productPrice;
    this.productWeight = productWeight;
    this.taxType = taxType;
  }

  /**
   * Sync new coupon information to snapshot fields
   */
  public void syncCouponInfo(double couponValue) {
    this.couponValue = couponValue;
  }

  /**
   * override static method Domain deserialize
   *
   * @param data serialized string data
   * @return new instance of Product
   */
  public static CartItem deserialize(String data) {
    if (Helpers.isNullOrEmpty(data))
      throw new IllegalArgumentException("data to deserialize should not be empty!");
    String[] fields = data.split(",", 13);

    if (!fields[0].equalsIgnoreCase(CartItem.class.getSimpleName()))
      return null;

    return new CartItem(UUID.fromString(fields[1]),
        UUID.fromString(fields[2]),
        UUID.fromString(fields[3]),
        fields[4],
        Double.parseDouble(fields[5]),
        Double.parseDouble(fields[6]),
        TaxType.valueOf(fields[7]),
        Integer.parseInt(fields[8]),
        Helpers.isNullOrEmpty(fields[9]) ? null : fields[9],
        Helpers.isNullOrEmpty(fields[10]) ? 0 : Double.parseDouble(fields[10]),
        Helpers.isNullOrEmpty(fields[11]) ? null : CouponType.valueOf(fields[11]),
        fields[12]);
  }

  // Override methods
  @Override
  public boolean isGift() {
    return !Helpers.isNullOrEmpty(this.message);
  }

  @Override
  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public CartItem split() {
    this.decreaseQuantity(1);
    return new CartItem(
        UUID.randomUUID(),
        this.cartId,
        this.productId,
        this.productName,
        this.productPrice,
        this.productWeight,
        this.taxType,
        1,
        this.couponCode,
        this.couponValue,
        this.couponType,
        null);
  }

  // Getter methods
  public UUID getCartId() {
    return cartId;
  }

  public UUID getProductId() {
    return productId;
  }

  public String getProductName() {
    return productName;
  }

  public double getProductPrice() {
    return productPrice;
  }

  public double getProductWeight() {
    return productWeight;
  }

  public TaxType getTaxType() {
    return taxType;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getCouponCode() {
    return couponCode;
  }

  public double getCouponValue() {
    return couponValue;
  }
}
