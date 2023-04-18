package oss.cosc2440.rmit.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import oss.cosc2440.rmit.service.ShoppingCart;

public class FileCartRepository {
    private final String dataDirectory;
    private final String FILE_CART = "carts.txt";
    private final String FILE_CART_ITEMS = "cartItems.txt";

    public FileCartRepository(String dataDirectory){
        this.dataDirectory = dataDirectory;
    }
    
}