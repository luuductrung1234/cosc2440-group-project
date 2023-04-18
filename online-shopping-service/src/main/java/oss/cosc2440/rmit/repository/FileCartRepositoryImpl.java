package oss.cosc2440.rmit.repository;

import oss.cosc2440.rmit.domain.ShoppingCart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileCartRepositoryImpl extends BaseFileRepository implements CartRepository {
  public FileCartRepositoryImpl(String pathToDataFile) {
    super(pathToDataFile);
  }

  @Override
  public List<ShoppingCart> listAll() {
    return null;
  }

  @Override
  public Optional<ShoppingCart> findById(UUID id) {
    return Optional.empty();
  }

  @Override
  public boolean add(ShoppingCart cart) {
    return false;
  }

  @Override
  public boolean update(ShoppingCart cart) {
    return false;
  }

  @Override
  public boolean delete(UUID id) {
    return false;
  }
}
