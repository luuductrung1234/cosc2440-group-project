package oss.cosc2440.rmit.service;

import org.junit.jupiter.api.Test;
import oss.cosc2440.rmit.domain.PhysicalProduct;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.domain.ProductType;
import oss.cosc2440.rmit.mock.MockedStorageFactory;
import oss.cosc2440.rmit.model.CreateProductModel;
import oss.cosc2440.rmit.model.SearchProductParameters;
import oss.cosc2440.rmit.model.UpdateProductModel;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTests {
  @Test
  public void createProductShouldSuccess() {
    // setup
    String name = "Iphone 14";
    String description = "best phone in 2023";
    int quantity = 1;
    double price = 899;
    double weight = 0.2;
    ProductService productService = new ProductService(new MockedStorageFactory());

    // before create new product
    List<Product> products = productService.listAll(new SearchProductParameters());
    assertEquals(6, products.size());

    // action
    CreateProductModel model = new CreateProductModel();
    model.setName(name);
    model.setDescription(description);
    model.setQuantity(quantity);
    model.setPrice(price);
    model.setType(ProductType.PHYSICAL);
    model.setWeight(weight);
    boolean isSuccess = productService.addProduct(model);

    // after create new product
    assertTrue(isSuccess);
    products = productService.listAll(new SearchProductParameters());
    assertEquals(7, products.size());
    PhysicalProduct addedProduct = (PhysicalProduct) productService.findByName(name).orElse(null);
    assertNotNull(addedProduct);
    assertEquals(name, addedProduct.getName());
    assertEquals(description, addedProduct.getDescription());
    assertEquals(quantity, addedProduct.getQuantity());
    assertEquals(price, addedProduct.getPrice());
    assertEquals(weight, addedProduct.getWeight());
  }

  @Test
  public void createProductButAlreadyExistedShouldFail() {
    // setup
    String name = "Apple Pen";
    ProductService productService = new ProductService(new MockedStorageFactory());

    // before create new product
    List<Product> products = productService.listAll(new SearchProductParameters());
    assertEquals(6, products.size());

    // action
    CreateProductModel model = new CreateProductModel();
    model.setName(name);
    model.setDescription("Apple a electric pen");
    model.setQuantity(1);
    model.setPrice(329.9);
    model.setType(ProductType.PHYSICAL);
    model.setWeight(0.2);
    boolean isSuccess = productService.addProduct(model);

    // after create new product
    assertFalse(isSuccess);
    products = productService.listAll(new SearchProductParameters());
    assertEquals(6, products.size());
  }

  @Test
  public void updateProductShouldSuccess() {
    // setup
    String name = "Apple Pen";
    ProductService productService = new ProductService(new MockedStorageFactory());

    // before update
    PhysicalProduct originalProduct = (PhysicalProduct) productService.findByName(name).get();
    assertEquals(name, originalProduct.getName());
    assertEquals("Apple a electric pen", originalProduct.getDescription());
    assertEquals(0, originalProduct.getQuantity());
    assertEquals(250, originalProduct.getPrice());
    assertEquals(0.1, originalProduct.getWeight());

    // action
    UpdateProductModel model = new UpdateProductModel();
    model.setName(name);
    model.setDescription("New version of apple electric pen");
    model.setQuantity(5);
    model.setPrice(329.9);
    model.setWeight(0.12);
    boolean isSuccess = productService.updateProduct(model);

    // after update
    assertTrue(isSuccess);
    PhysicalProduct updatedProduct = (PhysicalProduct) productService.findByName(name).get();
    assertEquals(name, updatedProduct.getName());
    assertEquals("New version of apple electric pen", updatedProduct.getDescription());
    assertEquals(5, updatedProduct.getQuantity());
    assertEquals(329.9, updatedProduct.getPrice());
    assertEquals(0.12, updatedProduct.getWeight());
  }

  @Test
  public void updateProductButNotExistShouldFail() {
    // setup
    String name = "JBL Speaker";
    ProductService productService = new ProductService(new MockedStorageFactory());

    // before update
    Optional<Product> originalProduct = productService.findByName(name);
    assertTrue(originalProduct.isEmpty());

    // action
    UpdateProductModel model = new UpdateProductModel();
    model.setName(name);
    model.setDescription("enjoyable speaker");
    model.setQuantity(3);
    model.setPrice(159.9);
    model.setWeight(0.45);
    boolean isSuccess = productService.updateProduct(model);

    // after update
    assertFalse(isSuccess);
    Optional<Product> updatedProduct = productService.findByName(name);
    assertTrue(updatedProduct.isEmpty());
  }
}
