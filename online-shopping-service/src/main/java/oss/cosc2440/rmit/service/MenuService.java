package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.PhysicalProduct;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.domain.ProductType;
import oss.cosc2440.rmit.model.CreateProductModel;
import oss.cosc2440.rmit.model.SearchProductParameters;
import oss.cosc2440.rmit.model.UpdateProductModel;
import oss.cosc2440.rmit.seedwork.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
    this.currentCart = new ShoppingCart(productService);
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
    AtomicBoolean goBack = new AtomicBoolean(false);
    do {
      banner("current shopping cart");
      currentCart.printDetail();

      List<ActionOption<Runnable>> actionOptions = new ArrayList<>();

      if (currentCart.totalQuantity() > 0) {
        actionOptions.add(new ActionOption<>("purchase as gift", () -> {
          int productNo = Helpers.requestIntInput(scanner, "Enter product No. to set message: ", (value) -> {
            if (value < 0 || value >= currentCart.totalQuantity()) {
              return ValidationResult.inValidInstance("Given product No. is out of index.");
            }
            return ValidationResult.validInstance();
          });

          String message = Helpers.requestStringInput(scanner, "Enter gift message: ", (value) -> {
            if (Helpers.isNullOrEmpty(value)) {
              return ValidationResult.inValidInstance("Message must not be empty!");
            }
            return ValidationResult.validInstance();
          });

          String productName = currentCart.getItem(productNo);
          if (currentCart.setItemMessage(productName, message))
            Logger.printSuccess("Set gift message successfully!");
          else
            Logger.printDanger("Set gift message failed!");
        }));

        actionOptions.add(new ActionOption<>("remove item from cart", () -> {
          int productNo = Helpers.requestIntInput(scanner, "Enter product No. to remove from cart: ", (value) -> {
            if (value < 0 || value >= currentCart.totalQuantity()) {
              return ValidationResult.inValidInstance("Given product No. is out of index.");
            }
            return ValidationResult.validInstance();
          });
          String productName = currentCart.getItem(productNo);
          if (currentCart.removeItem(productName))
            Logger.printSuccess("Remove item from cart successfully!");
          else
            Logger.printDanger("Remove item from cart failed!");
        }));

        actionOptions.add(new ActionOption<>("submit cart", () -> {
          cartService.add(currentCart);
          currentCart = new ShoppingCart(productService);
          Logger.printSuccess("Submit cart successfully!");
        }));
      }

      addCommonActions(actionOptions, goBack);
      Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
    } while (!goBack.get());
  }

  public void listProductsScreen() {
    AtomicReference<SearchProductParameters> searchParameters = new AtomicReference<>(new SearchProductParameters());
    AtomicBoolean goBack = new AtomicBoolean(false);
    do {
      banner("products");
      List<Product> products = productService.listAll(searchParameters.get());
      System.out.printf("search by \n\t name: %-20s \n\t category: %-20s\n\n",
          Helpers.isNullOrEmpty(searchParameters.get().getName()) ? "n/a" : searchParameters.get().getName(),
          searchParameters.get().getType() == null ? "n/a" : searchParameters.get().getType());

      System.out.printf("%-7s %-35s %-10s %-20s %-45s %10s\n", "No.", "name", "quantity", "price", "description", "weight");
      System.out.println("-".repeat(140));
      if (products.isEmpty())
        Logger.printInfo("No product found...");
      for (int productNo = 0; productNo < products.size(); productNo++) {
        Product product = products.get(productNo);
        String weight = "-";

        if (product.getType().equals(ProductType.PHYSICAL))
          weight = String.format("%.2f", ((PhysicalProduct) product).getWeight());

        System.out.printf("%-7s %-35s %-10s %-20s %-45s %10s\n", productNo, product, product.getQuantity(),
            Helpers.toString(product.getPrice(), "USD", true), product.getDescription(), weight);
      }

      List<ActionOption<Runnable>> actionOptions = new ArrayList<>() {{
        add(new ActionOption<>("search", () -> {
          try {
            SearchProductParameters parameters = new SearchProductParameters();

            Helpers.requestStringInput(scanner, "Search by name: ", "name", parameters);
            Helpers.requestSelectValue(scanner, "Filter by type: ",
                new ArrayList<>() {
                  {
                    add(new ValueOption<>(ProductType.DIGITAL.toString(), ProductType.DIGITAL));
                  }

                  {
                    add(new ValueOption<>(ProductType.PHYSICAL.toString(), ProductType.PHYSICAL));
                  }
                }, "type", parameters);

            searchParameters.set(parameters);
          } catch (RuntimeException e) {
            Logger.printError(this.getClass().getName(), "productScreen", e);
          }
        }));
        add(new ActionOption<>("clear search", () -> searchParameters.set(new SearchProductParameters())));
        add(new ActionOption<>("add to cart", () -> {
          int productNo = Helpers.requestIntInput(scanner, "Enter product No. to add to cart: ", (value) -> {
            if (value < 0 || value >= products.size()) {
              return ValidationResult.inValidInstance("Given product No. is out of index.");
            }
            return ValidationResult.validInstance();
          });
          if (currentCart.addItem(products.get(productNo).getName()))
            Logger.printSuccess("Add item to cart successfully!");
          else
            Logger.printDanger("Add item to cart failed!");
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

  public void listCartsScreen() {
    AtomicBoolean goBack = new AtomicBoolean(false);
    do {
      banner("shopping carts");
      List<ShoppingCart> carts = cartService.listAll();

      System.out.printf("%-7s %-40s %-10s %-25s %-15s\n", "No.", "id", "quantity", "amount", "weight (sorted)");
      System.out.println("-".repeat(100));
      if (carts.isEmpty())
        Logger.printInfo("No shopping cart found...");
      for (int cartNo = 0; cartNo < carts.size(); cartNo++) {
        ShoppingCart cart = carts.get(cartNo);
        System.out.printf("%-7s %-40s %-10s %-25s %-15s\n", cartNo, cart.getId(), cart.totalQuantity(),
            Helpers.toString(cart.cartAmount(), "USD", true), cart.totalWeight());
      }

      List<ActionOption<Runnable>> actionOptions = new ArrayList<>();
      addCommonActions(actionOptions, goBack);
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

      Helpers.requestSelectValue(scanner, "Select product type: ",
          new ArrayList<>() {
            {
              add(new ValueOption<>(ProductType.DIGITAL.toString(), ProductType.DIGITAL));
            }

            {
              add(new ValueOption<>(ProductType.PHYSICAL.toString(), ProductType.PHYSICAL));
            }
          }, "type", model);

      if (model.getType().equals(ProductType.PHYSICAL)) {
        Helpers.requestDoubleInput(scanner, "Enter product weight: ", "weight", model);
      }

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

      // not support edit product's name
      model.setName(product.getName());
      Logger.printInfo("Editing product: %s", product.getName());

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

      if (product.getType().equals(ProductType.PHYSICAL)) {
        Logger.printInfo(String.format("Old weight: %.2f", ((PhysicalProduct) product).getWeight()));
        Helpers.requestDoubleInput(scanner, "Enter product weight: ", "weight", model);
        if (model.getWeight() == null)
          model.setWeight(((PhysicalProduct) product).getWeight());
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
    System.out.println("*\tCOSC2440 Individual Project");
    System.out.println("*\tONLINE SHOPPING SYSTEM");
    System.out.println("*\tInstructor: Mr. Tri Dang");
    System.out.println("*\tAuthor:");
    System.out.println("*\ts3951127, Luu Duc Trung");
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
