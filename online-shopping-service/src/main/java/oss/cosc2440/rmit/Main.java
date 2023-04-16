package oss.cosc2440.rmit;

import oss.cosc2440.rmit.service.*;

/**
 * @author Luu Duc Trung - S3951127
 */
public class Main {
  public static void main(String[] args) {
    StorageFactory storageFactory = new DefaultStorageFactory();

    ProductService productService = new ProductService(storageFactory);
    CartService cartService = new CartService(storageFactory, productService);
    MenuService menuService = new MenuService(productService, cartService);
    menuService.welcomeScreen();
  }
}