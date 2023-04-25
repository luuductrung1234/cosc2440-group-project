package oss.cosc2440.rmit;

import oss.cosc2440.rmit.seedwork.Constants;
import oss.cosc2440.rmit.seedwork.Logger;
import oss.cosc2440.rmit.service.*;

import java.util.Objects;

/**
 * @author Luu Duc Trung - S3951127
 */
public class Main {
  public static void main(String[] args) {
    ClassLoader loader = Main.class.getClassLoader();

    if (loader.getResource(Constants.PRODUCT_FILE_NAME) == null) {
      Logger.printWarning("Not found data file %s", Constants.PRODUCT_FILE_NAME);
      return;
    }

    if (loader.getResource(Constants.CART_FILE_NAME) == null) {
      Logger.printWarning("Not found data file %s", Constants.CART_FILE_NAME);
      return;
    }

    ProductService productService = new ProductService(
        Objects.requireNonNull(loader.getResource(Constants.PRODUCT_FILE_NAME)).getPath());

    CartService cartService = new CartService(
        Objects.requireNonNull(loader.getResource(Constants.CART_FILE_NAME)).getPath());

    MenuService menuService = new MenuService(productService, cartService);
    menuService.welcomeScreen();
  }
}