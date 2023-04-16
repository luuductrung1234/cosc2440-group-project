package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public interface StorageFactory {
  /**
   * create a collection of products
   */
  List<Product> createProductStorage();

  /**
   * create a collection of reserved products
   */
  Map<UUID, List<Product>> createReservedProductStorage();

  /**
   * create a collection of shopping carts
   */
  List<ShoppingCart> createCartStorage();

  /**
   * create a collection of shopping carts
   *
   * @param cartSupplier a supplier to define how to create a ShoppingCart
   */
  List<ShoppingCart> createCartStorage(Supplier<ShoppingCart> cartSupplier);
}
