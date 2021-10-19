package com.zeit.hotrocket.rocket;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;


import com.zeit.hotrocket.appinit.annotations.PROCESS;
import com.zeit.hotrocket.rocket.core.utils.Constants;

public class HotRocketProcessInstance {

    private static PROCESS process;

    private static volatile boolean f39851c;

    public static PROCESS getPROCESS(Application application, String fullProcess) {
        if (f39851c) {
            return process;
        }
        if (TextUtils.isEmpty(fullProcess)) {
            process = null;
        } else {
            process = convert2PROCESS(application, fullProcess);
        }
        f39851c = true;
        return process;
    }

    private static PROCESS convert2PROCESS(Context context, String fullProcess) {
        String replaceFirst = fullProcess.replaceFirst(context.getPackageName(), "");
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
