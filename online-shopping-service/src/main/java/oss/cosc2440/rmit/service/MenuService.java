package oss.cosc2440.rmit.service;

/**
 * @author Group 8
 * <p>
 * Acknowledgement:
 * - WhiteFang34, "How to print color in console using System.out.println?", Stackoverflow, https://stackoverflow.com/a/5762502
 */

import oss.cosc2440.rmit.domain.*;
import oss.cosc2440.rmit.model.CreateProductModel;
import oss.cosc2440.rmit.model.SearchProductParameters;
import oss.cosc2440.rmit.model.UpdateProductModel;
import oss.cosc2440.rmit.seedwork.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
    AtomicBoolean goBack = new AtomicBoolean(false);
    do {
      banner("current cart");

      System.out.printf("%-15s: %.2f\n", "Weight", currentCart.totalWeight());
      System.out.printf("%-15s: %d\n", "Quantity", currentCart.totalQuantity());

      List<CartItem> itemsAppliedCoupon = currentCart.getItemsAppliedCoupon();
      if (!itemsAppliedCoupon.isEmpty()) {
        System.out.printf("%-15s: %s\n", "Coupon", itemsAppliedCoupon.stream().findFirst().get().getCouponCode());
        System.out.println();
      } else System.out.println();

      System.out.printf("%-7s %-35s %-15s %-20s %-15s %-12s %-20s\n", "No.", "name", "quantity", "price", "weight", "tax", "message");
      System.out.println("-".repeat(130));
      if (currentCart.getItems().isEmpty())
        Logger.printInfo("Cart is empty...");
      for (int itemNo = 0; itemNo < currentCart.getItems().size(); itemNo++) {
        CartItem item = currentCart.getItems().get(itemNo);
        System.out.printf("%-7s %-35s %-15s %-20s %-15s %-12s %-20s\n", itemNo, item.getProductName(),
            item.getQuantity(), Helpers.toString(item.getProductPrice(), "USD", true),
            item.getProductWeight(), item.getTaxType(), Helpers.isNullOrEmpty(item.getMessage()) ? "-" : item.getMessage());
      }

      System.out.println();
      System.out.printf("%-20s: %s\n", "Total Origin Amount", Helpers.toString(currentCart.totalOriginAmount()));
      System.out.printf("%-20s: + %s\n", "Tax", Helpers.toString(currentCart.totalTax()));
      System.out.printf("%-20s: - %s\n", "Discount", Helpers.toString(currentCart.totalDiscount()));
      System.out.printf("%-20s: + %s\n", "Shipping Fee", Helpers.toString(currentCart.shippingFee(), "USD", true));
      System.out.printf("%-20s: %s\n", "Total Amount", Helpers.toString(currentCart.totalAmount()));

      List<ActionOption<Runnable>> actionOptions = new ArrayList<>();

      if (!currentCart.getItems().isEmpty()) {
        actionOptions.add(new ActionOption<>("add item", () -> {
          int itemNo = Helpers.requestIntInput(scanner, "Enter item No. to add: ", (value) -> {
            if (value < 0 || value >= currentCart.getItems().size()) {
              return ValidationResult.inValidInstance("Given item No. is out of index.");
            }
            return ValidationResult.validInstance();
          });

          CartItem item = currentCart.getItems().get(itemNo);
          Product product = productService.findById(item.getProductId()).orElseThrow();

          int quantity = Helpers.requestIntInput(scanner, "Enter quantity to add: ", (value) -> {
            if (value < 0 || value > product.getQuantity()) {
              return ValidationResult.inValidInstance(String.format("Given quantity must >= 0 and <= product quantity (remaining %d items in stock).", product.getQuantity()));
            }
            return ValidationResult.validInstance();
          });

          if (!currentCart.addItem(item.getId(), product, quantity))
            Logger.printWarning("fail to add item to cart!");
          else
            Logger.printSuccess("Add item to cart successfully!");
        }));
        actionOptions.add(new ActionOption<>("remove item", () -> {
          int itemNo = Helpers.requestIntInput(scanner, "Enter item No. to remove: ", (value) -> {
            if (value < 0 || value >= currentCart.getItems().size()) {
              return ValidationResult.inValidInstance("Given item No. is out of index.");
            }
            return ValidationResult.validInstance();
          });

          CartItem item = currentCart.getItems().get(itemNo);
          Product product = productService.findById(item.getProductId()).orElseThrow();

          int quantity = Helpers.requestIntInput(scanner, "Enter quantity to remove: ", (value) -> {
            if (value < 0) {
              return ValidationResult.inValidInstance("Given quantity must >= 0.");
            }
            return ValidationResult.validInstance();
          });

          if (!currentCart.removeItem(item.getId(), product, quantity))
            Logger.printWarning("fail to remove item from cart!");
          else
            Logger.printSuccess("Remove item from cart successfully!");
        }));
        actionOptions.add(new ActionOption<>("apply coupon", () -> {
          String couponCode = Helpers.requestStringInput(scanner, "Enter coupon code: ", (value) -> ValidationResult.validInstance());

          if (Helpers.isNullOrEmpty(couponCode) || !productService.couponExist(couponCode)) {
            Logger.printWarning("Coupon not found!");
            return;
          }

          Coupon coupon = productService.findCoupon(couponCode).orElseThrow();
          if (!currentCart.applyCoupon(coupon))
            Logger.printWarning("fail to apply coupon!");
          else
            Logger.printSuccess("Apply coupon successfully!");
        }));
        actionOptions.add(new ActionOption<>("clear coupon", () -> {
          Boolean isClear = Helpers.requestBooleanInput(scanner, "Do you want to continue? [y/n]: ");
          if (isClear) {
            currentCart.clearAppliedCoupon();
          }
        }));
        actionOptions.add(new ActionOption<>("purchase as gift", () -> {
          int itemNo = Helpers.requestIntInput(scanner, "Enter item No. to set message: ", (value) -> {
            if (value < 0 || value >= currentCart.getItems().size()) {
              return ValidationResult.inValidInstance("Given item No. is out of index.");
            }
            return ValidationResult.validInstance();
          });

          CartItem item = currentCart.getItems().get(itemNo);
          Product product = productService.findById(item.getProductId()).orElseThrow();

          if (!product.canUseAsGift()) {
            Logger.printWarning("This product can not purchase as gift!");
            return;
          }

          String message = Helpers.requestStringInput(scanner, "Enter gift message: ", (value) -> {
            if (Helpers.isNullOrEmpty(value))
              return ValidationResult.inValidInstance("Gift message is not valid!");
            return ValidationResult.validInstance();
          });

          if (!currentCart.setGiftMessage(item.getId(), message))
            Logger.printWarning("fail to set gift message!");
          else
            Logger.printSuccess("Set gift message successfully!");
        }));
        actionOptions.add(new ActionOption<>("submit", () -> {
          Boolean isSave = Helpers.requestBooleanInput(scanner, "Do you want to continue? [y/n]: ");
          if (isSave) {
            cartService.submit(currentCart);
            currentCart = new ShoppingCart();
          }
        }));
      }

      addCommonActions(actionOptions, goBack);
      Helpers.requestSelectAction(scanner, "Your choice [0-" + (actionOptions.size() - 1) + "]: ", actionOptions);
    } while (!goBack.get());
  }

  public void listCartsScreen() {
    AtomicBoolean goBack = new AtomicBoolean(false);
    do {
      banner("carts");
      List<ShoppingCart> carts = cartService.listAll();

      System.out.printf("%-7s %-37s %-20s %-15s %-15s %-20s\n", "No.", "Id", "total amount", "quantity", "weight", "date of purchase");
      System.out.println("-".repeat(120));
      if (carts.isEmpty())
        Logger.printInfo("No cart found...");
      for (int cartNo = 0; cartNo < carts.size(); cartNo++) {
        ShoppingCart cart = carts.get(cartNo);
        System.out.printf("%-7s %-37s %-20s %-15d %-15.2f %-20s\n", cartNo, cart.getId(),
            Helpers.toString(cart.totalAmount()), cart.totalQuantity(),
            cart.totalWeight(), Helpers.toString(cart.getDateOfPurchase()));
      }

      List<ActionOption<Runnable>> actionOptions = new ArrayList<>() {{
        add(new ActionOption<>("print receipt", () -> {
          int cartNo = Helpers.requestIntInput(scanner, "Enter cart No. to print receipt: ", (value) -> {
            if (value < 0 || value >= carts.size()) {
              return ValidationResult.inValidInstance("Given cart No. is out of index.");
            }
            return ValidationResult.validInstance();
          });

          Boolean printToFile = Helpers.requestBooleanInput(scanner, "Do you want to print to file? [y/n]: ");
          cartService.printReceipt(carts.get(cartNo), printToFile);
        }));
      }};

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
      System.out.printf("search by\n" +
              "\tname: %-20s\n" +
              "\ttype: %-20s\n" +
              "\ttax: %-20s\n" +
              "\tfrom: %-20s to: %-20s\n" +
              "\tsort by: %-20s\n\n",
          Helpers.isNullOrEmpty(searchParameters.get().getName()) ? "n/a" : searchParameters.get().getName(),
          searchParameters.get().getType() == null ? "n/a" : searchParameters.get().getType(),
          searchParameters.get().getTaxType() == null ? "n/a" : searchParameters.get().getTaxType(),
          searchParameters.get().getFromPrice() == null ? "n/a" : searchParameters.get().getFromPrice(),
          searchParameters.get().getToPrice() == null ? "n/a" : searchParameters.get().getToPrice(),
          searchParameters.get().getSortedBy() == null ? "n/a" : searchParameters.get().getSortedBy());

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
        add(new ActionOption<>("add to cart", () -> {
          int productNo = Helpers.requestIntInput(scanner, "Enter product No. to add to cart: ", (value) -> {
            if (value < 0 || value >= products.size()) {
              return ValidationResult.inValidInstance("Given product No. is out of index.");
            }
            return ValidationResult.validInstance();
          });
          if (!currentCart.addItem(products.get(productNo), 1))
            Logger.printWarning("Fail to add item to cart!");
          else
            Logger.printSuccess("Add item to cart successfully!");
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
      List<Coupon> coupons = productService.findCoupon(productId);

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
      System.out.printf("%-16s: %-10s \n", "Description", product.getDescription());
      System.out.printf("%-16s: %-10s \n", "Can use as gift", product.canUseAsGift() ? "yes" : "no");

      System.out.println("\nCoupons:");
      if (coupons.isEmpty())
        Logger.printInfo("This product has no coupon...");
      for (int couponNo = 0; couponNo < coupons.size(); couponNo++) {
        Coupon coupon = coupons.get(couponNo);
        System.out.printf("\t[%d] %-6s: %-10s, %-6s: -%-15s\n", couponNo, "Code", coupon.getCode(),
            "Value", coupon.getType().equals(CouponType.PRICE)
                ? Helpers.toString(coupon.getValue(), "USD", true)
                : coupon.getValue() + "%");
      }

      List<ActionOption<Runnable>> actionOptions = new ArrayList<>() {{
        add(new ActionOption<>("edit product", () -> editProductScreen(product)));
        add(new ActionOption<>("add to cart", () -> {
          if (!currentCart.addItem(product, 1))
            Logger.printWarning("Fail to add item to cart!");
          else
            Logger.printSuccess("Add item to cart successfully!");
        }));
      }};

      if (!coupons.isEmpty()) {
        actionOptions.add(new ActionOption<>("use coupon", () -> {
          int couponNo = Helpers.requestIntInput(scanner, "Enter coupon No. to use: ", (value) -> {
            if (value < 0 || value >= coupons.size()) {
              return ValidationResult.inValidInstance("Given coupon No. is out of index.");
            }
            return ValidationResult.validInstance();
          });
          if (!currentCart.applyCoupon(coupons.get(couponNo)))
            Logger.printWarning("Fail to apply coupon! Please add product to cart before apply coupon.");
          else
            Logger.printSuccess("Apply coupon successfully!");
        }));
      }

      actionOptions.add(new ActionOption<>("go back", () -> goBack.set(true)));
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

      Logger.printInfo(String.format("Old product type : %s", product.getType()));
      Helpers.requestSelectValue(scanner, "Select product type: ", Constants.PRODUCT_TYPE_OPTIONS, "type", model);
      if (model.getType() == null) {
        model.setType(product.getType());
      }

      if (model.getType().equals(ProductType.PHYSICAL)) {
        Logger.printInfo(String.format("Old weight: %.2f", product.getWeight()));
        Helpers.requestDoubleInput(scanner, "Enter product weight: ", "weight", model);
        if (model.getWeight() == null)
          model.setWeight(product.getWeight());
      } else {
        model.setWeight(0.0);
      }

      Logger.printInfo(String.format("Can be used as gift: %s", product.canUseAsGift()));
      model.setCanUseAsGift(product.canUseAsGift());
      Helpers.requestBoolInput(scanner, "Can the product be used as gift? [y/n]: ", "canUseAsGift", model);

      if (productService.updateProduct(model)) {
        Logger.printSuccess("Update product successfully!");
        currentCart.syncProductInfo(product);
        cartService.syncProductInfo(product);
      } else
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
