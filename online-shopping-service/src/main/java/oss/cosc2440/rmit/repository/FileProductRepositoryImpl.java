package oss.cosc2440.rmit.repository;

import oss.cosc2440.rmit.domain.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileProductRepositoryImpl extends BaseFileRepository implements ProductRepository{
  public FileProductRepositoryImpl(String pathToDataFile) {
    super(pathToDataFile);
  }

  @Override
  public List<Product> listAll() {
    return null;
  }

  @Override
  public Optional<Product> findById(UUID id) {
    return Optional.empty();
  }

  @Override
  public boolean add(Product product) {
    return false;
  }

  @Override
  public boolean update(Product product) {
    return false;
  }

  @Override
  public boolean delete(UUID id) {
    return false;
  }
}
