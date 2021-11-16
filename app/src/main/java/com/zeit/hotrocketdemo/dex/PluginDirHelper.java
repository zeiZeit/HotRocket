package com.zeit.hotrocketdemo.dex;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 插件目录结构
 * 基本目录： /data/data/com.HOST.PACKAGE/Plugin
 * 单个插件的基本目录： /data/data/com.HOST.PACKAGE/Plugin
 * source_dir： /data/data/com.HOST.PACKAGE/Plugin/PLUGIN.PKG/apk/apk-1.apk
 * 数据目录： /data/data/com.HOST.PACKAGE/Plugin/PLUGIN.PKG/data/PLUGIN.PKG
 * dex缓存目录： /data/data/com.HOST.PACKAGE/Plugin/PLUGIN.PKG/dalvik-cache/
 * <p>
 */
public class PluginDirHelper {

    //data/data/com.HOST.PACKAGE/Plugin
    private static File sBaseDir = null;

    private static void init(Context context) {
        System.out.println("PluginDirHelper:init");
        if (sBaseDir == null) {
            sBaseDir = new File(context.getCacheDir().getParentFile(), "Plugin");
            enforceDirExists(sBaseDir);
        }
    }

    private static String enforceDirExists(File file) {
        System.out.println("PluginDirHelper:enforceDirExists");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    //data/data/com.HOST.PACKAGE/Plugin/pluginInfoPackageName
    public static String makePluginBaseDir(Context context, String pluginInfoPackageName) {
        System.out.println("PluginDirHelper:makePluginBaseDir");
        init(context);
        return enforceDirExists(new File(sBaseDir, pluginInfoPackageName));
    }

    //data/data/com.HOST.PACKAGE/Plugin
    public static String getBaseDir(Context context) {
        System.out.println("PluginDirHelper:getBaseDir");
        init(context);
        return enforceDirExists(sBaseDir);
    }

    //data/data/com.HOST.PACKAGE/Plugin/pluginInfoPackageName/data/pluginInfoPackageName
    public static String getPluginDataDir(Context context, String pluginInfoPackageName) {
        System.out.println("PluginDirHelper:getPluginDataDir");
        return enforceDirExists(new File(makePluginBaseDir(context, pluginInfoPackageName), "data/" + pluginInfoPackageName));
    }

    //data/data/com.HOST.PACKAGE/Plugin/pluginInfoPackageName/Signature/
    public static String getPluginSignatureDir(Context context, String pluginInfoPackageName) {
        System.out.println("PluginDirHelper:getPluginSignatureDir");
        return enforceDirExists(new File(makePluginBaseDir(context, pluginInfoPackageName), "Signature/"));
    }

    public static String getPluginSignatureFile(Context context, String pluginInfoPackageName, int index) {
        System.out.println("PluginDirHelper:getPluginSignatureFile");
        return new File(getPluginSignatureDir(context, pluginInfoPackageName), String.format("Signature_%s.key", index)).getPath();
    }

    public static List<String> getPluginSignatureFiles(Context context, String pluginInfoPackageName) {
        System.out.println("PluginDirHelper:getPluginSignatureFiles");
        ArrayList<String> files = new ArrayList<String>();
        String dir = getPluginSignatureDir(context, pluginInfoPackageName);
        File d = new File(dir);
        File[] fs = d.listFiles();
        if (fs != null && fs.length > 0) {
            for (File f : fs) {
                files.add(f.getPath());
            }
        }
        return files;
    }

    //data/data/com.HOST.PACKAGE/Plugin/pluginInfoPackageName/apk
    public static String getPluginApkDir(Context context, String pluginInfoPackageName) {
        System.out.println("PluginDirHelper:getPluginApkDir");
        return enforceDirExists(new File(makePluginBaseDir(context, pluginInfoPackageName), "apk"));
    }

    //data/data/com.HOST.PACKAGE/Plugin/pluginInfoPackageName/apk/base-1.apk
    public static String getPluginApkFile(Context context, String pluginInfoPackageName) {
        System.out.println("PluginDirHelper:getPluginApkFile");
        return new File(getPluginApkDir(context, pluginInfoPackageName), "base-1.apk").getPath();
    }

    //data/data/com.HOST.PACKAGE/Plugin/pluginInfoPackageName/apk/base.apk
    public static String getTempSourceFile(Context context, String pluginInfoPackageName) {
        return new File(getPluginApkDir(context, pluginInfoPackageName), "base.apk").getPath();
    }

    //data/data/com.HOST.PACKAGE/Plugin/pluginInfoPackageName/dalvik-cache
    public static String getPluginDalvikCacheDir(Context context, String pluginInfoPackageName) {
        System.out.println("PluginDirHelper:getPluginDalvikCacheDir");
        return enforceDirExists(new File(makePluginBaseDir(context, pluginInfoPackageName), "dalvik-cache"));
    }

    //data/data/com.HOST.PACKAGE/Plugin/pluginInfoPackageName/lib
    public static String getPluginNativeLibraryDir(Context context, String pluginInfoPackageName) {
        System.out.println("PluginDirHelper:getPluginNativeLibraryDir");
        return enforceDirExists(new File(makePluginBaseDir(context, pluginInfoPackageName), "lib"));
    }

    public static String getPluginDalvikCacheFile(Context context, String pluginInfoPackageName) {
        System.out.println("PluginDirHelper:getPluginDalvikCacheFile");
        String dalvikCacheDir = getPluginDalvikCacheDir(context, pluginInfoPackageName);

        String pluginApkFile = getPluginApkFile(context, pluginInfoPackageName);
        String apkName = new File(pluginApkFile).getName();
        String dexName = apkName.replace(File.separator, "@");
        if (dexName.startsWith("@")) {
            dexName = dexName.substring(1);
        }
        return new File(dalvikCacheDir, dexName + "@classes.dex").getPath();
    }

    public static String getContextDataDir(Context context) {
        System.out.println("PluginDirHelper:getContextDataDir");
        String dataDir = new File(Environment.getDataDirectory(), "data/").getPath();
        return new File(dataDir, context.getPackageName()).getPath();
    }


    public static void cleanOptimizedDirectory(String optimizedDirectory) {
        System.out.println("PluginDirHelper:cleanOptimizedDirectory");
        try {
            File dir = new File(optimizedDirectory);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        f.delete();
                    }
                }
            }

            if (dir.exists() && dir.isFile()) {
                dir.delete();
                dir.mkdirs();
            }
        } catch (Throwable e) {
        }
    }

    public static String getSourceFile(Context context) {
        String appDir = null;
        try {
            appDir = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), 0).sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appDir;
    }

    public static String getSourceDir(Context mContext, String pluginPackageName) {
        String appDir = null;
        try {
            appDir = mContext.getPackageManager().getApplicationInfo(
                    mContext.getPackageName(), 0).sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appDir!=null){
            appDir = enforceDirExists(new File(appDir).getParentFile());
        }
        return appDir;
    }
}