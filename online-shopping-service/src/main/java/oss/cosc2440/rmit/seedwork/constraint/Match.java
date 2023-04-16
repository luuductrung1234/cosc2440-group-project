package oss.cosc2440.rmit.seedwork.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Luu Duc Trung - S3951127
 * Apply regular expression matching for input type String
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Match {
    public String regex() default "";
    public boolean caseSensitive() default true;
    public String message() default "Given input is not matched";
}
