package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.DigitalProduct;
import oss.cosc2440.rmit.domain.PhysicalProduct;
import oss.cosc2440.rmit.domain.Product;

import java.util.*;
import java.util.function.Supplier;

public class DefaultStorageFactory implements StorageFactory {
  private final List<Product> seedProducts = new ArrayList<>() { /* seed products */
    {
      add(new PhysicalProduct("Code Complete", "the best programming book", 2, 49.9, 1.2));
      add(new PhysicalProduct("SamSung Monitor 23inch", "good for your work and your eyes", 4, 80.5, 2.0));
      add(new DigitalProduct("Clean Code", "a book from uncle Bob", 3, 35.7));
      add(new DigitalProduct("Netflix Subscription", "Netflix 1 month subscription for all movies", 3, 2.99));
      add(new PhysicalProduct("Table Lamp", "tiny lamp for you cozy bed room", 2, 9.2, 3.5));
    }
  };

  @Override
  public List<Product> createProductStorage() {
    return seedProducts;
  }

  @Override
  public Map<UUID, List<Product>> createReservedProductStorage() {
    return new HashMap<>();
  }

  @Override
  public List<ShoppingCart> createCartStorage() {
    return new ArrayList<>();
  }

  @Override
  public List<ShoppingCart> createCartStorage(Supplier<ShoppingCart> cartSupplier) {
    return new ArrayList<>();
  }
}
