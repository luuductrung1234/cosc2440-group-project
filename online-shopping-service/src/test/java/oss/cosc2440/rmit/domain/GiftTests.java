package oss.cosc2440.rmit.domain;

/**
* @author Group 8
*/

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;


public class GiftTests {
  @Test
  public void productAsGiftTest() {
    // Set up
    UUID productId = UUID.randomUUID();
    Product product = new Product(productId, "Product 1", "Description", 10, 110, 0.1, ProductType.PHYSICAL, TaxType.LUXURY_TAX, true);
    
    CartItem cartItem = new CartItem(
      UUID.randomUUID(), 
      UUID.randomUUID(), 
      productId, 
      product.getName(), 
      product.getPrice(), 
      product.getWeight(), 
      product.getTaxType(), 
      product.getQuantity(), 
      "C001", 
      10, 
      CouponType.PERCENT, 
      "message");
    
    // Assert
    assertTrue(cartItem.isGift());
    cartItem.setMessage("This is a gift");
    String myGiftMessage = cartItem.getMessage();
    assertEquals("This is a gift", myGiftMessage);
  }
}
