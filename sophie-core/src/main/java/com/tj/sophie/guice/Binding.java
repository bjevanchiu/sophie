package com.tj.sophie.guice;

import java.lang.annotation.*;

/**
 * Created by mbp on 6/3/15.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Binding {
    Class<?> from();

    Class<?> to();
}
