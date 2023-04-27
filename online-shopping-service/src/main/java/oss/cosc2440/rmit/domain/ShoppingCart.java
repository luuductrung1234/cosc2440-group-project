package oss.cosc2440.rmit.domain;

/**
* @author Group 8
*/

import oss.cosc2440.rmit.seedwork.Constants;
import oss.cosc2440.rmit.seedwork.Helpers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCart extends Domain<UUID> {

  /**
   * shopping cart attributes
   */
  private List<CartItem> items;
  private Instant dateOfPurchase;

  /**
   * Constructor
   */
  public ShoppingCart() {
    this(UUID.randomUUID(), null);
  }

  public ShoppingCart(UUID id, Instant dateOfPurchase) {
    super(id);
    this.items = new ArrayList<>();
    this.dateOfPurchase = dateOfPurchase;
  }

  public int totalQuantity() {
    return this.items.stream().mapToInt(CartItem::getQuantity).sum();
  }

  public double totalWeight() {
    return this.items.stream().mapToDouble(CartItem::getItemWeight).sum();
  }

  public double shippingFee() {
    return totalWeight() * Constants.BASE_FEE;
  }

  public BigDecimal totalDiscount() {
    return this.items.stream().map(CartItem::getDiscountAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal totalTax() {
    return this.items.stream().map(CartItem::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal totalOriginAmount() {
    return this.items.stream().map(CartItem::getOriginAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal totalAmount() {
    BigDecimal totalAmount = this.items.stream().map(CartItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    return totalAmount.add(BigDecimal.valueOf(shippingFee()));
  }

  /**
   * Add item to the shopping cart:
   * - Increase item quantity if it already existed in cart.
   * - Or add new item to the cart.
   * If there are more than 1 items with the same product, add to non-gift item
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
    if (nonGiftItemOpt.isPresent()) {
      CartItem nonGiftItem = nonGiftItemOpt.get();
      nonGiftItem.increaseQuantity(quantity);
      product.decreaseQuantity(quantity);
      return true;
    }

    // if there are only gift items, create new non-gift item
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

  /**
   * Increase quantity of an item in shopping cart
   */
  public boolean addItem(UUID itemId, Product product, int quantity) {
    if (product.getQuantity() < quantity)
      return false;

    Optional<CartItem> itemOpt = this.items.stream()
        .filter(i -> i.getId().equals(itemId)).findFirst();
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
  public boolean removeItem(UUID itemId, Product product, int quantity) {
    Optional<CartItem> itemOpt = this.items.stream()
        .filter(i -> i.getId().equals(itemId)).findFirst();
    if (itemOpt.isEmpty())
      return false;

    CartItem item = itemOpt.get();
    if (item.getQuantity() <= quantity) {
      boolean isSuccess = this.items.removeIf(i -> i.getId().equals(itemId));
      if (isSuccess)
        product.increaseQuantity(item.getQuantity());
      return isSuccess;
    }

    item.decreaseQuantity(quantity);
    product.increaseQuantity(quantity);
    return true;
  }

  public boolean setGiftMessage(UUID cartId, String message) {
    Optional<CartItem> itemOpt = this.items.stream()
        .filter(i -> i.getId().equals(cartId)).findFirst();
    if (itemOpt.isEmpty())
      return false;

    CartItem item = itemOpt.get();
    if (item.getQuantity() == 1) {
      item.setMessage(message);
      return true;
    }

    CartItem splitItem = item.split();
    splitItem.setMessage(message);
    this.items.add(splitItem);
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
    if (isPurchased()) return;
    List<CartItem> itemsToUpdate = items.stream().filter(i -> i.getProductId().equals(product.getId())).collect(Collectors.toList());
    if (itemsToUpdate.isEmpty()) return;
    for (CartItem item : itemsToUpdate) {
      item.syncProductInfo(product.getName(), product.getPrice(), product.getWeight(), product.getTaxType());
    }
  }

  public void purchase() {
    if (isPurchased()) return;
    dateOfPurchase = Instant.now();
  }

  public boolean isPurchased() {
    return dateOfPurchase != null;
  }

  /**
   * override static method Domain deserialize
   *
   * @param data serialized string data
   * @return new instance of Product
   */
  public static ShoppingCart deserialize(String data) {
    if (Helpers.isNullOrEmpty(data))
      throw new IllegalArgumentException("data to deserialize should not be empty!");
    String[] fields = data.split(",", 3);

    if (!fields[0].equalsIgnoreCase(ShoppingCart.class.getSimpleName()))
      return null;

    return new ShoppingCart(UUID.fromString(fields[1]),
        Helpers.isNullOrEmpty(fields[2]) ? null : Instant.parse(fields[2]));
  }

  // Getter & Setter methods
  public List<CartItem> getItems() {
    return this.items;
  }

  public void setItems(List<CartItem> items) {
    this.items = items;
  }

  public Instant getDateOfPurchase() {
    return dateOfPurchase;
  }
}
