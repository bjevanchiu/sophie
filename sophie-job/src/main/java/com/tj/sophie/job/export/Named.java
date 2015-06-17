package com.tj.sophie.job.export;

import com.tj.sophie.job.model.NamedType;

import java.lang.annotation.*;

/**
 * Created by evanchiu on 15/6/17.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Named {
    String name();
    NamedType type();
}
