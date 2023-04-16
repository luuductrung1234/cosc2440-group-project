package oss.cosc2440.rmit.service;

import org.junit.jupiter.api.Test;
import oss.cosc2440.rmit.domain.DigitalProduct;
import oss.cosc2440.rmit.domain.PhysicalProduct;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.mock.MockedStorageFactory;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTests {
  @Test
  public void addItemShouldSuccess() {
    String targetProductName = "SamSung Monitor 23inch";
    ProductService productService = new ProductService(new MockedStorageFactory());
    ShoppingCart shoppingCart = new ShoppingCart(productService);
    Product product = productService.findByName(targetProductName).get();

    // before add
    assertEquals(0, shoppingCart.totalQuantity());
    assertEquals(4, product.getQuantity());

    // action
    boolean isSuccess = shoppingCart.addItem(targetProductName);

    // after add
    assertTrue(isSuccess);
    assertEquals(1, shoppingCart.totalQuantity());
    assertEquals(2.0, shoppingCart.totalWeight());
    assertEquals(80.7, shoppingCart.cartAmount());
    assertEquals(3, product.getQuantity());
  }

  @Test
  public void addItemAlreadyExistedShouldFail() {
    String targetProductName = "Clean Code";
    ProductService productService = new ProductService(new MockedStorageFactory());
    ShoppingCart shoppingCart = new ShoppingCart(productService);
    Product product = productService.findByName(targetProductName).get();

    // before add
    assertEquals(0, shoppingCart.totalQuantity());
    assertEquals(3, product.getQuantity());

    // action
    boolean isSuccess = shoppingCart.addItem(targetProductName);

    // after add first time
    assertTrue(isSuccess);
    assertEquals(1, shoppingCart.totalQuantity());
    assertEquals(0, shoppingCart.totalWeight());
    assertEquals(35.7, shoppingCart.cartAmount());
    assertEquals(2, product.getQuantity());

    // action
    isSuccess = shoppingCart.addItem(targetProductName);

    // after add second time
    assertFalse(isSuccess);
    assertEquals(1, shoppingCart.totalQuantity());
    assertEquals(0, shoppingCart.totalWeight());
    assertEquals(35.7, shoppingCart.cartAmount());
    assertEquals(2, product.getQuantity());
  }

  @Test
  public void addItemButEmptyShouldFail() {
    String targetProductName = "Apple Pen";
    ProductService productService = new ProductService(new MockedStorageFactory());
    ShoppingCart shoppingCart = new ShoppingCart(productService);
    Product product = productService.findByName(targetProductName).get();

    // before add
    assertEquals(0, shoppingCart.totalQuantity());
    assertEquals(0, product.getQuantity());

    // action
    boolean isSuccess = shoppingCart.addItem(targetProductName);

    // after add
    assertFalse(isSuccess);
    assertEquals(0, shoppingCart.totalQuantity());
    assertEquals(0, product.getQuantity());
  }

  @Test
  public void removeExistedItemShouldSuccess() {
    String targetProductName = "SamSung Monitor 23inch";
    ProductService productService = new ProductService(new MockedStorageFactory());
    ShoppingCart shoppingCart = new ShoppingCart(productService);
    Product product = productService.findByName(targetProductName).get();

    // before add
    assertEquals(0, shoppingCart.totalQuantity());
    assertEquals(4, product.getQuantity());

    // action
    boolean addSuccess = shoppingCart.addItem(targetProductName);

    // after add
    assertTrue(addSuccess);
    assertEquals(1, shoppingCart.totalQuantity());
    assertEquals(3, product.getQuantity());

    // action
    boolean removeSuccess = shoppingCart.removeItem(targetProductName);

    // after remove
    assertTrue(removeSuccess);
    assertEquals(0, shoppingCart.totalQuantity());
    assertEquals(4, product.getQuantity());
  }

  @Test
  public void removeNotExistedItemShouldFail() {
    String targetProductName = "SamSung Monitor 23inch";
    ProductService productService = new ProductService(new MockedStorageFactory());
    ShoppingCart shoppingCart = new ShoppingCart(productService);
    Product product = productService.findByName(targetProductName).get();

    // before add
    assertEquals(0, shoppingCart.totalQuantity());
    assertEquals(4, product.getQuantity());

    // action
    boolean removeSuccess = shoppingCart.removeItem(targetProductName);

    // after remove
    assertFalse(removeSuccess);
    assertEquals(0, shoppingCart.totalQuantity());
    assertEquals(4, product.getQuantity());
  }

  @Test
  public void setMessageToExistedItemShouldSuccess() {
    String targetProductName = "SamSung Monitor 23inch";
    ProductService productService = new ProductService(new MockedStorageFactory());
    ShoppingCart shoppingCart = new ShoppingCart(productService);
    Product originalProduct = productService.findByName(targetProductName).get();

    // action
    boolean addSuccess = shoppingCart.addItem(targetProductName);

    // after add
    assertTrue(addSuccess);

    // before set gift message
    Product reservedProduct = productService.findReservedProductByName(shoppingCart.getId(), targetProductName).get();
    assertNull(originalProduct.getMessage());
    assertNull(reservedProduct.getMessage());

    // action
    boolean setMessageSuccess = shoppingCart.setItemMessage(targetProductName, "Happy coding");

    // after set gift message
    assertTrue(setMessageSuccess);
    assertNull(originalProduct.getMessage());
    assertEquals("Happy coding", reservedProduct.getMessage());
  }

  @Test
  public void setMessageToNotExistedItemShouldFail() {
    String targetProductName = "SamSung Monitor 23inch";
    ProductService productService = new ProductService(new MockedStorageFactory());
    ShoppingCart shoppingCart = new ShoppingCart(productService);
    Product originalProduct = productService.findByName(targetProductName).get();

    // before set gift message
    assertNull(originalProduct.getMessage());

    // action
    boolean setMessageSuccess = shoppingCart.setItemMessage(targetProductName, "Happy coding");

    // after set gift message
    assertFalse(setMessageSuccess);
    assertNull(originalProduct.getMessage());
  }

  @Test
  public void calculateCartAmountTest() {
    String targetDigitalProduct = "Netflix Subscription";
    String targetPhysicalProduct = "Table Lamp";
    ProductService productService = new ProductService(new MockedStorageFactory());
    ShoppingCart shoppingCart = new ShoppingCart(productService);
    PhysicalProduct physicalProduct = (PhysicalProduct) productService.findByName(targetPhysicalProduct).get();
    DigitalProduct digitalProduct = (DigitalProduct) productService.findByName(targetDigitalProduct).get();

    // action
    boolean isSuccess = shoppingCart.addItem(targetDigitalProduct);
    assertTrue(isSuccess);
    isSuccess = shoppingCart.addItem(targetPhysicalProduct);
    assertTrue(isSuccess);

    // assertions
    assertEquals(2, shoppingCart.totalQuantity());
    assertEquals(physicalProduct.getWeight(), shoppingCart.totalWeight());
    double baseFee = 0.1;
    double expectedCartAmount = physicalProduct.getPrice() + digitalProduct.getPrice() + (physicalProduct.getWeight() * baseFee);
    assertEquals(expectedCartAmount, shoppingCart.cartAmount());
  }
}
