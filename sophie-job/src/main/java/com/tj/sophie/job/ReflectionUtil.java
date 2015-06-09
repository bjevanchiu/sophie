package com.tj.sophie.job;

import java.lang.annotation.Annotation;

/**
 * Created by mbp on 6/4/15.
 */
public class ReflectionUtil {

    public static <T extends Annotation> T findAnnotation(Class<T> annotationType, Class<?> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation item : annotations) {
            if (annotationType.equals(item.annotationType())) {
                return (T) item;
            }
        }
        return null;
    }
}
