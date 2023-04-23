package oss.cosc2440.rmit.seedwork;

import oss.cosc2440.rmit.domain.ProductType;
import oss.cosc2440.rmit.domain.TaxType;
import oss.cosc2440.rmit.model.ProductSort;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Luu Duc Trung - S3951127
 */
public class Constants {
  public static final String PRODUCT_FILE_NAME = "data" + File.separator + "products.txt";
  public static final String CART_FILE_NAME = "data" + File.separator + "carts.txt";
  public static final String COUPON_FILE_NAME = "data" + File.separator + "coupons.txt";

  public static final double BASE_FEE = 0.1;

  public static final int TAX_FREE_AMOUNT = 0;
  public static final int NORMAL_TAX_AMOUNT = 10;
  public static final int LUXURY_TAX_AMOUNT = 20;

  public static final List<ValueOption<ProductType>> PRODUCT_TYPE_OPTIONS = new ArrayList<>() {
    {
      add(new ValueOption<>(ProductType.DIGITAL.toString(), ProductType.DIGITAL));
    }

    {
      add(new ValueOption<>(ProductType.PHYSICAL.toString(), ProductType.PHYSICAL));
    }
  };

  public static final List<ValueOption<TaxType>> TAX_TYPE_OPTIONS = new ArrayList<>() {
    {
      add(new ValueOption<>(TaxType.TAX_FREE.toString(), TaxType.TAX_FREE));
    }

    {
      add(new ValueOption<>(TaxType.NORMAL_TAX.toString(), TaxType.NORMAL_TAX));
    }

    {
      add(new ValueOption<>(TaxType.LUXURY_TAX.toString(), TaxType.LUXURY_TAX));
    }
  };

  public static final List<ValueOption<ProductSort>> PRODUCT_SORT_OPTIONS = new ArrayList<>() {
    {
      add(new ValueOption<>(ProductSort.NameAscending.toString(), ProductSort.NameAscending));
    }

    {
      add(new ValueOption<>(ProductSort.NameDescending.toString(), ProductSort.NameDescending));
    }

    {
      add(new ValueOption<>(ProductSort.PriceAscending.toString(), ProductSort.PriceAscending));
    }

    {
      add(new ValueOption<>(ProductSort.PriceDescending.toString(), ProductSort.PriceDescending));
    }
  };

  /**
   * ANSI color codes
   */
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_BLACK = "\u001B[30m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String ANSI_WHITE = "\u001B[37m";

  public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
  public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
  public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
  public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
  public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
  public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
  public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
  public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
}
