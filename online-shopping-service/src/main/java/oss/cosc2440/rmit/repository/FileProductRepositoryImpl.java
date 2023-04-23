package oss.cosc2440.rmit.repository;

import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.seedwork.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileProductRepositoryImpl extends BaseFileRepository implements ProductRepository {
  public FileProductRepositoryImpl(String pathToDataFile) {
    super(pathToDataFile);
  }

  @Override
  public List<Product> listAll() {
    try {
      return this.read(Product.class);
    } catch (IOException e) {
      Logger.printError(this.getClass().getName(), "listAll", e);
      return new ArrayList<>();
    }
  }

  @Override
  public Optional<Product> findById(UUID id) {
    return listAll().stream().filter(p -> p.getId().equals(id)).findFirst();
  }

  @Override
  public Optional<Product> findByName(String name) {
    return listAll().stream().filter(p -> p.getName().equals(name)).findFirst();
  }

  @Override
  public boolean add(Product product) {
    List<Product> products = listAll();
    boolean exist = products.stream().anyMatch(line -> line.getName().equals(product.getName()));
    if (exist)
      return false;
    products.add(product);
    try {
      this.write(products);
      return true;
    } catch (IOException e) {
      Logger.printError(this.getClass().getName(), "add", e);
      return false;
    }
  }

  @Override
  public boolean update(Product product) {
    List<Product> products = listAll();
    try {
      if (!products.removeIf(p -> p.getId().equals(product.getId())))
        return false;
      products.add(product);
      this.write(products);
      return true;
    } catch (IOException e) {
      Logger.printError(this.getClass().getName(), "update", e);
      return false;
    }
  }

  @Override
  public boolean delete(UUID id) {
    List<Product> products = listAll();
    try {
      if (!products.removeIf(p -> p.getId().equals(id)))
        return false;
      this.write(products);
      return true;
    } catch (IOException e) {
      Logger.printError(this.getClass().getName(), "delete", e);
    }
    return false;
  }
}
