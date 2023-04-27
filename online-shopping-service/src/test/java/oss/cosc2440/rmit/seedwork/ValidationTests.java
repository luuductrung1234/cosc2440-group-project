package oss.cosc2440.rmit.seedwork;

/**
* @author Group 8
*/

import org.junit.jupiter.api.Test;
import oss.cosc2440.rmit.model.CreateProductModel;
import oss.cosc2440.rmit.model.UpdateProductModel;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTests {
  @Test
  public void validateCreateProductModelWithNoPriceShouldFail() throws NoSuchFieldException {
    ValidationResult validationResult = Helpers.validate("price", null, CreateProductModel.class);

    assertFalse(validationResult.isValid());
    assertEquals("Given price must not be null", validationResult.getErrorMessage());
  }

  @Test
  public void validateCreateProductModelWithInvalidPriceShouldFail() throws NoSuchFieldException {
    ValidationResult validationResult = Helpers.validate("price", -2, CreateProductModel.class);

    assertFalse(validationResult.isValid());
    assertEquals("Given price must be greater than 0", validationResult.getErrorMessage());
  }

  @Test
  public void validateUpdateProductModelWithNoPriceShouldSuccess() throws NoSuchFieldException {
    ValidationResult validationResult = Helpers.validate("price", null, UpdateProductModel.class);

    assertTrue(validationResult.isValid());
  }

  @Test
  public void validateUpdateProductModelWithInvalidPriceShouldSuccess() throws NoSuchFieldException {
    ValidationResult validationResult = Helpers.validate("price", 0, UpdateProductModel.class);

    assertFalse(validationResult.isValid());
    assertEquals("Given price must be greater than 0", validationResult.getErrorMessage());
  }
}
