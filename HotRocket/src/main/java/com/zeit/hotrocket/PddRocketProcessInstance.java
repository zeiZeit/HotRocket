package com.zeit.hotrocket;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;


import com.zeit.hotrocket.appinit.annotations.PROCESS;
import com.zeit.hotrocket.core.utils.Constants;

/* renamed from: com.xunmeng.pinduoduo.rocket.e */
public class PddRocketProcessInstance {

    /* renamed from: b */
    private static PROCESS f39850b;

    /* renamed from: c */
    private static volatile boolean f39851c;

    /* renamed from: a */
    public static PROCESS m52502a(Application application, String str) {
        if (f39851c) {
            return f39850b;
        }
        if (TextUtils.isEmpty(str)) {
            f39850b = null;
        } else {
            f39850b = m52503d(application, str);
        }
        f39851c = true;
        return f39850b;
    }

    /* renamed from: d */
    private static PROCESS m52503d(Context context, String str) {
        String replaceFirst = str.replaceFirst(context.getPackageName(), "");
        if (replaceFirst.startsWith(Constants.COLON_SEPARATOR)) {
            replaceFirst = replaceFirst.substring(1);
        }
        if (TextUtils.isEmpty(replaceFirst)) {
            return PROCESS.MAIN;
        }
        PROCESS[] values = PROCESS.values();
        for (PROCESS process : values) {
            if (replaceFirst.equals(process.getName())) {
                return process;
            }
        }
        return null;
    }
}
