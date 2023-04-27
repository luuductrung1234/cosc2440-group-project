package oss.cosc2440.rmit.domain;

/**
* @author Group 8
*/

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.UUID;
import org.junit.jupiter.api.Test;


public class SplittableTests {
  @Test
  public void splitCartItemsShouldSuccess() {
    // Set up
    CartItem cartItem = new CartItem(
      UUID.randomUUID(), 
      UUID.randomUUID(), 
      UUID.randomUUID(), 
      "Iphone 14", 
      1000, 
      5.5, 
      TaxType.LUXURY_TAX, 
      100, 
      "C001", 
      10, 
      CouponType.PERCENT, 
      "message");

      CartItem splitItem = cartItem.split();

      // Assert
      assertEquals(cartItem.getProductName(), splitItem.getProductName());
      assertEquals(99, cartItem.getQuantity());
      assertEquals(1, splitItem.getQuantity());
  }
}
