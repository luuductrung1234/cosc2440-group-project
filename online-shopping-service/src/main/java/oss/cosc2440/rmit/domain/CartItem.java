package oss.cosc2440.rmit.domain;

import java.util.UUID;

/**
 * @author Luu Duc Trung - S3951127
 */
public class CartItem extends Domain<UUID> implements Gift {

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
  private String message;
  private String couponCode = "";

  /**
   * Constructor
   */
  public CartItem(UUID cartId, UUID productId, String productName, double productPrice, double productWeight,
      TaxType taxType, int quantity) {
    this(UUID.randomUUID(), cartId, productId, productName, productPrice, productWeight, taxType, quantity);
  }

  public CartItem(UUID id, UUID cartId, UUID productId, String productName, double productPrice, double productWeight,
      TaxType taxType, int quantity) {
    super(id);
    this.cartId = cartId;
    this.productId = productId;
    this.productName = productName;
    this.productPrice = productPrice;
    this.productWeight = productWeight;
    this.taxType = taxType;
    this.quantity = quantity;
  }

  public CartItem(UUID id, UUID cartId, UUID productId, String productName, double productPrice, double productWeight,
      TaxType taxType, int quantity, String couponCode) {
    super(id);
    this.cartId = cartId;
    this.productId = productId;
    this.productName = productName;
    this.productPrice = productPrice;
    this.productWeight = productWeight;
    this.taxType = taxType;
    this.quantity = quantity;
    this.couponCode = couponCode;

  }

  public String getCouponCode() {
    return this.couponCode;
  }

  public void increaseQuantity(int quantity) {
    this.quantity += quantity;
  }

  public void decreaseQuantity(int quantity) {
    this.quantity -= quantity;
  }

  public void syncProductInfo(String productName, double productPrice, double productWeight, TaxType taxType) {

    this.productName = productName;
    this.productPrice = productPrice;
    this.productWeight = productWeight;
    this.taxType = taxType;
  }

  // Override methods
  @Override
  public void setMessage(String msg) {
    this.message = msg;
  }

  @Override
  public String getMessage() {
    return this.message;
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
    if (taxType.equals("TAX_FREE")) {
      return productPrice;
    } else if (taxType.equals("NORMAL_TAX")) {
      return productPrice + productPrice * 0.1;
    } else {
      return productPrice + productPrice * 0.2;
    }
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
}
