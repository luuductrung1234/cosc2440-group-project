package oss.cosc2440.rmit.service;

/**
* @author Group 8
*/

import org.junit.jupiter.api.Test;

import oss.cosc2440.rmit.domain.*;
import oss.cosc2440.rmit.seedwork.Constants;
import oss.cosc2440.rmit.seedwork.Helpers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTests {
  @Test
  public void getAllCartInAscendingWeightOrderTest() {
    // Setup
    ClassLoader loader = CartServiceTests.class.getClassLoader();
    CartService cartService = new CartService(Helpers.getPathToFile(loader, Constants.CART_FILE_NAME));

    // Action
    List<ShoppingCart> carts = cartService.listAll();

    // Assert
    assertNotNull(carts);
    assertTrue(carts.size() > 0);

    assertTrue(carts.get(0).totalWeight() <= carts.get(1).totalWeight());
    assertTrue(carts.get(1).totalWeight() <= carts.get(2).totalWeight());
    assertTrue(carts.get(2).totalWeight() <= carts.get(3).totalWeight());
    assertTrue(carts.get(3).totalWeight() <= carts.get(4).totalWeight());
  }

  @Test
  public void addShoppingCartShouldSuccess() {
    // Setup
    ClassLoader loader = CartServiceTests.class.getClassLoader();
    CartService cartService = new CartService(Helpers.getPathToFile(loader, Constants.CART_FILE_NAME));

    // Action
    ShoppingCart cart1 = new ShoppingCart();
    int cartSize = cartService.listAll().size();
    cartService.submit(cart1);

    // Assert
    assertEquals(cartSize+1, cartService.listAll().size());
  }

  
  @Test 
  public void syncProductInfoShouldSuccess(){
    // Setup
    ClassLoader loader = CartServiceTests.class.getClassLoader();
    ProductService productService = new ProductService(Helpers.getPathToFile(loader, Constants.PRODUCT_FILE_NAME));
    CartService cartService = new CartService(Helpers.getPathToFile(loader, Constants.CART_FILE_NAME));
    ShoppingCart unPurchasedCart = cartService.listAll().stream().filter(c -> !c.isPurchased()).findFirst().orElseThrow();
    CartItem unPurchasedCartItem = unPurchasedCart.getItems().stream().findFirst().orElseThrow();
    Product productToUpdate = productService.findProduct(unPurchasedCartItem.getProductId()).orElseThrow();

    // Before Update
    double originalProductPrice = productToUpdate.getPrice();
    assertEquals(originalProductPrice, unPurchasedCartItem.getProductPrice());

    // Action
    double newProductPrice = originalProductPrice + 10.0;
    productToUpdate.update(
        productToUpdate.getName(),
        productToUpdate.getDescription(),
        productToUpdate.getQuantity(),
        newProductPrice,
        productToUpdate.getWeight(),
        productToUpdate.getTaxType(),
        productToUpdate.canUseAsGift());
    cartService.syncProductInfo(productToUpdate);

    // After Update
    assertEquals(newProductPrice, unPurchasedCartItem.getProductPrice());
  }

  @Test
  public void syncCouponInfoShouldSuccess(){
    // Setup
    ClassLoader loader = CartServiceTests.class.getClassLoader();
    ProductService productService = new ProductService(Helpers.getPathToFile(loader, Constants.PRODUCT_FILE_NAME));
    CartService cartService = new CartService(Helpers.getPathToFile(loader, Constants.CART_FILE_NAME));
    ShoppingCart unPurchasedCart = cartService.listAll().stream().filter(c -> !c.isPurchased()).findFirst().orElseThrow();
    CartItem unPurchasedCartItem = unPurchasedCart.getItems().stream().filter(CartItem::appliedCoupon).findFirst().orElseThrow();
    Coupon couponToUpdate = productService.findCoupon(unPurchasedCartItem.getCouponCode()).orElseThrow();

    // Before Update
    double originalCouponValue = couponToUpdate.getValue();
    assertEquals(originalCouponValue, unPurchasedCartItem.getCouponValue());

    // Action
    double newCouponValue = originalCouponValue + 5.0;
    if(couponToUpdate.getType() == CouponType.PRICE)
      couponToUpdate.update(newCouponValue, 0);
    else
      couponToUpdate.update(0.0, (int) newCouponValue);
    cartService.syncCouponInfo(couponToUpdate);

    // After Update
    assertEquals(newCouponValue, unPurchasedCartItem.getCouponValue());
  }
}
