package oss.cosc2440.rmit.repository;

import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.seedwork.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import oss.cosc2440.rmit.domain.DigitalProduct;
import oss.cosc2440.rmit.domain.PhysicalProduct;
import oss.cosc2440.rmit.domain.TaxType;;

public class FileProductRepositoryImpl extends BaseFileRepository implements ProductRepository{
    public FileProductRepositoryImpl(String pathToDataFile) {
        super(pathToDataFile);
  }

    /**
    * Read a list of product object from data file.
    */
    public List<Product> readProducts() throws IOException{
        Path path = Paths.get(pathToDataFile);
        if (!Files.isReadable(path)) return new ArrayList<>();

        List<Product> products = Files.lines(path)
            .map(line ->{
                String[] fields = line.split(",");
                UUID id = UUID.fromString(fields[0]);
                String name = fields[1];
                boolean isDigital = fields[2].equals("digital");
                String description = fields[3];
                int quantity = Integer.parseInt(fields[4]);
                double price = Double.parseDouble(fields[5]);
                double weight = Double.parseDouble(fields[6]);
                TaxType taxType;
                String t = fields[7];
                switch (t){
                        case "TAX_FREE":
                            taxType = TaxType.TAX_FREE;
                            break;
                        case "NORMAL_TAX":
                            taxType = TaxType.NORMAL_TAX;
                            break;
                        case "LUXURY_TAX":
                            taxType = TaxType.LUXURY_TAX;
                            break;
                        default:
                            taxType = TaxType.TAX_FREE;
                }
                boolean canUseAsGift = fields[8].equals("true");
                if (isDigital) {
                    return new DigitalProduct(id, name, description, quantity, price, taxType, canUseAsGift);
                } else {
                    return new PhysicalProduct(id, name, description, quantity, price, taxType, weight, canUseAsGift);
                }
            })
            .collect(Collectors.toList());
        return products;
    }

    /**
    * Write a list of product object to data file.
    */
    public void writeProducts(List<Product> products) throws IOException{
        Path path = Paths.get(pathToDataFile);
        String content = products.stream()
            .map(p -> {
                String type = p instanceof DigitalProduct ? "digital" : "physical";
                double weight = p instanceof DigitalProduct ? 0.0 : ((PhysicalProduct)p).getWeight();
                TaxType taxType = p.getTaxType();
                boolean canUseAsGift = p.canUseAsGift();
                // id,name,digital/physical,description,quantity,price,weight,taxtType,canUseAsGift(bool)
                return p.getId() + "," + p.getName() + "," + type + "," + p.getDescription() + "," + p.getQuantity()
                             + "," + p.getPrice() + "," + weight + "," + taxType + "," + canUseAsGift;
            })
            .collect(Collectors.joining("\n"));
        Files.write(path, content.getBytes());
    }


  @Override
    public List<Product> listAll() {
        try{
            return this.readProducts();
        }catch (IOException e){
            Logger.printError(this.getClass().getName(), "listAll", e);
            return new ArrayList<>();
        }
    }

  @Override
  public Optional<Product> findById(UUID id) {
      return listAll().stream().filter(p -> p.getId().equals(id)).findFirst();
  }

  @Override
  public boolean add(Product product) {
      List<Product> products = listAll();
      boolean exist = products.stream().anyMatch(line -> line.getName().equals(product.getName()));
      if (exist)
          return false;
      products.add(product);
      try {
          this.writeProducts(products);
          return true;
      } catch (IOException e) {
          Logger.printError(this.getClass().getName(), "add", e);
          return false;
      }
  }

  @Override
  public boolean update(Product product) {
    List<Product> products = listAll();
    try{
        if (products.removeIf(p -> p.getName().equals(product.getName())));{
            products.add(product);
            this.writeProducts(products);
            return true;
        }
    }catch (IOException e){
        Logger.printError(this.getClass().getName(), "update", e);
        return false;
    }
  }

  @Override
  public boolean delete(UUID id) {
    List<Product> products = listAll();
    try {
        if (products.removeIf(p -> p.getId().equals(id))) {
            this.writeProducts(products);
            return true;
        }
    } catch (IOException e) {
        Logger.printError(this.getClass().getName(), "delete", e);
    }
    return false;
  }
}
