package oss.cosc2440.rmit.repository;

import oss.cosc2440.rmit.domain.Coupon;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileCouponRepositoryImpl extends BaseFileRepository implements CouponRepository {
  public FileCouponRepositoryImpl(String pathToDataFile) {
    super(pathToDataFile);
  }

  @Override
  public List<Coupon> listAll() {
    return null;
  }

  @Override
  public Optional<Coupon> findById(UUID id) {
    return Optional.empty();
  }
}
