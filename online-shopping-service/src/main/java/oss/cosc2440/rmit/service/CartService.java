package oss.cosc2440.rmit.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Luu Duc Trung - S3951127
 * Manage a collection of shopping carts (sorted by total weight, by default)
 */
public class CartService {
  private final List<ShoppingCart> shoppingCarts;

  public CartService() {
    shoppingCarts = new ArrayList<>();
  }

  public CartService(StorageFactory storageFactory, ProductService productService) {
    shoppingCarts = storageFactory.createCartStorage(() -> new ShoppingCart(productService));
  }

  public List<ShoppingCart> listAll() {
    return shoppingCarts.stream().sorted(Comparator.comparingDouble(ShoppingCart::totalWeight).reversed()).collect(Collectors.toList());
  }

  public void add(ShoppingCart shoppingCart) {
    shoppingCarts.add(shoppingCart);
  }
}
