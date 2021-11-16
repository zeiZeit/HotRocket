package com.zeit.hotrocketdemo.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.zeit.hotrocket.appinit.annotations.InitTask;
import com.zeit.hotrocketdemo.dex.Dex2oat;

public class Dex2oatInit implements InitTask {
    private final String SP_KEY_DEX_UPDATE = "DEX_VERSION_CODE";

    @Override
    public void run(Context context) {
        //这里如果之前没有编译过，或者有更新，才编译，否则就不执行以下代码
        if (context==null){
            return;
        }
        int currentCode = getPackageVersionCode(context);
        int storeCode = getInt(getSp(context),SP_KEY_DEX_UPDATE,0);
        if (storeCode>=currentCode){
            return;
        }
        putInt(getSp(context),SP_KEY_DEX_UPDATE,currentCode);
        try {
            String appDir = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), 0).sourceDir;
            if (Build.VERSION.SDK_INT >= 29){
                new Dex2oat().dex2oatByBinder(context);  //binder方式触发全量编译，有效
            }else {
                //在Android 10 以下，在android 8上的小米手机测试 系统会自动触发全量编译，这里作用不大，可做备用。
//                Dex2oat.makeDex2OatV2(appDir,"data/app/base.odex"); //快速编译 runtime.exec()在api>29报错，没有对/data的操作权限
                Dex2oat.makeDex2oat1(appDir,"data/app/com.simuwang.ppw/base.odex");  //全量编译 runtime.exec()在api>29报错，没有对/data的操作权限
            }

            //new Dex2oat().dexOptSpeed(mContext,mContext.getPackageName()); //反射报错
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e2){
            e2.printStackTrace();
        }
    }

    /**
     * 获取AndroidManifest中指定版本号整形值
     *
     * @return 如果没有获取成功(没有对应值, 或者异常)，则返回值为-1
     */
    public static int getPackageVersionCode(Context context) {
        int            verCode = -1;
        PackageManager pm      = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            if(null != pi.versionName) {
                verCode = pi.versionCode;
            }
        } catch(PackageManager.NameNotFoundException e) {

        }
        return verCode;
    }

    private static SharedPreferences sp;

    private static SharedPreferences getSp(Context context) {
        if(sp == null) {
            sp = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
        }
        return sp;
    }

    /**
     * 存int缓存
     */
    public static void putInt(SharedPreferences sp,String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 取int缓存
     */
    public static int getInt(SharedPreferences sp,String key, int defValue) {
        return sp.getInt(key, defValue);
    }


}
