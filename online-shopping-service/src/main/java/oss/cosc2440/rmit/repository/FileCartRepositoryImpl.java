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
    try {
      return read(ShoppingCart.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Optional<ShoppingCart> findById(UUID id) {
    try {
      List<ShoppingCart> carts = read(ShoppingCart.class);
      return carts.stream().filter(cart -> cart.getId().equals(id)).findFirst();
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public boolean add(ShoppingCart cart) {
    try {
      List<ShoppingCart> carts = read(ShoppingCart.class);
      carts.add(cart);
      write(carts);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean update(ShoppingCart cart) {
    try {
      List<ShoppingCart> carts = read(ShoppingCart.class);
      for (int i = 0; i < carts.size(); i++) {
        if (carts.get(i).getId().equals(cart.getId())) {
          carts.set(i, cart);
          write(carts);
          return true;
        }
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean delete(UUID id) {
    try {
      List<ShoppingCart> carts = read(ShoppingCart.class);
      boolean result = carts.removeIf(cart -> cart.getId().equals(id));
      if (result) {
        write(carts);
      }
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
