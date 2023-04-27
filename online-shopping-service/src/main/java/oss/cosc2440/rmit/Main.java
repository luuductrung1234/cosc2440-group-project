package oss.cosc2440.rmit;

/**
* @author Group 8
*/

import oss.cosc2440.rmit.seedwork.Constants;
import oss.cosc2440.rmit.seedwork.Helpers;
import oss.cosc2440.rmit.service.CartService;
import oss.cosc2440.rmit.service.MenuService;
import oss.cosc2440.rmit.service.ProductService;


public class Main {
  public static void main(String[] args) {
    ClassLoader loader = Main.class.getClassLoader();

    ProductService productService = new ProductService(Helpers.getPathToFile(loader, Constants.PRODUCT_FILE_NAME));

    CartService cartService = new CartService(Helpers.getPathToFile(loader, Constants.CART_FILE_NAME));

    MenuService menuService = new MenuService(productService, cartService);
    menuService.welcomeScreen();
  }
}