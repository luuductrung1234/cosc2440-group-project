package oss.cosc2440.rmit.domain;

import java.time.Instant;
import java.util.*;

/**
 * @author Luu Duc Trung - S3951127
 */
public class ShoppingCart extends Domain<UUID> {

  /**
   * shopping cart attributes
   */
  private final Set<CartItem> items;
  private String couponCode;
  private Instant dateOfPurchase;

  /**
   * Constructor
   */
  public ShoppingCart() {
    this(UUID.randomUUID(), new HashSet<>(), null, null);
  }

  public ShoppingCart(UUID id, Set<CartItem> items, String couponCode, Instant dateOfPurchase) {
    super(id);
    this.items = items;
    this.couponCode = couponCode;
    this.dateOfPurchase = dateOfPurchase;
  }

  public int totalQuantity() {
    return this.items.stream().mapToInt(CartItem::getQuantity).sum();
  }

  public int totalWeight() {
    return 0;
  }

  public double totalAmount() {
    return 0;
  }

  public boolean addItem(Product product) {
    return false;
  }

  public boolean removeItem(String productName) {
    return false;
  }

  public boolean applyCoupon() {
    return false;
  }

  public void syncProductInfo(Product product) {
    if (isPurchased())
      throw new IllegalStateException("Can not update product info for purchased shopping cart");
    Optional<CartItem> itemOpt = items.stream().filter(i -> i.getProductId().equals(product.getId())).findFirst();
    if (itemOpt.isEmpty()) return;
    CartItem item = itemOpt.get();
    if (product.getType() == ProductType.PHYSICAL) {
      item.syncProductInfo(product.getName(), product.getPrice(), product.getWeight(), product.getTaxType());
    } else {
      item.syncProductInfo(product.getName(), product.getPrice(), 0, product.getTaxType());
    }
  }

  public boolean isPurchased() {
    return dateOfPurchase != null;
  }

  // Getter methods
  public Collection<CartItem> getItems() {
    return this.items;
  }

  public String getCouponCode() {
    return couponCode;
  }

  public Instant getDateOfPurchase() {
    return dateOfPurchase;
  }
}
