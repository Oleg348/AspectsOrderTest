package org.example.async;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithAsyncResult {

    String DEFAULT_SERVICE = "";

    String serviceRef() default DEFAULT_SERVICE;
}
