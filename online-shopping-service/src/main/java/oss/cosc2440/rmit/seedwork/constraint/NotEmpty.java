package oss.cosc2440.rmit.seedwork.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Luu Duc Trung - S3951127
 * Apply not-empty validation for List, array, UUID
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotEmpty {
    public String message() default "Given input must not be empty.";
}
