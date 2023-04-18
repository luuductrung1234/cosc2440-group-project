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
  private final ProductService productService;

  public CartService(CartRepository cartRepository, ProductService productService) {
    this.cartRepository = cartRepository;
    this.productService = productService;
  }

  public List<ShoppingCart> listAll() {
    return cartRepository.listAll().stream()
        .sorted(Comparator.comparingDouble(ShoppingCart::totalWeight))
        .collect(Collectors.toList());
  }

  public void syncProductInfo(Product product) {
    // find all non-purchased carts
    // sync product info
  }

  public void submit(ShoppingCart cart) {
    // save cart
  }

  public void printReceipt(ShoppingCart cart, boolean printToFile) {
    if (printToFile) {
      printToFile(cart);
    } else {
      print(cart);
    }
    // set date of purchase
  }

  public void print(ShoppingCart cart) {
    // print to console
  }

  public void printToFile(ShoppingCart cart) {
    // print to file
  }
}
