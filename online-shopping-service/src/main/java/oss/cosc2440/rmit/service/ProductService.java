package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.DigitalProduct;
import oss.cosc2440.rmit.domain.PhysicalProduct;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.domain.ProductType;
import oss.cosc2440.rmit.model.CreateProductModel;
import oss.cosc2440.rmit.model.SearchProductParameters;
import oss.cosc2440.rmit.model.UpdateProductModel;
import oss.cosc2440.rmit.seedwork.Helpers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Luu Duc Trung - S3951127
 * Management a collection of products
 */
public class ProductService {
  private final List<Product> products;
  private final Map<UUID, List<Product>> reservedProducts;

  public ProductService(StorageFactory storageFactory) {
    this.products = storageFactory.createProductStorage();
    this.reservedProducts =storageFactory.createReservedProductStorage();
  }

  public List<Product> listAll(SearchProductParameters parameters) {
    Stream<Product> stream = products.stream();

    if(!Helpers.isNullOrEmpty(parameters.getName()))
      stream = stream.filter(p -> p.getName().toLowerCase().contains(parameters.getName().toLowerCase()));

    if(parameters.getType() != null)
      stream = stream.filter(p -> p.getType().equals(parameters.getType()));

    return stream.collect(Collectors.toList());
  }

  public Collection<Product> listAllByNamesAndType(Collection<String> names, ProductType type) {
    return products.stream().filter(p -> names.contains(p.getName())
        && p.getType().equals(type)).collect(Collectors.toList());
  }

  public Collection<Product> listAllByNames(Collection<String> names) {
    return products.stream().filter(p -> names.contains(p.getName())).collect(Collectors.toList());
  }

  public Optional<Product> findByName(String name) {
    return products.stream().filter(p -> p.getName().equals(name)).findFirst();
  }

  public Optional<Product> findReservedProductByName(UUID cartId, String name) {
    if(!reservedProducts.containsKey(cartId))
      reservedProducts.put(cartId, new ArrayList<>());
    return reservedProducts.get(cartId).stream()
        .filter(p -> p.getName().equals(name)).findFirst();
  }

  public Optional<Product> reserveProduct(String name, UUID cartId) {
    Optional<Product> productOpt = this.findByName(name);
    if(productOpt.isEmpty())
      return Optional.empty();

    Product product = productOpt.get();
    if (product.getQuantity() == 0)
      return Optional.empty();

    Product splitProduct = product.splitOne();

    if(!reservedProducts.containsKey(cartId))
      reservedProducts.put(cartId, new ArrayList<>());

    List<Product> productsInCart = reservedProducts.get(cartId);
    productsInCart.add(splitProduct);

    return productOpt;
  }

  public Optional<Product> rollbackProduct(String name, UUID cartId) {
    Optional<Product> productOpt = this.findByName(name);
    if(productOpt.isEmpty())
      return Optional.empty();

    Product product = productOpt.get();
    product.increaseQuantity();

    List<Product> productsInCart = reservedProducts.get(cartId);
    productsInCart.removeIf(p -> p.getName().equals(name));

    return productOpt;
  }

  public boolean addProduct(CreateProductModel model) {
    Optional<Product> existedProductOpt = this.findByName(model.getName());
    if (existedProductOpt.isPresent())
      return false;

    if (model.getType() == ProductType.PHYSICAL) {
      PhysicalProduct physicalProduct = new PhysicalProduct(model.getName(),
          model.getDescription(), model.getQuantity(), model.getPrice(), model.getWeight());
      products.add(physicalProduct);
      return true;
    }

    DigitalProduct digitalProduct = new DigitalProduct(model.getName(),
        model.getDescription(), model.getQuantity(), model.getPrice());
    products.add(digitalProduct);
    return true;
  }

  public boolean updateProduct(UpdateProductModel model) {
    Optional<Product> existedProductOpt = this.findByName(model.getName());
    Collection<Product> reservedProducts = this.listReservedProductByName(model.getName());
    if (existedProductOpt.isEmpty())
      return false;

    Product product = existedProductOpt.get();

    if (product.getType() == ProductType.PHYSICAL) {
      PhysicalProduct physicalProduct = (PhysicalProduct) product;
      physicalProduct.update(model.getDescription(), model.getQuantity(), model.getPrice(), model.getWeight());
      // sync changes
      reservedProducts.stream()
          .map(p -> (PhysicalProduct)p)
          .forEach(p -> p.update(model.getDescription(), model.getPrice(), model.getWeight()));
      return true;
    }

    DigitalProduct digitalProduct = (DigitalProduct) product;
    digitalProduct.update(model.getDescription(), model.getQuantity(), model.getPrice());
    // sync changes
    reservedProducts.stream()
        .map(p -> (DigitalProduct)p)
        .forEach(p -> p.update(model.getDescription(), model.getPrice()));
    return true;
  }

  private Collection<Product> listReservedProductByName(String name) {
    return reservedProducts.values().stream().flatMap(List::stream)
        .filter(p -> p.getName().equals(name))
        .collect(Collectors.toList());
  }
}
