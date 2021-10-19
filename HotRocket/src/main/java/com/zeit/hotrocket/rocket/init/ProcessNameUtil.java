package com.zeit.hotrocket.rocket.init;

import android.app.ActivityManager;

import java.io.FileInputStream;
import java.io.IOException;

public class ProcessNameUtil {
    private static String CURRENT_PROCESS_NAME;

    public static String getCurrentProcessName() {
        if (CURRENT_PROCESS_NAME!=null&&!CURRENT_PROCESS_NAME.equals("")){
            return CURRENT_PROCESS_NAME;
        }
        FileInputStream in = null;
        try {
            String fn = "/proc/self/cmdline";
            in = new FileInputStream(fn);
            byte[] buffer = new byte[256];
            int len = 0;
            int b;
            while ((b = in.read()) > 0 && len < buffer.length) {
                buffer[len++] = (byte) b;
            }
            if (len > 0) {
                String s = new String(buffer, 0, len, "UTF-8");
                CURRENT_PROCESS_NAME = s;
                return s;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}
