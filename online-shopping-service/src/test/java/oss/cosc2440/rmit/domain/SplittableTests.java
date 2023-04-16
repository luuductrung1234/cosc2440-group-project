package oss.cosc2440.rmit.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SplittableTests {
  @Test
  public void splitPhysicalProductTest() {
    Splittable<Product> originalProduct = new PhysicalProduct("Iphone 14",
        "best phone in 2023", 3, 899, 0.2);

    assertTrue(originalProduct instanceof Splittable);
    assertTrue(originalProduct instanceof Product);
    assertTrue(originalProduct instanceof PhysicalProduct);

    Product splitProduct = originalProduct.splitOne();

    assertTrue(splitProduct instanceof Product);
    assertTrue(splitProduct instanceof PhysicalProduct);
    assertEquals(((Product) originalProduct).getName(), splitProduct.getName());
    assertEquals(2, ((Product) originalProduct).getQuantity());
    assertEquals(1, splitProduct.getQuantity());
  }

  @Test
  public void splitDigitalProductTest() {
    Splittable<Product> originalProduct = new DigitalProduct("Netflix Subscription",
        "One month premium subscription", 2, 2.9);

    assertTrue(originalProduct instanceof Splittable);
    assertTrue(originalProduct instanceof Product);
    assertTrue(originalProduct instanceof DigitalProduct);

    Product splitProduct = originalProduct.splitOne();

    assertTrue(splitProduct instanceof Product);
    assertTrue(splitProduct instanceof DigitalProduct);
    assertEquals(((Product) originalProduct).getName(), splitProduct.getName());
    assertEquals(1, ((Product) originalProduct).getQuantity());
    assertEquals(1, splitProduct.getQuantity());
  }
}
