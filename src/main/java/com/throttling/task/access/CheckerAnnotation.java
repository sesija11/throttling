package com.throttling.task.access;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckerAnnotation {
    String value() default "";
}
