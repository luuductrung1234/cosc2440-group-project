package oss.cosc2440.rmit.service;

/**
* @author Group 8
*/

import org.junit.jupiter.api.Test;
import oss.cosc2440.rmit.domain.ShoppingCart;
import oss.cosc2440.rmit.seedwork.Constants;
import oss.cosc2440.rmit.seedwork.Helpers;

import java.util.List;

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
}
