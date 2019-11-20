package com.twh.common.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
public @interface EnumType {
    String factoryMethod() default "of";

    String valueMethod() default "getValue";
}
