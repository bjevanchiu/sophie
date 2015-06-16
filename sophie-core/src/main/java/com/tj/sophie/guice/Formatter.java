package com.tj.sophie.guice;

import java.lang.annotation.*;

/**
 * Created by evan.chiu on 2015/6/15.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Formatter {
    String key();
}
