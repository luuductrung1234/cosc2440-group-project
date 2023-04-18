package oss.cosc2440.rmit.repository;

import oss.cosc2440.rmit.domain.ShoppingCart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
  List<ShoppingCart> listAll();

  Optional<ShoppingCart> findById(UUID id);

  boolean add(ShoppingCart cart);

  boolean update(ShoppingCart cart);

  boolean delete(UUID id);
}
