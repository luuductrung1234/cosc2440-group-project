package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.domain.ShoppingCart;
import oss.cosc2440.rmit.repository.CartRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    return cartRepository.listAll().stream()
        .sorted(Comparator.comparingDouble(ShoppingCart::totalWeight))
        .collect(Collectors.toList());
  }

  public void syncProductInfo(Product product) {
    List<ShoppingCart> carts = listAll().stream().filter(c -> !c.isPurchased()).collect(Collectors.toList());
    carts.forEach(c -> {
      c.syncProductInfo(product);
      cartRepository.update(c);
    });
  }

  public void submit(ShoppingCart cart) {
    cartRepository.add(cart);
  }

  public void printReceipt(ShoppingCart cart, boolean printToFile) {
    if (printToFile) {
      printToFile(cart);
    } else {
      print(cart);
    }

    cart.purchase();
    cartRepository.update(cart);
  }

  public void print(ShoppingCart cart) {
    // TODO: print to console
  }

  public void printToFile(ShoppingCart cart) {
    // TODO: print to file
  }
}
