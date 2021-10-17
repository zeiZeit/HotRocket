package com.zeit.hotrocket.logger.ilogger;


public interface ILogger {

    void v(String str, String str2);

    void v(String str, String str2, Object... objArr);

    void v(String str, Throwable th);

    void v(String str, String str2, Throwable th);

    void d(String str, String str2);

    void d(String str, String str2, Object... objArr);

    void d(String str, Throwable th);

    void d(String str, String str2, Throwable th);

    void i(String str, String str2);

    void i(String str, String str2, Object... objArr);

    void i(String str, Throwable th);

    void i(String str, String str2, Throwable th);

    void w(String str, String str2);

    void w(String str, String str2, Object... objArr);

    void w(String str, Throwable th);

    void w(String str, String str2, Throwable th);

    void e(String str, String str2);

    void e(String str, String str2, Object... objArr);

    void e(String str, Throwable th);

    void e(String str, String str2, Throwable th);
}
