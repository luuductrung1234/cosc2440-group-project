package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.model.CreateProductModel;
import oss.cosc2440.rmit.model.SearchProductParameters;
import oss.cosc2440.rmit.model.UpdateProductModel;
import oss.cosc2440.rmit.repository.CouponRepository;
import oss.cosc2440.rmit.repository.ProductRepository;

import java.util.List;

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
    return List.of();
  }

  public boolean addProduct(CreateProductModel model) {
    return false;
  }

  public boolean updateProduct(UpdateProductModel model) {
    return false;
  }
}
