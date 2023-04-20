package oss.cosc2440.rmit.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShoppingCartTests {

  ShoppingCart shoppingCart;

  @BeforeEach
  void setup() {
    PhysicalProduct physicalProduct;
    DigitalProduct digitalProduct;
    CartItem cartItem1;
    CartItem cartItem2;
    shoppingCart = new ShoppingCart();

    physicalProduct = new PhysicalProduct("book1", "book", 1, 1.1, TaxType.NORMAL_TAX, 1.2, false);

    digitalProduct = new DigitalProduct("Ebook1", "Ebook", 2, 1.2, TaxType.LUXURY_TAX, false);

    shoppingCart.addItem(digitalProduct);
    shoppingCart.addItem(physicalProduct);
  }

  @Test
  public void addItemShouldSuccess() {
    PhysicalProduct physicalProduct;
    DigitalProduct digitalProduct;
    physicalProduct = new PhysicalProduct("book2", "book", 1, 1.1, TaxType.NORMAL_TAX, 1.2, false);

    digitalProduct = new DigitalProduct("Ebook2", "Ebook", 2, 1.2, TaxType.LUXURY_TAX, false);

    assertTrue(shoppingCart.addItem(digitalProduct));
    assertTrue(shoppingCart.addItem(physicalProduct));

  }

  @Test
  public void addItemAlreadyExistedShouldFail() {
    PhysicalProduct physicalProduct;
    DigitalProduct digitalProduct;
    physicalProduct = new PhysicalProduct("book1", "book", 1, 1.1, TaxType.NORMAL_TAX, 1.2, false);

    digitalProduct = new DigitalProduct("Ebook1", "Ebook", 2, 1.2, TaxType.LUXURY_TAX, false);

    assertFalse(shoppingCart.addItem(digitalProduct));
    assertFalse(shoppingCart.addItem(physicalProduct));

  }

  @Test
  public void removeExistedItemShouldSuccess() {

    assertTrue(shoppingCart.removeItem("book1"));
    assertTrue(shoppingCart.removeItem("Ebook1"));

  }

  @Test
  public void removeNotExistedItemShouldFail() {

    assertFalse(shoppingCart.removeItem("Ebook2"));
    assertFalse(shoppingCart.removeItem("book2"));
  }

  @Test
  public void calculateCartAmountTest() {
    assertEquals(0.828, shoppingCart.totalAmount());
  }
}
