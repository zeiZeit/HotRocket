package com.zeit.hotrocket.rocket.core.utils;

import java.util.Arrays;
import java.util.HashSet;


public class SetUtil {

    public static <T> HashSet<T> toHashSet(T... tArr) {
        return new HashSet<>(Arrays.asList(tArr));
    }
}
