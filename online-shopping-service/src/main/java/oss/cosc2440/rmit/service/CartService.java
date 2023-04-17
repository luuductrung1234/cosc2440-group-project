package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.ShoppingCart;
import oss.cosc2440.rmit.repository.CartRepository;

import java.util.List;

/**
 * @author Luu Duc Trung - S3951127
 * Manage a collection of shopping carts (sorted by total weight, by default)
 */
public class CartService {
  private final CartRepository cartRepository;

  public CartService(CartRepository cartRepository) {
    this.cartRepository = cartRepository;
  }

  public List<ShoppingCart> listAll() {
    return List.of();
  }

  public void add(ShoppingCart shoppingCart) {
  }
}
