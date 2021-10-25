package com.zeit.hotrocket.logger;

import android.os.Build;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CrashHandlerLogger {

    public static String m34780h(String str, Object... objArr) {
        if (str == null) {
            return "";
        }
        try {
            return String.format(str, objArr);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static <E> Iterator<E> m34824V(List<E> list) {
        if (list != null) {
            return list.iterator();
        }
        Logger.e("CrashHandler","List.iterator() throw npe, due to list is null");

        return new ArrayList().iterator();
    }

    public static boolean m34820R(String str, Object obj) {
        if (str == null) {
            Logger.e("CrashHandler","Object.equals(Object str) throw npe");
            return false;
        } else if (obj == null) {
            return false;
        } else {
            return str.equals(obj);
        }
    }

    public static long m34829c(long[] jArr, int i) {
        if (jArr != null && i >= 0 && i < jArr.length) {
            return jArr[i];
        }
        if (jArr == null) {
            Logger.e("CrashHandler","long[i] throw NullPointerException");
            return 0;
        }
        Logger.e("CrashHandler","long[i] throw IndexOutOfBoundsException");
        return 0;
    }

    public static Object mapPut(Map map, Object obj, Object obj2) {
        if (map == null) {
            Logger.e("CrashHandler", "Map.put() throw NullPointerException due to map is null");
            return null;
        }
        try {
            return map.put(obj, obj2);
        } catch (Exception e) {
            Logger.e("CrashHandler", "Map.put() throw NullPointerException due to" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Object mapGet(Map map, Object obj) {
        if (map == null) {
            Logger.e("CrashHandler", "Map.put() throw NullPointerException due to map is null");            return null;
        }
        try {
            return map.get(obj);
        } catch (NullPointerException e) {
            Logger.e("CrashHandler", "Map.get() throw NullPointerException due to key/value is null");
            e.printStackTrace();
            return null;
        } catch (StackOverflowError e2) {
            Logger.e("CrashHandler", "Map.get() throw StackOverflowError");
            e2.printStackTrace();
            return null;
        }
    }

}
