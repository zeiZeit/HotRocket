package com.zeit.hotrocket.rocket.init;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;


public class ProcessNameUtil {
    private static String CURRENT_PROCESS_NAME;
    private static String PROCESS_NAME;

    public static String currentProcessName() {
        if (!TextUtils.isEmpty(CURRENT_PROCESS_NAME)) {
            return CURRENT_PROCESS_NAME;
        }
        String str = null;
        try {
            str = ActivityThread.currentActivityThread().getProcessName();
        } catch (Throwable th) {

        }
        if (TextUtils.isEmpty(str)) {
            str = currentProcessNameHighApi();
        }
        if (TextUtils.isEmpty(str)) {
            try {
                int myPid = Process.myPid();
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) ActivityThread.currentApplication().getSystemService(EnvConsts.ACTIVITY_MANAGER_SRVNAME)).getRunningAppProcesses()) {
                    if (runningAppProcessInfo.pid == myPid) {
                        String a = PluginUtils.m28820a(runningAppProcessInfo.processName);
                        CURRENT_PROCESS_NAME = a;
                        return a;
                    }
                }
            } catch (Throwable th2) {
                ThrowableExtension.printStackTrace(th2);
            }
        }
        if (TextUtils.isEmpty(str)) {
            str = getProcessName();
        }
        String a2 = PluginUtils.m28820a(str);
        CURRENT_PROCESS_NAME = a2;
        return a2;
    }

    private static String currentProcessNameHighApi() {
        if (Build.VERSION.SDK_INT <= 17) {
            return null;
        }
        try {
            return ActivityThread.currentProcessName();
        } catch (Throwable th) {
            ThrowableExtension.printStackTrace(th);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x004a  */
    @Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static String getProcessName() {
        /*
            java.lang.String r0 = com.xunmeng.pinduoduo.app.ProcessNameUtil.PROCESS_NAME
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x000b
            java.lang.String r0 = com.xunmeng.pinduoduo.app.ProcessNameUtil.PROCESS_NAME
            return r0
        L_0x000b:
            r0 = 0
            java.util.Locale r1 = java.util.Locale.getDefault()
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            int r4 = android.os.Process.myPid()
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r2[r3] = r4
            java.lang.String r3 = "/proc/%d/cmdline"
            java.lang.String r1 = com.xunmeng.pinduoduo.p614b.IllegalArgumentCrashHandler.m34781i(r1, r3, r2)
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ all -> 0x0042 }
            java.io.FileReader r3 = new java.io.FileReader     // Catch:{ all -> 0x0042 }
            r3.<init>(r1)     // Catch:{ all -> 0x0042 }
            r2.<init>(r3)     // Catch:{ all -> 0x0042 }
            java.lang.String r0 = r2.readLine()     // Catch:{ all -> 0x0040 }
            java.lang.String r0 = r0.trim()     // Catch:{ all -> 0x0040 }
            java.lang.String r0 = r0.intern()     // Catch:{ all -> 0x0040 }
            com.xunmeng.pinduoduo.app.ProcessNameUtil.PROCESS_NAME = r0     // Catch:{ all -> 0x0040 }
            r2.close()     // Catch:{ all -> 0x004e }
            goto L_0x0052
        L_0x0040:
            r0 = move-exception
            goto L_0x0045
        L_0x0042:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L_0x0045:
            com.google.devtools.build.android.desugar.runtime.ThrowableExtension.printStackTrace(r0)     // Catch:{ all -> 0x0055 }
            if (r2 == 0) goto L_0x0052
            r2.close()
            goto L_0x0052
        L_0x004e:
            r0 = move-exception
            com.google.devtools.build.android.desugar.runtime.ThrowableExtension.printStackTrace(r0)
        L_0x0052:
            java.lang.String r0 = com.xunmeng.pinduoduo.app.ProcessNameUtil.PROCESS_NAME
            return r0
        L_0x0055:
            r0 = move-exception
            if (r2 == 0) goto L_0x0060
            r2.close()     // Catch:{ all -> 0x005c }
            goto L_0x0060
        L_0x005c:
            r1 = move-exception
            com.google.devtools.build.android.desugar.runtime.ThrowableExtension.printStackTrace(r1)
        L_0x0060:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xunmeng.pinduoduo.app.ProcessNameUtil.getProcessName():java.lang.String");
    }
}
