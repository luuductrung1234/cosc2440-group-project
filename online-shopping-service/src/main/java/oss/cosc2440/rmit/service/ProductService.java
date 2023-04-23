package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.Coupon;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.model.CreateProductModel;
import oss.cosc2440.rmit.model.SearchProductParameters;
import oss.cosc2440.rmit.model.UpdateProductModel;
import oss.cosc2440.rmit.repository.CouponRepository;
import oss.cosc2440.rmit.repository.ProductRepository;
import oss.cosc2440.rmit.seedwork.Helpers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Luu Duc Trung - S3951127
 * Management a collection of products
 */
public class ProductService {

  private final ProductRepository productRepository;
  private final CouponRepository couponRepository;

  public ProductService(ProductRepository productRepository, CouponRepository couponRepository) {
    this.productRepository = productRepository;
    this.couponRepository = couponRepository;
  }

  public List<Product> listAll(SearchProductParameters parameters) {
    Stream<Product> stream = productRepository.listAll().stream();
    if(!Helpers.isNullOrEmpty(parameters.getName()))
      stream = stream.filter(p -> p.getName().toUpperCase().contains(parameters.getName().toUpperCase()));
    if(parameters.getType() != null)
      stream = stream.filter(p -> p.getType().equals(parameters.getType()));
    if(parameters.getTaxType() != null)
      stream = stream.filter(p -> p.getTaxType().equals(parameters.getTaxType()));
    if(parameters.getFromPrice() != null)
      stream = stream.filter(p -> p.getPrice() >= parameters.getFromPrice());
    if(parameters.getToPrice() != null)
      stream = stream.filter(p -> p.getPrice() <= parameters.getToPrice());
    if(parameters.getSortedBy() != null) {
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

  public Optional<Product> findById(UUID productId) {
    return productRepository.findById(productId);
  }

  public Optional<Coupon> findCoupon(UUID productId) {
    return couponRepository.findByProductId(productId);
  }

  public boolean isExisted(String name) {
    return productRepository.findByName(name).isPresent();
  }

  public boolean addProduct(CreateProductModel model) {
    Product product = new Product(
        model.getName(),
        model.getDescription(),
        model.getQuantity(),
        model.getPrice(),
        model.getWeight(),
        model.getType(),
        model.getTaxType(),
        model.canUseAsGift());
    return productRepository.add(product);
  }

  public boolean updateProduct(UpdateProductModel model) {
    Optional<Product> productOpt = productRepository.findById(model.getId());
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
    productRepository.update(product);

    // sync new product info into non-purchased carts
    return true;
  }
}
