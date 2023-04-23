package oss.cosc2440.rmit.repositories;

import org.junit.jupiter.api.Test;
import oss.cosc2440.rmit.domain.Coupon;
import oss.cosc2440.rmit.repository.CouponRepository;
import oss.cosc2440.rmit.repository.FileCouponRepositoryImpl;
import oss.cosc2440.rmit.seedwork.Constants;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CouponRepositoryTests {
  @Test
  public void listAllProductShouldSuccess() {
    // Setup
    CouponRepository couponRepository = new FileCouponRepositoryImpl(
        Objects.requireNonNull(ProductRepositoryTests.class.getClassLoader().getResource(Constants.COUPON_FILE_NAME)).getPath());

    // Action
    List<Coupon> coupons = couponRepository.listAll();

    // Assert
    assertNotNull(coupons);
    assertTrue(coupons.size() > 0);
  }
}
