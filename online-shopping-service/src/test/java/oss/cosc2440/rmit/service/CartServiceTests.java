package oss.cosc2440.rmit.service;

import org.junit.jupiter.api.Test;
import oss.cosc2440.rmit.mock.MockedStorageFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartServiceTests {
  @Test
  public void getAllCartInDescendingWeightOrderTest() {
    StorageFactory storageFactory = new MockedStorageFactory();
    ProductService productService = new ProductService(storageFactory);
    CartService cartService = new CartService(storageFactory, productService);

    // action
    List<ShoppingCart> carts = cartService.listAll();

    // assertions
    assertEquals(3, carts.size());
    assertTrue(carts.get(0).totalWeight() > carts.get(1).totalWeight());
    assertTrue(carts.get(1).totalWeight() > carts.get(2).totalWeight());
  }

  @Test
  public void addShoppingCartTest() {
    StorageFactory storageFactory = new MockedStorageFactory();
    ProductService productService = new ProductService(storageFactory);
    CartService cartService = new CartService(storageFactory, productService);
    List<ShoppingCart> carts = cartService.listAll();

    // before add
    assertEquals(3, carts.size());

    // action
    ShoppingCart shoppingCart = new ShoppingCart(productService);
    boolean isSuccess = shoppingCart.addItem("Code Complete");
    cartService.add(shoppingCart);

    // after add
    carts = cartService.listAll();
    assertTrue(isSuccess);
    assertEquals(4, carts.size());
  }
}
