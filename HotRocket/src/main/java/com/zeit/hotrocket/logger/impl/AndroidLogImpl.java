package com.zeit.hotrocket.logger.impl;

import android.util.Log;
import com.zeit.hotrocket.logger.ilogger.ILogger;


public class AndroidLogImpl implements ILoggerImpl {
    @Override
    public ILogger getLogger() {
        return new ILogger() {

            @Override
            public void v(String str, String str2) {
                if (str != null && str2 != null) {
                    Log.v(str, str2);
                }
            }

            @Override
            public void v(String str, String str2, Object... objArr) {
                if (str != null && str2 != null) {
                    Log.v(str, format(str2, objArr));
                }
            }

            @Override
            public void v(String str, Throwable th) {
                if (str != null) {
                    Log.v(str, "", th);
                }
            }

            @Override
            public void v(String str, String str2, Throwable th) {
                if (str != null) {
                    if (str2 == null) {
                        str2 = "";
                    }
                    Log.v(str, str2, th);
                }
            }

            @Override
            public void d(String str, String str2) {
                if (str != null && str2 != null) {
                    Log.d(str, str2);
                }
            }

            @Override
            public void d(String str, String str2, Object... objArr) {
                if (str != null && str2 != null) {
                    Log.d(str, format(str2, objArr));
                }
            }

            @Override
            public void d(String str, Throwable th) {
                if (str != null) {
                    Log.d(str, "", th);
                }
            }

            @Override
            public void d(String str, String str2, Throwable th) {
                if (str != null) {
                    if (str2 == null) {
                        str2 = "";
                    }
                    Log.d(str, str2, th);
                }
            }

            @Override
            public void i(String str, String str2) {
                if (str != null && str2 != null) {
                    Log.i(str, str2);
                }
            }

            @Override
            public void i(String str, String str2, Object... objArr) {
                if (str != null && str2 != null) {
                    Log.i(str, format(str2, objArr));
                }
            }

            @Override
            public void i(String str, Throwable th) {
                if (str != null) {
                    Log.i(str, "", th);
                }
            }

            @Override
            public void i(String str, String str2, Throwable th) {
                if (str != null) {
                    if (str2 == null) {
                        str2 = "";
                    }
                    Log.i(str, str2, th);
                }
            }

            @Override
            public void w(String str, String str2) {
                if (str != null && str2 != null) {
                    Log.w(str, str2);
                }
            }

            @Override
            public void w(String str, String str2, Object... objArr) {
                if (str != null && str2 != null) {
                    Log.w(str, format(str2, objArr));
                }
            }

            @Override
            public void w(String str, Throwable th) {
                if (str != null) {
                    Log.w(str, "", th);
                }
            }

            @Override
            public void w(String str, String str2, Throwable th) {
                if (str != null) {
                    if (str2 == null) {
                        str2 = "";
                    }
                    Log.w(str, str2, th);
                }
            }

            @Override
            public void e(String str, String str2) {
                if (str != null && str2 != null) {
                    Log.e(str, str2);
                }
            }

            @Override
            public void e(String str, String str2, Object... objArr) {
                if (str != null && str2 != null) {
                    Log.e(str, format(str2, objArr));
                }
            }

            @Override
            public void e(String str, Throwable th) {
                if (str != null) {
                    Log.e(str, "", th);
                }
            }

            @Override
            public void e(String str, String str2, Throwable th) {
                if (str != null) {
                    if (str2 == null) {
                        str2 = "";
                    }
                    Log.e(str, str2, th);
                }
            }
        };
    }

    public static String format(String str, Object... objArr) {
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
}
