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
  private Set<CartItem> items = new HashSet<CartItem>();
  private String couponCode = "";
  private String taxType;
  private Instant dateOfPurchase;

  /**
   * Constructor
   */
  public ShoppingCart() {
    this(UUID.randomUUID(), new HashSet<>(), null);

  }

  public ShoppingCart(UUID id, Set<CartItem> items, Instant dateOfPurchase) {
    // do we need date constructor parameter?
    // I think it is better to make new calender here
    super(id);
    this.items = items;

    this.dateOfPurchase = dateOfPurchase;
  }

  public String getCouponCode() {
    return couponCode;
  }

  public void setCouponCode(String couponCode) {
    this.couponCode = couponCode;
  }

  public int totalQuantity() {
    return this.items.stream().mapToInt(CartItem::getQuantity).sum();
  }

  public double totalWeight() {
    return this.items.stream().mapToDouble(CartItem::getProductWeight).sum();

  }

  public double totalAmount() {
    double totalAmount = 0;
    for (CartItem cartItem : items) {
      totalAmount += getPrice(cartItem);
    }
    return totalAmount;

  }

  public boolean addItem(Product product) {
    boolean result = true;
    for (CartItem cartItem : items) {
      if (product.getName().equals(cartItem.getProductName())) {
        result = false;
        break;
      }
    }
    if (result == true) {
      double weight = (product instanceof PhysicalProduct) ? ((PhysicalProduct) product).getWeight() : 0;

      CartItem cartItem = new CartItem(UUID.randomUUID(), product.getId(), product.getName(), product.getPrice(),
          weight,
          product.getTaxType(), product.getQuantity());
      this.items.add(cartItem);
    }

    return result;

  }

  public boolean removeItem(String productName) {
    for (CartItem item : this.items) {
      if (item.getProductName().equals(productName)) {
        this.items.remove(productName);
        return true;
      }

    }
    return false;
  }

  public double getPrice(CartItem cartItem) {
    double couponAppliedPrice = 0;
    double tax = 0;
    if (cartItem.getCouponCode().equals(this.couponCode)) {
      couponAppliedPrice += cartItem.getProductPrice() * 0.1;
      // coupon service
    } else {
      couponAppliedPrice += cartItem.getProductPrice();
    }
    if (cartItem.getTaxType().equals("TAX_FREE")) {
      tax = 0;
    } else if (cartItem.getTaxType().equals("NORMAL_TAX")) {
      tax = cartItem.getProductPrice() * 0.1;
    } else {
      tax = cartItem.getProductPrice() * 0.2;
    }
    return couponAppliedPrice + tax;
  }

  public void syncProductInfo(Product product) {
    if (isPurchased())
      throw new IllegalStateException("Can not update product info for purchased shopping cart");
    Optional<CartItem> itemOpt = items.stream().filter(i -> i.getProductId().equals(product.getId())).findFirst();
    if (itemOpt.isEmpty())
      return;
    CartItem item = itemOpt.get();
    if (product.getType() == ProductType.PHYSICAL) {
      item.syncProductInfo(product.getName(), product.getPrice(), ((PhysicalProduct) product).getWeight(),
          product.getTaxType());
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

  public Instant getDateOfPurchase() {
    return dateOfPurchase;
  }
}
