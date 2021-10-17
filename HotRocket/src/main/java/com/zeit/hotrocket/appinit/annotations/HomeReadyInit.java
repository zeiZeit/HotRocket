package com.zeit.hotrocket.appinit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface HomeReadyInit {
    String[] dependsOn() default {""};

    String name();

    PROCESS[] process();

    PRIORITY taskPriority() default PRIORITY.UNKNOWN;

    THREAD thread() default THREAD.BACKGROUND;
}
