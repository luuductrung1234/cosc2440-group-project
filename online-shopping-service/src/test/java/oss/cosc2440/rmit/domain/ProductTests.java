package oss.cosc2440.rmit.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTests {
  @Test
  public void physicalProductAsProductTest() {
    // setup
    String name = "Iphone 14";
    String description = "best phone in 2023";
    int quantity = 1;
    double price = 899;
    double weight = 0.2;

    // action
    Product product = new PhysicalProduct(name, description, quantity, price, weight);

    // assertions
    assertTrue(product instanceof Product);
    assertEquals(name, product.getName());
    assertEquals(description, product.getDescription());
    assertEquals(quantity, product.getQuantity());
    assertEquals(price, product.getPrice());
    assertEquals(ProductType.PHYSICAL, product.getType());

    assertTrue(product instanceof PhysicalProduct);
    assertFalse(product instanceof DigitalProduct);
    assertEquals(weight, ((PhysicalProduct) product).getWeight());
  }

  @Test
  public void digitalProductAsProductTest() {
    // setup
    String name = "Netflix Subscription";
    String description = "One month premium subscription";
    int quantity = 1;
    double price = 2.9;

    // action
    Product product = new DigitalProduct(name, description, quantity, price);

    // assertions
    assertTrue(product instanceof Product);
    assertEquals(name, product.getName());
    assertEquals(description, product.getDescription());
    assertEquals(quantity, product.getQuantity());
    assertEquals(price, product.getPrice());
    assertEquals(ProductType.DIGITAL, product.getType());

    assertTrue(product instanceof DigitalProduct);
    assertFalse(product instanceof PhysicalProduct);
  }

  @Test
  public void increaseProductQuantityTest() {
    PhysicalProduct physicalProduct = new PhysicalProduct("Iphone 14",
        "best phone in 2023", 1, 899, 0.2);
    DigitalProduct digitalProduct = new DigitalProduct("Netflix Subscription",
        "One month premium subscription", 2, 2.9);

    assertEquals(1, physicalProduct.getQuantity());
    assertEquals(2, digitalProduct.getQuantity());

    physicalProduct.increaseQuantity();
    digitalProduct.increaseQuantity();

    assertEquals(2, physicalProduct.getQuantity());
    assertEquals(3, digitalProduct.getQuantity());

    Product product1 = physicalProduct;
    Product product2 = digitalProduct;

    product1.increaseQuantity();
    product2.increaseQuantity();

    assertEquals(3, physicalProduct.getQuantity());
    assertEquals(4, digitalProduct.getQuantity());
  }

  @Test
  public void decreaseProductQuantityTest() {
    PhysicalProduct physicalProduct = new PhysicalProduct("Iphone 14",
        "best phone in 2023", 3, 899, 0.2);
    DigitalProduct digitalProduct = new DigitalProduct("Netflix Subscription",
        "One month premium subscription", 4, 2.9);

    assertEquals(3, physicalProduct.getQuantity());
    assertEquals(4, digitalProduct.getQuantity());

    physicalProduct.decreaseQuantity();
    digitalProduct.decreaseQuantity();

    assertEquals(2, physicalProduct.getQuantity());
    assertEquals(3, digitalProduct.getQuantity());

    Product product1 = physicalProduct;
    Product product2 = digitalProduct;

    product1.decreaseQuantity();
    product2.decreaseQuantity();

    assertEquals(1, physicalProduct.getQuantity());
    assertEquals(2, digitalProduct.getQuantity());
  }

  @Test
  public void distinguishProductsByTypeTest() {
    PhysicalProduct iphone = new PhysicalProduct("Iphone 14",
        "best phone in 2023", 3, 899, 0.2);
    PhysicalProduct book = new PhysicalProduct("Clean Code",
        "book from uncle Bob", 1, 35.7, 0.3);
    DigitalProduct netflixSub = new DigitalProduct("Netflix Subscription",
        "One month premium subscription", 2, 2.9);

    List<Product> products = new ArrayList<>() {{
      add(iphone);
      add(book);
      add(netflixSub);
    }};

    // looking for physical products
    List<Product> physicalProducts = products.stream()
        .filter(p -> p.getType().equals(ProductType.PHYSICAL))
        .map(p -> (PhysicalProduct) p)
        .sorted(Comparator.comparingDouble(PhysicalProduct::getWeight).reversed())
        .collect(Collectors.toList());

    assertEquals(2, physicalProducts.size());

    assertEquals(book.getName(), physicalProducts.get(0).getName());
    assertEquals(book.getDescription(), physicalProducts.get(0).getDescription());
    assertEquals(book.getQuantity(), physicalProducts.get(0).getQuantity());
    assertEquals(book.getPrice(), physicalProducts.get(0).getPrice());
    assertEquals(ProductType.PHYSICAL, physicalProducts.get(0).getType());
    assertEquals(book.getWeight(), ((PhysicalProduct) physicalProducts.get(0)).getWeight());

    assertEquals(iphone.getName(), physicalProducts.get(1).getName());
    assertEquals(iphone.getDescription(), physicalProducts.get(1).getDescription());
    assertEquals(iphone.getQuantity(), physicalProducts.get(1).getQuantity());
    assertEquals(iphone.getPrice(), physicalProducts.get(1).getPrice());
    assertEquals(ProductType.PHYSICAL, physicalProducts.get(1).getType());
    assertEquals(iphone.getWeight(), ((PhysicalProduct) physicalProducts.get(1)).getWeight());

    // looking for digital products
    List<Product> digitalProducts = products.stream()
        .filter(p -> p.getType().equals(ProductType.DIGITAL))
        .collect(Collectors.toList());

    assertEquals(1, digitalProducts.size());

    assertEquals(netflixSub.getName(), digitalProducts.get(0).getName());
    assertEquals(netflixSub.getDescription(), digitalProducts.get(0).getDescription());
    assertEquals(netflixSub.getQuantity(), digitalProducts.get(0).getQuantity());
    assertEquals(netflixSub.getPrice(), digitalProducts.get(0).getPrice());
    assertEquals(ProductType.DIGITAL, digitalProducts.get(0).getType());
  }

  @Test
  public void getPresentationStringOfPhysicalProductTest() {
    String name = "Iphone 14";
    Product product = new PhysicalProduct(name,
        "best phone in 2023", 1, 899, 0.2);

    String presentationString = product.toString();

    assertEquals(String.format("%s - %s", ProductType.PHYSICAL, name), presentationString);
  }

  @Test
  public void getPresentationStringOfDigitalProductTest() {
    String name = "Netflix Subscription";
    Product product = new DigitalProduct(name,
        "One month premium subscription", 2, 2.9);

    String presentationString = product.toString();

    assertEquals(String.format("%s - %s", ProductType.DIGITAL, name), presentationString);
  }

  @Test
  public void updatePhysicalProductAsProductTest() {
    // setup
    String name = "Iphone 14";
    String updatedDescription = "best phone in 2022";
    int updatedQuantity = 5;
    double updatedPrice = 780;
    double updatedWeight = 0.3;

    // action
    PhysicalProduct product = new PhysicalProduct(name,
        "best phone in 2023", 1, 899, 0.2);

    product.update(updatedDescription, updatedQuantity, updatedPrice, updatedWeight);

    // assertions
    assertEquals(name, product.getName());
    assertEquals(updatedDescription, product.getDescription());
    assertEquals(updatedQuantity, product.getQuantity());
    assertEquals(updatedPrice, product.getPrice());
    assertEquals(updatedWeight, product.getWeight());
  }

  @Test
  public void updateDigitalProductAsProductTest() {
    // setup
    String name = "Netflix Subscription";
    String updateDescription = "One year premium subscription";
    int updatedQuantity = 1;
    double updatedPrice = 22.8;

    // action
    DigitalProduct product = new DigitalProduct(name,
        "One month premium subscription", 2, 2.9);
    product.update(updateDescription, updatedQuantity, updatedPrice);

    // assertions
    assertTrue(product instanceof Product);
    assertEquals(name, product.getName());
    assertEquals(updateDescription, product.getDescription());
    assertEquals(updatedQuantity, product.getQuantity());
    assertEquals(updatedPrice, product.getPrice());
  }
}
