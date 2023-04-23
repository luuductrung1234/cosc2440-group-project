package oss.cosc2440.rmit.repository;

import oss.cosc2440.rmit.domain.Coupon;
import oss.cosc2440.rmit.seedwork.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileCouponRepositoryImpl extends BaseFileRepository implements CouponRepository {
  public FileCouponRepositoryImpl(String pathToDataFile) {
    super(pathToDataFile);
  }

  @Override
  public List<Coupon> listAll() {
    try {
      return this.read(Coupon.class);
    } catch (IOException e) {
      Logger.printError(this.getClass().getName(), "listAll", e);
      return new ArrayList<>();
    }
  }

  @Override
  public Optional<Coupon> findById(UUID id) {
    return listAll().stream().filter(c -> c.getId().equals(id)).findFirst();
  }

  @Override
  public Optional<Coupon> findByCode(String code) {
    return listAll().stream().filter(c -> c.getCode().equals(code)).findFirst();
  }

  @Override
  public Optional<Coupon> findByProductId(UUID productId) {
    return listAll().stream().filter(c -> c.getTargetProduct().equals(productId)).findFirst();
  }
}
