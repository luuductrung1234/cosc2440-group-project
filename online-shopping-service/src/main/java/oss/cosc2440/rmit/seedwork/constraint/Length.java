package oss.cosc2440.rmit.seedwork.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Luu Duc Trung - S3951127
 * Apply limited length validation
 * for array-like data structure (String/SequenceChar, List, array)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Length {
    public int max() default 255;
    public int min() default 0;
    public String message() default "Given input must have valid length between 0 and 255 characters.";
}
