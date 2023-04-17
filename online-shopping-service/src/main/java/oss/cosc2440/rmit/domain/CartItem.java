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
  private int quantity;
  private String message;

  /**
   * Constructor
   */
  public CartItem(UUID cartId, UUID productId, int quantity) {
    this(UUID.randomUUID(), cartId, productId, quantity);
  }

  public CartItem(UUID id, UUID cartId, UUID productId, int quantity) {
    super(id);
    this.cartId = cartId;
    this.productId = productId;
    this.quantity = quantity;
  }

  public void increaseQuantity(int quantity) {
    this.quantity += quantity;
  }

  public void decreaseQuantity(int quantity) {
    this.quantity -= quantity;
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

  public int getQuantity() {
    return quantity;
  }
}
