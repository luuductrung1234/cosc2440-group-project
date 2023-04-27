package oss.cosc2440.rmit.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ProductTests {
  @Test
  public void physicalProductAsProductTest() {
    // Set up
    Product product = new Product("Product", "Description", 1, 10.0, 0.1, ProductType.PHYSICAL, TaxType.TAX_FREE, true);

    // Assert
    assertEquals(ProductType.PHYSICAL, product.getType());
    assertFalse(product.getType() == ProductType.DIGITAL);
  }

  @Test
  public void digitalProductAsProductTest() {
    // Set up
    Product product = new Product("Product", "Description", 1, 10.0, 0.1, ProductType.DIGITAL, TaxType.TAX_FREE, true);

    // Assert
    assertEquals(ProductType.DIGITAL, product.getType());
    assertFalse(product.getType() == ProductType.PHYSICAL);
  }

  @Test
  public void increaseProductQuantityTest() {
    // Set up
    Product product = new Product("Product", "Description", 1, 10.0, 0.1, ProductType.DIGITAL, TaxType.TAX_FREE, true);

    // Assert
    assertEquals(1, product.getQuantity());
    product.increaseQuantity(1);
    assertEquals(2, product.getQuantity());
  }

  @Test
  public void decreaseProductQuantityTest() {
    // Set up
    Product product = new Product("Product", "Description", 1, 10.0, 0.1, ProductType.DIGITAL, TaxType.TAX_FREE, true);

    // Assert
    assertEquals(1, product.getQuantity());
    product.increaseQuantity(1);
    assertEquals(2, product.getQuantity());
  }

  @Test
  public void getPresentationStringOfPhysicalProductTest() {
    // Set up
    Product p1 = new Product("Product 1", "Description 1", 10, 10.0, 1.0, ProductType.DIGITAL, TaxType.TAX_FREE, true);
    Product p2 = new Product("Product 2", "Description 2", 20, 20.0, 2.0, ProductType.PHYSICAL, TaxType.TAX_FREE, false);
    
    // Assert
    assertEquals("DIGITAL  - Product 1", p1.toString());
    assertEquals("PHYSICAL - Product 2", p2.toString());
  }

  @Test
  public void updateProductAsProductTest() {
    // Set up
    Product product = new Product("Product 1", "Description 1", 10, 10.0, 1.0, ProductType.PHYSICAL, TaxType.TAX_FREE, true);

    product.update("Iphone 12", "This is an iphone", 100, 1000, 0.2, TaxType.LUXURY_TAX, true);

    // Assert
    assertEquals("Iphone 12", product.getName());
    assertEquals("This is an iphone", product.getDescription());
    assertEquals(100, product.getQuantity());
    assertEquals(1000.0, product.getPrice());
    assertEquals(0.2, product.getWeight());
    assertEquals(TaxType.LUXURY_TAX, product.getTaxType());
    assertTrue(product.canUseAsGift());
  }
}
