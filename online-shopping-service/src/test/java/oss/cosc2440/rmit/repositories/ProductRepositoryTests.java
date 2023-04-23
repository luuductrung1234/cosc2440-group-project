package oss.cosc2440.rmit.repositories;

import org.junit.jupiter.api.Test;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.repository.FileProductRepositoryImpl;
import oss.cosc2440.rmit.repository.ProductRepository;
import oss.cosc2440.rmit.seedwork.Constants;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductRepositoryTests {
  @Test
  public void listAllProductShouldSuccess() {
    // Setup
    ProductRepository productRepository = new FileProductRepositoryImpl(
        Objects.requireNonNull(ProductRepositoryTests.class.getClassLoader().getResource(Constants.PRODUCT_FILE_NAME)).getPath());

    // Action
    List<Product> products = productRepository.listAll();

    // Assert
    assertNotNull(products);
    assertTrue(products.size() > 0);
  }
}
