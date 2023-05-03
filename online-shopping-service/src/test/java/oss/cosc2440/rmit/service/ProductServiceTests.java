package oss.cosc2440.rmit.service;

/**
* @author Group 8
*/

import org.junit.jupiter.api.Test;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.domain.ProductType;
import oss.cosc2440.rmit.domain.TaxType;
import oss.cosc2440.rmit.model.CreateProductModel;
import oss.cosc2440.rmit.model.SearchProductParameters;
import oss.cosc2440.rmit.model.UpdateProductModel;
import oss.cosc2440.rmit.seedwork.Constants;
import oss.cosc2440.rmit.seedwork.Helpers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTests {
  @Test
  public void listAllProductsShouldSuccess() {
    // Setup
    ClassLoader loader = ProductServiceTests.class.getClassLoader();
    ProductService productService = new ProductService(Helpers.getPathToFile(loader, Constants.PRODUCT_FILE_NAME));

    // Action
    List<Product> products = productService.listAllProducts(new SearchProductParameters());

    // Assert
    assertNotNull(products);
    assertTrue(products.size() > 0);
  }

  @Test
  public void createProductShouldSuccess() {
    // Setup
    ClassLoader loader = ProductServiceTests.class.getClassLoader();
    ProductService productService = new ProductService(Helpers.getPathToFile(loader, Constants.PRODUCT_FILE_NAME));

    // Action
    Product product1 = new Product("Product 1", "description", 1,10,0
            ,ProductType.DIGITAL,TaxType.TAX_FREE, true);

    CreateProductModel model = new CreateProductModel();
    model.setName(product1.getName());
    model.setDescription(product1.getDescription());
    model.setPrice(product1.getPrice());
    model.setQuantity(product1.getQuantity());
    model.setWeight(product1.getWeight());
    model.setType(product1.getType());
    model.setTaxType(product1.getTaxType());
    model.setCanUseAsGift(product1.canUseAsGift());

    // Assert
    assertTrue(productService.addProduct(model));
  }

  @Test
  public void createProductButAlreadyExistedShouldFail() {
    // Setup
    ClassLoader loader = ProductServiceTests.class.getClassLoader();
    ProductService productService = new ProductService(Helpers.getPathToFile(loader, Constants.PRODUCT_FILE_NAME));

    // Action
    Product product1 = new Product("Iphone 14", "description", 1,10,0
            ,ProductType.DIGITAL,TaxType.TAX_FREE, true);

    CreateProductModel model = new CreateProductModel();
    model.setName(product1.getName());
    model.setDescription(product1.getDescription());
    model.setPrice(product1.getPrice());
    model.setQuantity(product1.getQuantity());
    model.setWeight(product1.getWeight());
    model.setType(product1.getType());
    model.setTaxType(product1.getTaxType());
    model.setCanUseAsGift(product1.canUseAsGift());

    // Assert
    assertFalse(productService.addProduct(model));
  }

  @Test
  public void updateProductShouldSuccess() {
    // Setup
    ClassLoader loader = ProductServiceTests.class.getClassLoader();
    ProductService productService = new ProductService(Helpers.getPathToFile(loader, Constants.PRODUCT_FILE_NAME));

    // Action
    Optional<Product> productOptional = productService.findProduct(UUID.fromString("c6fbde46-272f-4f42-81b5-e2cd8058b638"));
    Product product = productOptional.get();

    UpdateProductModel uModel = new UpdateProductModel();
    uModel.setId(UUID.fromString("c6fbde46-272f-4f42-81b5-e2cd8058b638"));
    uModel.setName("Product 2");
    uModel.setDescription("product 2 description");
    uModel.setPrice(10.0);
    uModel.setQuantity(10);
    uModel.setWeight(0.0);
    uModel.setType(ProductType.DIGITAL);
    uModel.setTaxType(TaxType.LUXURY_TAX);
    uModel.setCanUseAsGift(false);

    boolean updated = productService.updateProduct(uModel);

    // Assert
    assertTrue(updated);
    assertEquals("Product 2", product.getName());

  }

  @Test
  public void updateProductButNotExistShouldFail() {
    // Setup
    ClassLoader loader = ProductServiceTests.class.getClassLoader();
    ProductService productService = new ProductService(Helpers.getPathToFile(loader, Constants.PRODUCT_FILE_NAME));

    // Action
    UpdateProductModel uModel = new UpdateProductModel();
    uModel.setId(UUID.randomUUID());
    uModel.setName("Product 2");
    uModel.setDescription("product 2 description");
    uModel.setPrice(10.0);
    uModel.setQuantity(10);
    uModel.setWeight(0.0);
    uModel.setType(ProductType.DIGITAL);
    uModel.setTaxType(TaxType.LUXURY_TAX);
    uModel.setCanUseAsGift(false);

    boolean updated = productService.updateProduct(uModel);

    // Assert
    assertFalse(updated);
  }
}
