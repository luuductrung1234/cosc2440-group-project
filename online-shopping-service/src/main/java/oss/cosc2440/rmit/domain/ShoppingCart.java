package oss.cosc2440.rmit.domain;

import java.util.*;

/**
 * @author Luu Duc Trung - S3951127
 */
public class ShoppingCart extends Domain<UUID> {

  /**
   * shopping cart attributes
   */
  private final Set<CartItem> items;

  // Constructor
  public ShoppingCart() {
    this(UUID.randomUUID(), new HashSet<>());
  }

  public ShoppingCart(UUID id, Set<CartItem> items) {
    super(id);
    this.items = items;
  }

  public int totalQuantity() {
    return this.items.stream().mapToInt(CartItem::getQuantity).sum();
  }

  public boolean addItem(Product product) {
    return false;
  }

  public boolean removeItem(String productName) {
    return false;
  }

  // Getter methods
  public UUID getId() {
    return id;
  }

  public Collection<CartItem> getItems() {
    return this.items;
  }
}
