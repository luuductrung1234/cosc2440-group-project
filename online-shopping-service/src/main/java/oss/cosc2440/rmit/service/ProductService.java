package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.DigitalProduct;
import oss.cosc2440.rmit.domain.PhysicalProduct;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.domain.ProductType;
import oss.cosc2440.rmit.model.CreateProductModel;
import oss.cosc2440.rmit.model.SearchProductParameters;
import oss.cosc2440.rmit.model.UpdateProductModel;
import oss.cosc2440.rmit.repository.CouponRepository;
import oss.cosc2440.rmit.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

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
        Product newProduct;
        if (model.getType() == ProductType.DIGITAL){
            newProduct = new DigitalProduct(UUID.randomUUID(), model.getName(), model.getDescription(),
                        model.getQuantity(), model.getPrice(), model.getTaxType(), model.canUseAsGift());
        }else{
            newProduct = new PhysicalProduct(UUID.randomUUID(), model.getName(), model.getDescription(),
                        model.getQuantity(), model.getPrice(), model.getTaxType(), model.getWeight(), model.canUseAsGift());
        }
        if (productRepository.add(newProduct)){
            return true;
        }
        return false;
    }

    public boolean updateProduct(UpdateProductModel model) {
        // apply the updates 
        List<Product> products = productRepository.listAll();
        Product newProduct;
        if (model.getType() == ProductType.DIGITAL){
            newProduct = new DigitalProduct(model.getName(), model.getDescription(),
                        model.getQuantity(), model.getPrice(), model.getTaxType(), model.canUseAsGift());
        }else{
            newProduct = new PhysicalProduct(model.getName(), model.getDescription(),
                        model.getQuantity(), model.getPrice(), model.getTaxType(), model.getWeight(), model.canUseAsGift());
        }

        // save data
        // sync new product info into non-purchased carts
        boolean updated = false, synced = false;

        updated = productRepository.update(newProduct);
        //synced = method for sync in carts
        
        if (updated && synced){
            return true;
        }
        return false;
    }

    
}
