package com.zeit.hotrocket.core.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class ListUtil {
    public static <T> ArrayList<T> toArrayList(T... tArr) {
        return new ArrayList<>(Arrays.asList(tArr));
    }
}
