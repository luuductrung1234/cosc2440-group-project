package oss.cosc2440.rmit.mock;

import oss.cosc2440.rmit.domain.DigitalProduct;
import oss.cosc2440.rmit.domain.PhysicalProduct;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.service.ShoppingCart;
import oss.cosc2440.rmit.service.StorageFactory;

import java.util.*;
import java.util.function.Supplier;

public class MockedStorageFactory implements StorageFactory {
  private final List<Product> seedProducts = new ArrayList<>() { /* seed products */
    {
      add(new PhysicalProduct("Code Complete", "the best programming book", 2, 49.9, 1.2));
      add(new PhysicalProduct("SamSung Monitor 23inch", "good for your work and your eyes", 4, 80.5, 2.0));
      add(new DigitalProduct("Clean Code", "a book from uncle Bob", 3, 35.7));
      add(new DigitalProduct("Netflix Subscription", "Netflix 1 month subscription for all movies", 3, 2.99));
      add(new PhysicalProduct("Table Lamp", "tiny lamp for you cozy bed room", 2, 9.2, 3.5));
      add(new PhysicalProduct("Apple Pen", "Apple a electric pen", 0, 250, 0.1));
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
    return createCartStorage(null);
  }

  @Override
  public List<ShoppingCart> createCartStorage(Supplier<ShoppingCart> cartSupplier) {
    if(cartSupplier == null)
      return new ArrayList<>();
    // seed carts
    ShoppingCart cart1 = cartSupplier.get();
    cart1.addItem(seedProducts.get(0).getName());
    cart1.addItem(seedProducts.get(1).getName());

    ShoppingCart cart2 = cartSupplier.get();
    cart2.addItem(seedProducts.get(2).getName());
    cart2.addItem(seedProducts.get(3).getName());

    ShoppingCart cart3 = cartSupplier.get();
    cart3.addItem(seedProducts.get(2).getName());
    cart3.addItem(seedProducts.get(3).getName());
    cart3.addItem(seedProducts.get(4).getName());

    return new ArrayList<>() {{
      add(cart1);
      add(cart2);
      add(cart3);
    }};
  }
}
