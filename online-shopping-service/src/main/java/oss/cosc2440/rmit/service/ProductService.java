package oss.cosc2440.rmit.service;

/**
* @author Group 8
*/

import oss.cosc2440.rmit.domain.Coupon;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.model.*;
import oss.cosc2440.rmit.seedwork.Deserializer;
import oss.cosc2440.rmit.seedwork.Helpers;
import oss.cosc2440.rmit.seedwork.Logger;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manage collections of products and coupons
 */
public class ProductService {

  private final List<Product> products;
  private final List<Coupon> coupons;

  // Constructor
  public ProductService(String pathToFile) {
    products = new ArrayList<>();
    coupons = new ArrayList<>();
    try {
      products.addAll(Deserializer.read(pathToFile, Product.class));
      coupons.addAll(Deserializer.read(pathToFile, Coupon.class));
    } catch (IOException e) {
      Logger.printWarning("Fail to load data from file");
    }
  }

  public List<Product> listAllProducts(SearchProductParameters parameters) {
    Stream<Product> stream = products.stream();
    if (!Helpers.isNullOrEmpty(parameters.getName()))
      stream = stream.filter(p -> p.getName().toUpperCase().contains(parameters.getName().toUpperCase()));
    if (parameters.getType() != null)
      stream = stream.filter(p -> p.getType().equals(parameters.getType()));
    if (parameters.getTaxType() != null)
      stream = stream.filter(p -> p.getTaxType().equals(parameters.getTaxType()));
    if (parameters.getFromPrice() != null)
      stream = stream.filter(p -> p.getPrice() >= parameters.getFromPrice());
    if (parameters.getToPrice() != null)
      stream = stream.filter(p -> p.getPrice() <= parameters.getToPrice());
    if (parameters.getSortedBy() != null) {
      switch (parameters.getSortedBy()) {
        case PriceAscending:
          stream = stream.sorted(Comparator.comparingDouble(Product::getPrice));
          break;
        case PriceDescending:
          stream = stream.sorted(Comparator.comparingDouble(Product::getPrice).reversed());
          break;
        case NameDescending:
          stream = stream.sorted(Comparator.comparing(Product::getName).reversed());
          break;
        case NameAscending:
          stream = stream.sorted(Comparator.comparing(Product::getName));
          break;
      }
    } else {
      stream = stream.sorted(Comparator.comparing(Product::getName));
    }
    return stream.collect(Collectors.toList());
  }

  public List<Coupon> listAllCoupons(SearchCouponParameter parameters) {
    Stream<Coupon> stream = coupons.stream();
    if (!Helpers.isNullOrEmpty(parameters.getCode()))
      stream = stream.filter(p -> p.getCode().toUpperCase().contains(parameters.getCode().toUpperCase()));
    if (parameters.getType() != null)
      stream = stream.filter(p -> p.getType().equals(parameters.getType()));
    return stream.collect(Collectors.toList());
  }

  public Optional<Product> findProduct(UUID productId) {
    return products.stream().filter(p -> p.getId().equals(productId)).findFirst();
  }

  public List<Coupon> findCoupon(UUID productId) {
    return coupons.stream().filter(c -> c.getTargetProduct().equals(productId)).collect(Collectors.toList());
  }

  public Optional<Coupon> findCoupon(String code) {
    return coupons.stream().filter(c -> c.getCode().equalsIgnoreCase(code)).findFirst();
  }

  public boolean couponExist(String code) {
    return coupons.stream().anyMatch(c -> c.getCode().equalsIgnoreCase(code));
  }


  public boolean productExist(String name) {
    return products.stream().anyMatch(p -> p.getName().equals(name));
  }

  public boolean addProduct(CreateProductModel model) {
    if (productExist(model.getName())){
      Logger.printWarning("Product name '%s' already existed.", model.getName());
      return false;
    }

    Product product = new Product(
        model.getName(),
        model.getDescription(),
        model.getQuantity(),
        model.getPrice(),
        model.getWeight(),
        model.getType(),
        model.getTaxType(),
        model.canUseAsGift());
    return products.add(product);
  }

  public boolean updateProduct(UpdateProductModel model) {
    Optional<Product> productOpt = findProduct(model.getId());
    if (productOpt.isEmpty())
      return false;

    Product product = productOpt.get();
    product.update(
        model.getName(),
        model.getDescription(),
        model.getQuantity(),
        model.getPrice(),
        model.getWeight(),
        model.getTaxType(),
        model.canUseAsGift());
    return true;
  }

  public boolean addCoupon(CreateCouponModel model) {
    Coupon coupon = new Coupon(
        "CP" + String.format("%03d", coupons.size() + 1),
        model.getType(),
        model.getPrice(),
        model.getPercent(),
        model.getTargetProduct());
    return coupons.add(coupon);
  }

  public boolean updateCoupon(UpdateCouponModel model) {
    Optional<Coupon> couponOpt = coupons.stream().filter(c -> c.getId().equals(model.getId())).findFirst();
    if (couponOpt.isEmpty())
      return false;

    Coupon coupon = couponOpt.get();
    coupon.update(model.getPrice(), model.getPercent());
    return true;
  }
}
