package oss.cosc2440.rmit.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Luu Duc Trung - S3951127
 */
public class ShoppingCart extends Domain<UUID> {

  /**
   * shopping cart attributes
   */
  private final Set<CartItem> items;
  private Instant dateOfPurchase;

  /**
   * Constructor
   */
  public ShoppingCart() {
    this(UUID.randomUUID(), new HashSet<>(), null);
  }

  public ShoppingCart(UUID id, Set<CartItem> items, Instant dateOfPurchase) {
    super(id);
    this.items = items;
    this.dateOfPurchase = dateOfPurchase;
  }

  public int totalQuantity() {
    return this.items.stream().mapToInt(CartItem::getQuantity).sum();
  }

  public double totalWeight() {
    return this.items.stream().mapToDouble(CartItem::getItemWeight).sum();
  }

  public BigDecimal totalAmount() {
    return this.items.stream().map(CartItem::getItemPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Add item to the shopping cart:
   * - Increase item quantity if it already existed in cart.
   * - Or add new item to the cart.
   * If there are more than 1 items with the same product, the non-gift item has higher precedence in the selection
   */
  public boolean addItem(Product product, int quantity) {
    if (product.getQuantity() < quantity)
      return false;

    List<CartItem> existingItems = this.items.stream()
        .filter(i -> i.getProductId().equals(product.getId())).collect(Collectors.toList());
    if (existingItems.isEmpty()) {
      this.items.add(new CartItem(this.id,
          product.getId(),
          product.getName(),
          product.getPrice(),
          product.getWeight(),
          product.getTaxType(),
          quantity));
      product.decreaseQuantity(quantity);
      return true;
    }

    // prefer non-gift item to select
    Optional<CartItem> nonGiftItemOpt = existingItems.stream().filter(i -> !i.isGift()).findFirst();
    if(nonGiftItemOpt.isPresent()) {
      CartItem nonGiftItem = nonGiftItemOpt.get();
      nonGiftItem.increaseQuantity(quantity);
      product.decreaseQuantity(quantity);
      return true;
    }

    // if there are only gift items, select the first one
    CartItem item = existingItems.stream().findFirst().orElseThrow();
    item.increaseQuantity(quantity);
    product.decreaseQuantity(quantity);
    return true;
  }

  /**
   * Increase quantity of an item in shopping cart
   */
  public boolean addItem(UUID cartId, Product product, int quantity) {
    if (product.getQuantity() < quantity)
      return false;

    Optional<CartItem> itemOpt = this.items.stream()
        .filter(i -> i.getId().equals(cartId)).findFirst();
    if (itemOpt.isEmpty())
      return false;
    CartItem item = itemOpt.get();
    item.increaseQuantity(quantity);
    product.decreaseQuantity(quantity);
    return true;
  }

  /**
   * Deduct quantity of an item in shopping cart:
   * - Decrease item quantity if given quantity < item's quantity.
   * - Or remove entire item from the cart if given quantity >= item's quantity.
   */
  public boolean removeItem(UUID cartId, Product product, int quantity) {
    Optional<CartItem> itemOpt = this.items.stream()
        .filter(i -> i.getId().equals(cartId)).findFirst();
    if (itemOpt.isEmpty())
      return false;

    CartItem item = itemOpt.get();
    if (item.getQuantity() >= quantity) {
      boolean isSuccess = this.items.removeIf(i -> i.getId().equals(cartId));
      if (isSuccess)
        product.increaseQuantity(item.getQuantity());
      return isSuccess;
    }

    item.decreaseQuantity(quantity);
    product.increaseQuantity(quantity);
    return true;
  }

  public List<CartItem> getItemsAppliedCoupon() {
    return this.items.stream().filter(CartItem::appliedCoupon).collect(Collectors.toList());
  }

  public void clearAppliedCoupon() {
    List<CartItem> appliedItems = getItemsAppliedCoupon();
    if (appliedItems.isEmpty()) {
      return;
    }
    for (CartItem item : appliedItems) {
      item.clearCoupon();
    }
  }

  /**
   * Only apply coupon, which targets to the product already in shopping cart.
   * Also clear any applied coupon in the cart before applying new one.
   */
  public boolean applyCoupon(Coupon coupon) {
    List<CartItem> itemsToApply = this.items.stream()
        .filter(i -> i.getProductId().equals(coupon.getTargetProduct())).collect(Collectors.toList());
    if (itemsToApply.isEmpty())
      return false;

    clearAppliedCoupon();
    for (CartItem item : itemsToApply) {
      item.applyCoupon(coupon);
    }
    return true;
  }

  /**
   * Only Sync product information (after update) for shopping cart has not been purchased
   *
   * @param product updated product
   */
  public void syncProductInfo(Product product) {
    if (isPurchased())
      throw new IllegalStateException("Can not update product info for purchased shopping cart");
    List<CartItem> itemsToUpdate = items.stream().filter(i -> i.getProductId().equals(product.getId())).collect(Collectors.toList());
    if (itemsToUpdate.isEmpty())
      return;
    for (CartItem item : itemsToUpdate) {
      item.syncProductInfo(product.getName(), product.getPrice(), product.getWeight(), product.getTaxType());
    }
  }

  public void purchase() {
    if(isPurchased())
      throw new IllegalStateException("Shopping cart already purchased");
    dateOfPurchase = Instant.now();
  }

  public boolean isPurchased() {
    return dateOfPurchase != null;
  }

  // Getter methods
  public Collection<CartItem> getItems() {
    return this.items;
  }

  public Instant getDateOfPurchase() {
    return dateOfPurchase;
  }
}
