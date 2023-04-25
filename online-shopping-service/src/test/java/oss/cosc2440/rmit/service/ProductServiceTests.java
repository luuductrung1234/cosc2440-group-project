package oss.cosc2440.rmit.service;

import org.junit.jupiter.api.Test;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.model.SearchProductParameters;
import oss.cosc2440.rmit.seedwork.Constants;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductServiceTests {
  @Test
  public void listAllProductsShouldSuccess() {
    // Setup
    ClassLoader loader = ProductServiceTests.class.getClassLoader();
    String pathToFile = Objects.requireNonNull(loader.getResource(Constants.PRODUCT_FILE_NAME)).getPath();
    ProductService productService = new ProductService(pathToFile);

    // Action
    List<Product> products = productService.listAll(new SearchProductParameters());

    // Assert
    assertNotNull(products);
    assertTrue(products.size() > 0);
  }

  @Test
  public void createProductShouldSuccess() {
    // TODO: write test
  }

  @Test
  public void createProductButAlreadyExistedShouldFail() {
    // TODO: write test
  }

  @Test
  public void updateProductShouldSuccess() {
    // TODO: write test
  }

  @Test
  public void updateProductButNotExistShouldFail() {
    // TODO: write test
  }
}
