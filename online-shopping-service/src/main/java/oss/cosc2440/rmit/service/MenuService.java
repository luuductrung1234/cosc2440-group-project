package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.*;
import oss.cosc2440.rmit.model.CreateProductModel;
import oss.cosc2440.rmit.model.SearchProductParameters;
import oss.cosc2440.rmit.model.UpdateProductModel;
import oss.cosc2440.rmit.seedwork.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Luu Duc Trung - S3951127
 * <p>
 * Acknowledgement:
 * - WhiteFang34, "How to print color in console using System.out.println?", Stackoverflow, https://stackoverflow.com/a/5762502
 */
public class MenuService {
  private final Scanner scanner = new Scanner(System.in);
  private final ProductService productService;
  private final CartService cartService;
  private ShoppingCart currentCart;

  public MenuService(ProductService productService, CartService cartService) {
    this.productService = productService;
    this.cartService = cartService;
    this.currentCart = new ShoppingCart();
  }

  /**
   * this method intentionally have infinite loop. Users are always go back to main screen (home screen)
   */
  @SuppressWarnings("InfiniteLoopStatement")
  public void homeScreen() {
    while (true) {
      banner("home");
      Logger.printInfo("Welcome back! Enjoy the Online Shopping System...");
      List<ActionOption<Runnable>> commonOptions = new ArrayList<>() {{
        add(new ActionOption<>("show current cart", () -> showCartScreen()));
        add(new ActionOption<>("list products", () -> listProductsScreen()));
        add(new ActionOption<>("list carts", () -> listCartsScreen()));
        add(new ActionOption<>("exit", () -> exitScreen()));
      }};
      Helpers.requestSelectAction(scanner, "Your choice [0-7]: ", commonOptions);
    }
  }

  public void showCartScreen() {
    // TODO: implement showCartScreen
  }

  public void listCartsScreen() {
    // TODO: implement listCartsScreen
  }

  public void cartDetailScreen(UUID cartId) {
    // TODO: implement cartDetailScreen
  }

  public void listProductsScreen() {
    AtomicReference<SearchProductParameters> searchParameters = new AtomicReference<>(new SearchProductParameters());
    AtomicBoolean goBack = new AtomicBoolean(false);
    do {
      banner("products");
      List<Product> products = productService.listAll(searchParameters.get());
      System.out.printf("search by \n\t " +
              "name: %-20s \n\t " +
              "type: %-20s \n\t " +
              "tax: %-20s\n\n",
          Helpers.isNullOrEmpty(searchParameters.get().getName()) ? "n/a" : searchParameters.get().getName(),
          searchParameters.get().getType() == null ? "n/a" : searchParameters.get().getType(),
          searchParameters.get().getTaxType() == null ? "n/a" : searchParameters.get().getTaxType());

      System.out.printf("%-7s %-35s %-10s %-20s %-45s %10s\n", "No.", "name", "quantity", "price", "description", "weight");
      System.out.println("-".repeat(140));
      if (products.isEmpty())
        Logger.printInfo("No product found...");
      for (int productNo = 0; productNo < products.size(); productNo++) {
        Product product = products.get(productNo);
        String weight = "-";

        if (product.getType().equals(ProductType.PHYSICAL))
          weight = String.format("%.2f", product.getWeight());

        System.out.printf("%-7s %-35s %-10s %-20s %-45s %10s\n", productNo, product, product.getQuantity(),
            Helpers.toString(product.getPrice(), "USD", true), product.getDescription(), weight);
      }

      List<ActionOption<Runnable>> actionOptions = new ArrayList<>() {{
        add(new ActionOption<>("search", () -> {
          try {
            SearchProductParameters parameters = new SearchProductParameters();

            Helpers.requestStringInput(scanner, "Search by name: ", "name", parameters);
            Helpers.requestSelectValue(scanner, "Filter by type: ", Constants.PRODUCT_TYPE_OPTIONS, "type", parameters);
            Helpers.requestSelectValue(scanner, "Filter by tax: ", Constants.TAX_TYPE_OPTIONS, "taxType", parameters);
            Helpers.requestDoubleInput(scanner, "Filter from price: ", "fromPrice", parameters);
            Helpers.requestDoubleInput(scanner, "Filter to price: ", "toPrice", parameters);
            Helpers.requestSelectValue(scanner, "Sort by: ", Constants.PRODUCT_SORT_OPTIONS, "sortedBy", parameters);

            searchParameters.set(parameters);
          } catch (RuntimeException e) {
            Logger.printError(this.getClass().getName(), "productScreen", e);
          }
        }));
        add(new ActionOption<>("clear search", () -> searchParameters.set(new SearchProductParameters())));
        add(new ActionOption<>("view detail", () -> {
          int productNo = Helpers.requestIntInput(scanner, "Enter product No. to view detail: ", (value) -> {
            if (value < 0 || value >= products.size()) {
              return ValidationResult.inValidInstance("Given product No. is out of index.");
            }
            return ValidationResult.validInstance();
          });
          productDetailScreen(products.get(productNo).getId());
        }));
        add(new ActionOption<>("create product", () -> createProductScreen()));
        add(new ActionOption<>("edit product", () -> {
          int productNo = Helpers.requestIntInput(scanner, "Enter product No. to edit: ", (value) -> {
            if (value < 0 || value >= products.size()) {
              return ValidationResult.inValidInstance("Given product No. is out of index.");
            }
            return ValidationResult.validInstance();
          });
          editProductScreen(products.get(productNo));
        }));
      }};

      addCommonActions(actionOptions, goBack);
      Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
    } while (!goBack.get());
  }

  public void productDetailScreen(UUID productId) {
    AtomicBoolean goBack = new AtomicBoolean(false);
    do {
      banner("product detail");
      Optional<Product> productOpt = productService.findById(productId);
      if (productOpt.isEmpty())
        throw new IllegalStateException("Product with id: " + productId + " not found!");
      Optional<Coupon> couponOpt = productService.findCoupon(productId);

      Product product = productOpt.get();
      System.out.printf("%-16s: %-10s \n", "Id", product.getId());
      System.out.printf("%-16s: %-10s \n", "Name", product);
      System.out.printf("%-16s: %-10d \n", "Quantity", product.getQuantity());
      System.out.printf("%-16s: %-10s \n", "Tax", product.getTaxType());
      System.out.printf("%-16s: %-10s \n", "Price", Helpers.toString(product.getPrice(), "USD", true));
      if (product.getType().equals(ProductType.PHYSICAL))
        System.out.printf("%-16s: %-10.2f \n", "Weight", product.getWeight());
      else
        System.out.printf("%-16s: %-10s \n", "Weight", "n/a");
      System.out.printf("%-16s: %-10s \n", "Can use as gift", product.canUseAsGift() ? "yes" : "no");

      if (couponOpt.isPresent()) {
        Coupon coupon = couponOpt.get();
        System.out.println("\nCoupon");
        System.out.printf("\t%-12s: %-10s \n", "Code", coupon.getCode());
        System.out.printf("\t%-12s: -%s \n", "Value", coupon.getType().equals(CouponType.PRICE)
            ? Helpers.toString(coupon.getValue(), "USD", true)
            : coupon.getValue() + "%");
      }

      List<ActionOption<Runnable>> actionOptions = new ArrayList<>() {{
        add(new ActionOption<>("edit product", () -> editProductScreen(product)));
        add(new ActionOption<>("add to cart", () -> {
          // TODO: add to cart
          Logger.printSuccess("Add item to cart successfully!");
          goBack.set(true);
        }));
        add(new ActionOption<>("go back", () -> goBack.set(true)));
      }};

      Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
    } while (!goBack.get());
  }

  public void createProductScreen() {
    try {
      banner("create product");
      CreateProductModel model = new CreateProductModel();
      Helpers.requestStringInput(scanner, "Enter product name: ", "name", model);
      Helpers.requestStringInput(scanner, "Enter product description: ", "description", model);
      Helpers.requestDoubleInput(scanner, "Enter product price: ", "price", model);
      Helpers.requestIntInput(scanner, "Enter product quantity: ", "quantity", model);

      Helpers.requestSelectValue(scanner, "Select product type: ", Constants.PRODUCT_TYPE_OPTIONS, "type", model);

      if (model.getType().equals(ProductType.PHYSICAL)) {
        Helpers.requestDoubleInput(scanner, "Enter product weight: ", "weight", model);
      } else {
        model.setWeight(0.0);
      }

      Helpers.requestSelectValue(scanner, "Select tax type: ", Constants.TAX_TYPE_OPTIONS, "taxType", model);

      Helpers.requestBoolInput(scanner, "Can the product be used as gift? [y/n]: ", "canUseAsGift", model);

      if (productService.addProduct(model))
        Logger.printSuccess("Add new product successfully!");
      else
        Logger.printDanger("Add new product failed!");
    } catch (RuntimeException e) {
      Logger.printError(this.getClass().getName(), "createProductScreen", e);
    }
  }

  public void editProductScreen(Product product) {
    try {
      banner("edit product");
      UpdateProductModel model = new UpdateProductModel();

      // not support edit product's id
      model.setId(product.getId());

      Logger.printInfo("Editing product: %s", product.getName());

      Logger.printInfo(String.format("Old name: %s", product.getName()));
      String name = Helpers.requestInput(scanner, "Enter product name: ",
          value -> Helpers.isNullOrEmpty(value) ? null : value,
          value -> productService.isExisted(value) ? ValidationResult.inValidInstance("Product name already exist") : ValidationResult.validInstance());
      if (Helpers.isNullOrEmpty(name))
        model.setName(product.getName());
      else
        model.setName(name);

      Logger.printInfo(String.format("Old description: %s", product.getDescription()));
      Helpers.requestStringInput(scanner, "Enter product description: ", "description", model);
      if (Helpers.isNullOrEmpty(model.getDescription()))
        model.setDescription(product.getDescription());

      Logger.printInfo(String.format("Old price: %s", Helpers.toString(product.getPrice(), "USD", false)));
      Helpers.requestDoubleInput(scanner, "Enter product price: ", "price", model);
      if (model.getPrice() == null)
        model.setPrice(product.getPrice());

      Logger.printInfo(String.format("Old quantity: %d", product.getQuantity()));
      Helpers.requestIntInput(scanner, "Enter product quantity: ", "quantity", model);
      if (model.getQuantity() == null)
        model.setQuantity(product.getQuantity());

      Logger.printInfo(String.format("Old tax type : %s", product.getTaxType()));
      Helpers.requestSelectValue(scanner, "Select tax type: ", Constants.TAX_TYPE_OPTIONS, "taxType", model);
      if (model.getTaxType() == null) {
        model.setTaxType(product.getTaxType());
      }

      Logger.printInfo(String.format("Can be used as gift: %s", product.canUseAsGift()));
      model.setCanUseAsGift(product.canUseAsGift());
      Helpers.requestBoolInput(scanner, "Can the product be used as gift? [y/n]: ", "canUseAsGift", model);

      if (product.getType().equals(ProductType.PHYSICAL)) {
        Logger.printInfo(String.format("Old weight: %.2f", product.getWeight()));
        Helpers.requestDoubleInput(scanner, "Enter product weight: ", "weight", model);
        if (model.getWeight() == null)
          model.setWeight(product.getWeight());
      }

      if (productService.updateProduct(model))
        Logger.printSuccess("Update product successfully!");
      else
        Logger.printDanger("Update product failed!");
    } catch (RuntimeException e) {
      Logger.printError(this.getClass().getName(), "editProductScreen", e);
    }
  }

  public void welcomeScreen() {
    banner("welcome", "", "*");
    System.out.println("*\tCOSC2440 Group Project");
    System.out.println("*\tONLINE SHOPPING SYSTEM");
    System.out.println("*\tInstructor: Mr. Tri Dang");
    System.out.println("*\tAuthors:");
    System.out.println("*\ts3951127, Luu Duc Trung");
    System.out.println("*\ts3938007, Pham Hoang Long");
    System.out.println("*\ts3891941, Yunjae Kim");
    System.out.println("*\ts3915034, Do Phan Nhat Anh");
    System.out.println();
    Boolean answer = Helpers.requestBooleanInput(scanner, "Do you want to continue? [y/n]: ");
    if (answer)
      homeScreen();
    else
      exitScreen();
  }

  public void exitScreen() {
    System.out.println();
    Logger.printInfo("Goodbye! See you again.");
    System.exit(0);
  }

  private void addCommonActions(List<ActionOption<Runnable>> actionOptions, AtomicBoolean goBack) {
    actionOptions.addAll(new ArrayList<>() {{
      add(new ActionOption<>("go back", () -> goBack.set(true)));
      add(new ActionOption<>("exit", () -> exitScreen()));
    }});
  }

  private void banner(String title) {
    banner(title, 10, "", "");
  }

  private void banner(String title, String topText, String bottomText) {
    banner(title, 10, topText, bottomText);
  }

  private void banner(String title, int padding, String topText, String bottomText) {
    String body = "*" + " ".repeat(padding) + title.toUpperCase() + " ".repeat(padding) + "*";
    String border = "*".repeat(body.length());
    System.out.println(topText);
    System.out.println(border);
    System.out.println(body);
    System.out.println(border);
    System.out.println(bottomText);
  }

}
