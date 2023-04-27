package oss.cosc2440.rmit.domain;

/**
* @author Group 8
*/

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class ShoppingCartTests {
    @Test
    public void addItemToCartShouldSuccesss(){
        // Set up
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("Product 1", "Description", 1, 10.0, 0.1, ProductType.PHYSICAL, TaxType.TAX_FREE, true);

        // Action
        boolean added = cart.addItem(product, 1);

        // Assert
        assertTrue(added);
        assertTrue(cart.getItems().size() > 0);
        assertEquals(0, product.getQuantity());
    }

    @Test
    public void addItemToCartButOutOfStockShouldFail(){
        // Set up
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("Product 1", "Description", 1, 10.0, 0.1, ProductType.PHYSICAL, TaxType.TAX_FREE, true);

        // Action
        boolean added = cart.addItem(product, 100);

        // Assert
        assertFalse(added);
    }

    @Test
    public void removeItemFromCartShouldSuccess(){
        // Set up
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("Product 1", "Description", 1, 10.0, 0.1, ProductType.PHYSICAL, TaxType.TAX_FREE, true);

        cart.addItem(product, 1);

        List<CartItem> cartitem = cart.getItems();
        UUID id = cartitem.get(0).getId();
        
        // Assert
        assertTrue(cart.removeItem(id, product, 1));
        assertEquals(1, product.getQuantity());
    }

    @Test
    public void setGiftMessageShouldSuccess(){
        // Set up
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("Product 1", "Description", 1, 10.0, 0.1, ProductType.PHYSICAL, TaxType.TAX_FREE, true);
        
        cart.addItem(product, 1);
        List<CartItem> cartitem = cart.getItems();
        UUID id = cartitem.get(0).getId();

        cart.setGiftMessage(id, "This is a gift");

        // Assert
        assertEquals("This is a gift", cartitem.get(0).getMessage());
    }

    @Test
    public void applyCouponShouldSuccess(){
        // Set up
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product("Product 1", "Description", 1, 10.0, 0.1, ProductType.PHYSICAL, TaxType.TAX_FREE, true);
         
        cart.addItem(product, 1);
        Coupon coupon = new Coupon(UUID.randomUUID(), "ABCD", CouponType.PRICE, 10, product.getId());

        // Assert
        assertTrue(cart.applyCoupon(coupon));
    }
}
