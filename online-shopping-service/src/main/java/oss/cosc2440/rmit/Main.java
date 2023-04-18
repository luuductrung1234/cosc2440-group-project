package oss.cosc2440.rmit;

import oss.cosc2440.rmit.repository.*;
import oss.cosc2440.rmit.seedwork.Constants;
import oss.cosc2440.rmit.service.*;

import java.util.Objects;

/**
 * @author Luu Duc Trung - S3951127
 */
public class Main {
  public static void main(String[] args) {
    ProductRepository productRepository = new FileProductRepositoryImpl(
        Objects.requireNonNull(Main.class.getClassLoader().getResource(Constants.PRODUCT_FILE_NAME)).getPath());

    CartRepository cartRepository = new FileCartRepositoryImpl(
        Objects.requireNonNull(Main.class.getClassLoader().getResource(Constants.CART_FILE_NAME)).getPath());

    CouponRepository couponRepository = new FileCouponRepositoryImpl(
        Objects.requireNonNull(Main.class.getClassLoader().getResource(Constants.COUPON_FILE_NAME)).getPath());

    ProductService productService = new ProductService(productRepository, couponRepository);
    CartService cartService = new CartService(cartRepository, productService);
    MenuService menuService = new MenuService(productService, cartService);
    menuService.welcomeScreen();
  }
}