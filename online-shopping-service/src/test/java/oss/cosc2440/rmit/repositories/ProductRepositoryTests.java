package oss.cosc2440.rmit.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import oss.cosc2440.rmit.domain.DigitalProduct;
import oss.cosc2440.rmit.domain.PhysicalProduct;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.domain.TaxType;
import oss.cosc2440.rmit.repository.FileProductRepositoryImpl;

public class ProductRepositoryTests {
    private FileProductRepositoryImpl productRepository;

    // create temporary directory before each test
    @BeforeEach
    void setUp(@TempDir File tempDir) {
        File dataFile = new File(tempDir, "products.txt");
        String pathToDataFile = dataFile.getAbsolutePath();
        productRepository = new FileProductRepositoryImpl(pathToDataFile);
    }

    @Test
    public void testListAllProduct() throws IOException{
        Product product1 = new DigitalProduct(UUID.randomUUID(), "Product 1", "Description 1", 10, 9.99, TaxType.NORMAL_TAX, true);
        Product product2 = new PhysicalProduct(UUID.randomUUID(), "Product 2", "Description 2", 5, 19.99, TaxType.LUXURY_TAX, 1.5, false);
        Product product3 = new PhysicalProduct(UUID.randomUUID(), "Product 3", "Description 3", 3, 29.99, TaxType.TAX_FREE, 2.0, true);

        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);

        List<Product> products = productRepository.listAll();
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
        assertTrue(products.contains(product3));
    }

    @Test
    public void testSearchById() throws IOException{
        UUID id = UUID.randomUUID();
        Product product1 = new DigitalProduct(id, "Product 1", "Description 1", 10, 9.99, TaxType.NORMAL_TAX, true);

        productRepository.add(product1);
        Optional<Product> result = productRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(product1, result.get());
    }

    @Test
    public void testAddProduct() throws IOException{
        Product product1 = new DigitalProduct(UUID.randomUUID(), "Product 1", "Description 1", 10, 9.99, TaxType.NORMAL_TAX, true);
        Product product2 = new PhysicalProduct(UUID.randomUUID(), "Product 2", "Description 2", 5, 19.99, TaxType.LUXURY_TAX, 1.5, false);

        boolean result1 = productRepository.add(product1);
        boolean result2 = productRepository.add(product2);

        // add duplicate
        boolean result3 = productRepository.add(product1);
        boolean result4 = productRepository.add(product2);

        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
        assertFalse(result4);
    }

    @Test
    public void testUpdateProduct() throws IOException{
        UUID id = UUID.randomUUID();
        String name = "Product 1";
        Product product1 = new DigitalProduct(id, name, "Description 1", 10, 9.99, TaxType.NORMAL_TAX, true);
        Product newProduct1 = new DigitalProduct(id, name, "Not Description 1", 100, 99.99, TaxType.LUXURY_TAX, true);

        boolean added = productRepository.add(product1);
        assertTrue(added);

        boolean updated = productRepository.update(newProduct1);
        assertTrue(updated);

        List<Product> products = productRepository.listAll();
        assertEquals(products.get(0).getDescription(), newProduct1.getDescription());
        assertEquals(products.get(0).getPrice(), newProduct1.getPrice());
        assertEquals(products.get(0).getQuantity(), newProduct1.getQuantity());
        assertEquals(products.get(0).getTaxType(), newProduct1.getTaxType());
    }

    @Test
    public void testDeleteProduct() throws IOException{
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Product product1 = new DigitalProduct(id1, "Product 1", "Description 1", 10, 9.99, TaxType.NORMAL_TAX, true);
        Product product2 = new PhysicalProduct(id2, "Product 2", "Description 2", 5, 19.99, TaxType.LUXURY_TAX, 1.5, false);
        
        productRepository.add(product1);
        productRepository.add(product2);

        List<Product> products = productRepository.listAll();
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));

        productRepository.delete(id1);
        productRepository.delete(id2);
        
        List<Product> newProducts = productRepository.listAll();
        assertFalse(newProducts.contains(product1));
        assertFalse(newProducts.contains(product2));
    }
}
