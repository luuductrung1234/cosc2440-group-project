package oss.cosc2440.rmit.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import oss.cosc2440.rmit.domain.DigitalProduct;
import oss.cosc2440.rmit.domain.PhysicalProduct;
import oss.cosc2440.rmit.domain.Product;

public class FileProductRepository {
    private final String dataDirectory;
    private final String FILE_PRODUCT = "products.txt";

    public FileProductRepository(String dataDirectory){
        this.dataDirectory = dataDirectory;
    }

    public List<Product> readProducts() throws IOException{
        Path path = Paths.get(this.dataDirectory + File.separator + this.FILE_PRODUCT);
        if (!Files.isReadable(path)) return new ArrayList<>();

        List<Product> products = Files.lines(path)
            .map(line -> {
                String[] fields = line.split(",");
                String name = fields[0];
                boolean isDigital = fields[1].equals("digital");
                String description = fields[2];
                int quantity = Integer.parseInt(fields[3]);
                double price = Double.parseDouble(fields[4]);
                double weight = Double.parseDouble(fields[5]);
                boolean isGiftable = fields[6].equals("gift-able");
                int taxRate = Integer.parseInt(fields[7]);
                if (isDigital) {
                    //return new DigitalProduct(name, description, quantity, price, isGiftable, taxRate);
                    return new DigitalProduct(name, description, quantity, price);
                } else {
                    //return new PhysicalProduct(name, description, quantity, price, weight, isGiftable, taxRate);
                    return new PhysicalProduct(name, description, quantity, price, weight);
                }
            })
            .collect(Collectors.toList());
        return products;
    }

    public void writeProducts(List<Product> products) throws IOException{
        Path path = Paths.get(this.dataDirectory + File.separator + this.FILE_PRODUCT);
        String content = products.stream()
            .map(p -> {
                String type = p instanceof DigitalProduct ? "digital" : "physical";
                double weight = p instanceof DigitalProduct ? 0.0 : ((PhysicalProduct)p).getWeight();
                String giftAble = "gift-able";
                //String giftAble = p.isGiftable() ? "gift-able" : "non-gift-able"; // implement later
                //return p.getName() + "," + type + "," + p.getDescription() + "," +
                   //p.getQuantity() + "," + p.getPrice() + "," + weight + "," + giftAble + "," + p.getTaxRate();
                return p.getName() + "," + type + "," + p.getDescription() + "," +
                   p.getQuantity() + "," + p.getPrice() + "," + weight + "," + giftAble;
            })
            .collect(Collectors.joining("\n"));
        Files.write(path, content.getBytes());
    }

    public boolean addProduct(Product p){
        List<Product> products;
        try{
            products = this.readProducts();
        } catch (IOException e){
            return false;
        }

        boolean exist = products.stream().anyMatch(line -> line.getName().equals(p.getName()));
        if (exist){
            return false;
        }
        products.add(p);

        try{
            this.writeProducts(products);
            return true;
        } catch(IOException e){
            return false;
        }
    }

    public boolean removeProduct(Product p){
        List<Product> products;
        try{
            products = this.readProducts();
        } catch (IOException e){
            return false;
        }
        try{
            if (products.removeIf(a -> a.getName().equals(p.getName())));{
                this.writeProducts(products);
                return true;
            }
        }catch (IOException e){
            return false;
        }
    }

    public boolean deleteProduct(Product p){
        List<Product> products;
        try{
            products = this.readProducts();
        } catch (IOException e){
            return false;
        }

        boolean exist = products.stream().anyMatch(line -> line.getName().equals(p.getName()));
        if (!exist) return false;
        products.removeIf(a -> a.getName().equals(p.getName()));
        products.add(p);

        try{
            this.writeProducts(products);
            return true;
        } catch(IOException e){
            return false;
        }
    }
}