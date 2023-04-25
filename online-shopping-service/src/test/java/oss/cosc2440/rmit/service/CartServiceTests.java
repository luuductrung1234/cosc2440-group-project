package oss.cosc2440.rmit.service;

import org.junit.jupiter.api.Test;
import oss.cosc2440.rmit.domain.ShoppingCart;
import oss.cosc2440.rmit.seedwork.Constants;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartServiceTests {
  @Test
  public void getAllCartInAscendingWeightOrderTest() {
    // Setup
    ClassLoader loader = ProductServiceTests.class.getClassLoader();
    String pathToFile = Objects.requireNonNull(loader.getResource(Constants.CART_FILE_NAME)).getPath();
    CartService cartService = new CartService(pathToFile);

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
  public void addShoppingCartTest() {
    // TODO: write test
  }
}
