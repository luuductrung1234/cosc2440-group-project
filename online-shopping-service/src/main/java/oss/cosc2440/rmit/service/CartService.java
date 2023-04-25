package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.CartItem;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.domain.ShoppingCart;
import oss.cosc2440.rmit.seedwork.Deserializer;
import oss.cosc2440.rmit.seedwork.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Luu Duc Trung - S3951127
 * Manage a collection of shopping carts (sorted by total weight, by default)
 */
public class CartService {
  private final List<ShoppingCart> carts;

  public CartService(String pathToFile) {
    carts = new ArrayList<>();
    try {
      carts.addAll(Deserializer.read(pathToFile, ShoppingCart.class));
      List<CartItem> cartItems = Deserializer.read(pathToFile, CartItem.class);
      carts.forEach(c -> c.setItems(cartItems.stream().filter(i -> i.getCartId().equals(c.getId())).collect(Collectors.toList())));
    } catch (IOException e) {
      Logger.printWarning("Fail to load data from file");
    }
  }

  public List<ShoppingCart> listAll() {
    return carts.stream()
        .sorted(Comparator.comparingDouble(ShoppingCart::totalWeight))
        .collect(Collectors.toList());
  }

  public void syncProductInfo(Product product) {
    List<ShoppingCart> carts = listAll().stream().filter(c -> !c.isPurchased()).collect(Collectors.toList());
    carts.forEach(c -> c.syncProductInfo(product));
  }

  public void submit(ShoppingCart cart) {
    carts.add(cart);
  }

  public void printReceipt(ShoppingCart cart, boolean printToFile) {
    if (printToFile) {
      printToFile(cart);
    } else {
      print(cart);
    }

    cart.purchase();
  }

  private void print(ShoppingCart cart) {
    // TODO: print to console
  }

  private void printToFile(ShoppingCart cart) {
    // TODO: print to file
  }
}
