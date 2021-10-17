package com.zeit.hotrocket.appinit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface HomeIdleInit {
    String[] dependsOn() default {""};

    @Deprecated
    String description() default "";

    String name();

    @Deprecated
    int priority() default 32767;

    PROCESS[] process();

    PRIORITY taskPriority() default PRIORITY.UNKNOWN;

    THREAD thread() default THREAD.MAIN;
}
