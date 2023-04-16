package oss.cosc2440.rmit.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GiftTests {
  @Test
  public void purchasePhysicalProductAsGiftTest() {
    String message = "Happy birthday";

    Gift gift = new PhysicalProduct("Iphone 14",
        "best phone in 2023", 3, 899, 0.2);

    gift.setMessage(message);

    assertTrue(gift instanceof Gift);
    assertTrue(gift instanceof Product);
    assertTrue(gift instanceof PhysicalProduct);
    assertEquals(message, gift.getMessage());
  }

  @Test
  public void purchaseDigitalProductAsGiftTest() {
    String message = "Enjoy movies";

    Gift gift = new DigitalProduct("Netflix Subscription",
        "One month premium subscription", 2, 2.9);

    gift.setMessage(message);

    assertTrue(gift instanceof Gift);
    assertTrue(gift instanceof Product);
    assertTrue(gift instanceof DigitalProduct);
    assertEquals(message, gift.getMessage());
  }
}
