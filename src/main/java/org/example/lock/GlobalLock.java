package org.example.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GlobalLock {

    String DEFAULT_LOCK_NAME = "";

    String DEFAULT_LOCKER_SERVICE = "";

    String lockerName() default DEFAULT_LOCK_NAME;

    String serviceRef() default DEFAULT_LOCKER_SERVICE;

    int order();
}
