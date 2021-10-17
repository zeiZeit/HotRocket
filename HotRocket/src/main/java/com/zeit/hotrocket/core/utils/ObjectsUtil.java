package com.zeit.hotrocket.core.utils;

import java.util.Arrays;


public class ObjectsUtil {

    public static boolean equals(Object obj, Object obj2) {
        if (obj == obj2 || (obj != null && obj.equals(obj2))) {
            return true;
        }
        return false;
    }


    public static int hashCode(Object... objArr) {
        return Arrays.hashCode(objArr);
    }
}
