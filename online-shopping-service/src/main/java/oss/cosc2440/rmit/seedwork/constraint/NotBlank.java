package oss.cosc2440.rmit.seedwork.constraint;

/**
* @author Group 8
*/

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Apply blank validation for input type String
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotBlank {
    public String message() default "Given input must not be blank.";
}
