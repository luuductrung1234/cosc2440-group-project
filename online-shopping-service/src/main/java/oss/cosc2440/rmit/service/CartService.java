package oss.cosc2440.rmit.service;

import oss.cosc2440.rmit.domain.CartItem;
import oss.cosc2440.rmit.domain.Product;
import oss.cosc2440.rmit.domain.ShoppingCart;
import oss.cosc2440.rmit.seedwork.Deserializer;
import oss.cosc2440.rmit.seedwork.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;

/**
 * @author Luu Duc Trung - S3951127
 * Manage a collection of shopping carts (sorted by total weight, by default)
 */
public class CartService {
    private final List<ShoppingCart> carts;

    public CartService(String pathToFile) {
        carts = new ArrayList<>();
        try {
            carts.addAll(Deserializer.read(pathToFile, ShoppingCart.class));
            List<CartItem> cartItems = Deserializer.read(pathToFile, CartItem.class);
            carts.forEach(c -> c.setItems(cartItems.stream().filter(i -> i.getCartId().equals(c.getId())).collect(Collectors.toList())));
        } catch (IOException e) {
            Logger.printWarning("Fail to load data from file");
        }
    }

    public List<ShoppingCart> listAll() {
        return carts.stream()
                .sorted(Comparator.comparingDouble(ShoppingCart::totalWeight))
                .collect(Collectors.toList());
    }

    public void syncProductInfo(Product product) {
        List<ShoppingCart> carts = listAll().stream().filter(c -> !c.isPurchased()).collect(Collectors.toList());
        carts.forEach(c -> c.syncProductInfo(product));
    }

    public void submit(ShoppingCart cart) {
        carts.add(cart);
    }

    public void printReceipt(ShoppingCart cart, boolean printToFile) {
        if (printToFile) {
            printToFile(cart);
        } else {
            print(cart);
        }

        cart.purchase();
    }

    private void print(ShoppingCart cart) {
        // Print to console
        System.out.println("\n\n======== RECEIPT ========");

        System.out.println(String.format("Date of purchase: %s", cart.getDateOfPurchase()));
        System.out.println("Items:");
        cart.getItems().forEach(item -> {
            System.out.println(String.format("%s - $%.2f x %d", item.getProductName(), item.getProductPrice(), item.getQuantity()));
        });

        System.out.println(String.format("\nSubtotal: $%.2f", cart.totalOriginAmount()));
        System.out.println(String.format("Tax: $%.2f", cart.totalTax()));
        System.out.println(String.format("Shipping fee: $%.2f", cart.shippingFee()));
        System.out.println(String.format("Total Discount: $%.2f", cart.totalDiscount()));
        System.out.println(String.format("Total: $%.2f", cart.totalAmount()));

        System.out.println("\nThank you for shopping with us!");
        System.out.println("=========================");
    }

    private void printToFile(ShoppingCart cart) {
        // Print to file
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter file name to print to:");
        String fileName = scanner.nextLine();

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("======== RECEIPT ========\n\n");

            writer.write(String.format("Date of purchase: %s\n\n", cart.getDateOfPurchase()));

            writer.write("Items:\n");
            cart.getItems().forEach(item -> {
                try {
                    writer.write(String.format("%s - $%.2f x %d\n", item.getProductName(), item.getProductPrice(), item.getQuantity()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            writer.write(String.format("\nSubtotal: $%.2f\n", cart.totalOriginAmount()));
            writer.write(String.format("Tax: $%.2f\n", cart.totalTax()));
            writer.write(String.format("Shipping fee: $%.2f\n", cart.shippingFee()));
            writer.write(String.format("Total Discount: $%.2f\n", cart.totalDiscount()));
            writer.write(String.format("Total: $%.2f\n\n", cart.totalAmount()));

            writer.write("Thank you for shopping with us!\n");
            writer.write("=========================");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
