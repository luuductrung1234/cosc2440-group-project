package oss.cosc2440.rmit.repository;

import oss.cosc2440.rmit.domain.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
  List<Product> listAll();

  Optional<Product> findById(UUID id);

  boolean add(Product product);

  boolean update(Product product);

  boolean delete(UUID id);
}
