package oss.cosc2440.rmit;

import oss.cosc2440.rmit.repository.*;
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

    if(loader.getResource(Constants.PRODUCT_FILE_NAME) == null) {
      Logger.printWarning("Not found data file %s", Constants.PRODUCT_FILE_NAME);
      return;
    }

    ProductRepository productRepository = new FileProductRepositoryImpl(
        Objects.requireNonNull(loader.getResource(Constants.PRODUCT_FILE_NAME)).getPath());

    CouponRepository couponRepository = new FileCouponRepositoryImpl(
        Objects.requireNonNull(loader.getResource(Constants.PRODUCT_FILE_NAME)).getPath());

    if(loader.getResource(Constants.CART_FILE_NAME) == null) {
      Logger.printWarning("Not found data file %s", Constants.CART_FILE_NAME);
      return;
    }

    CartRepository cartRepository = new FileCartRepositoryImpl(
        Objects.requireNonNull(loader.getResource(Constants.CART_FILE_NAME)).getPath());


    ProductService productService = new ProductService(productRepository, couponRepository);
    CartService cartService = new CartService(cartRepository);
    MenuService menuService = new MenuService(productService, cartService);
    menuService.welcomeScreen();
  }
}