package oss.cosc2440.rmit.repository;

import oss.cosc2440.rmit.domain.Coupon;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {
  List<Coupon> listAll();

  Optional<Coupon> findById(UUID id);
}
