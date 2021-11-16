package com.zeit.hotrocketdemo.dex;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ResultReceiver;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Dex2oat {
    private final String PACKAGE_NAME = "com.simuwang.ppw";
    private static final String COMPILE_FILTER_QUICKEN = "quicken";
    private static final String COMPILE_FILTER_SPEED = "speed";
    private static final int MAX_RETRY_COUNT = 3;
    int SHELL_COMMAND_TRANSACTION = ('_'<<24)|('C'<<16)|('M'<<8)|'D';
    private Context mContext;
    private Object mPmObj;
    private IBinder mPmBinder;

    /**
     * 全量编译  在api>=29以上的系统，没有对/data/目录的写入权限
     * @param sourceFilePath
     * @param optimizedFilePath
     */
    public static void makeDex2oat1(String sourceFilePath, String optimizedFilePath ) {
        List<String> commandAndParams = new ArrayList<>();
        commandAndParams.add("dex2oat");
        if (Build.VERSION.SDK_INT >= 24) {
            commandAndParams.add("--runtime-arg");
            commandAndParams.add("-classpath");
            commandAndParams.add("--runtime-arg");
            commandAndParams.add("&");
        }
        commandAndParams.add("--instruction-set=" + Build.CPU_ABI);
        //全量编译
        commandAndParams.add("--compiler-filter=speed");
        //源码路径（apk or dex路径）
        commandAndParams.add("--dex-file=" + sourceFilePath);
        //dex2oat产物路径
        commandAndParams.add("--oat-file=" + optimizedFilePath);
        String[] cmd = commandAndParams.toArray(new String[commandAndParams.size()]);
        //step2 执行命令
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private final String TAG = "Dex2OatHelper";

    /**
     * 快速编译  在api>=29以上的系统，没有对/data/目录的写入权限
     * @param dexFilePath
     * @param oatFilePath
     * @throws IOException
     */
    public static void makeDex2OatV2(String dexFilePath, String oatFilePath) throws IOException {
        final File oatFile = new File(oatFilePath);
        if (!oatFile.exists()) {
            oatFile.getParentFile().mkdirs();
        }

        try {
            final List<String> commandAndParams = new ArrayList<>();
            commandAndParams.add("dex2oat");
            // for 7.1.1, duplicate class fix
            if (Build.VERSION.SDK_INT >= 24) {
                commandAndParams.add("--runtime-arg");
                commandAndParams.add("-classpath");
                commandAndParams.add("--runtime-arg");
                commandAndParams.add("&");
            }
            commandAndParams.add("--dex-file=" + dexFilePath);
            commandAndParams.add("--oat-fd=" + oatFilePath);
            commandAndParams.add("--instruction-set=" + Build.CPU_ABI);
            if (Build.VERSION.SDK_INT > 25) {
                commandAndParams.add("--compiler-filter=quicken");
            } else {
                commandAndParams.add("--compiler-filter=interpret-only");
            }

            final ProcessBuilder pb = new ProcessBuilder(commandAndParams);
            pb.redirectErrorStream(true);
            final Process dex2oatProcess = pb.start();
            StreamConsumer.consumeInputStream(dex2oatProcess.getInputStream());
            StreamConsumer.consumeInputStream(dex2oatProcess.getErrorStream());
            try {
                final int ret = dex2oatProcess.waitFor();
                if (ret != 0) {
                    throw new IOException("dex2oat works unsuccessfully, exit code: " + ret);
                }
            } catch (InterruptedException e) {
                throw new IOException("dex2oat is interrupted, msg: " + e.getMessage(), e);
            }
        } finally {

        }
    }

    private static class StreamConsumer {
        static final Executor STREAM_CONSUMER = Executors.newSingleThreadExecutor();
        static void consumeInputStream(final InputStream is) {
            STREAM_CONSUMER.execute(new Runnable() {
                @Override
                public void run() {
                    if (is == null) {
                        return;
                    }
                    final byte[] buffer = new byte[256];
                    try {
                        while ((is.read(buffer)) > 0) {
                            // To satisfy checkstyle rules.
                        }
                    } catch (IOException ignored) {
                        // Ignored.
                    } finally {
                        try {
                            is.close();
                        } catch (Exception ignored) {
                            // Ignored.
                        }
                    }
                }
            });
        }
    }

    /**
     * 用binder的方式触发全量编译，目前唯一可行的方案
     * @param context
     */
    public  void dex2oatByBinder(Context context){
        mContext = context;
        if (mContext == null || mPmBinder != null) {
            return;
        }
        PackageManager packageManager = mContext.getPackageManager();
        //Field mPmField = safeGetField(packageManager, "mPM");
        Field mPmField =  ReflectUtils.getField(packageManager, "mPM");
        if (mPmField == null) {
            return;
        }

        //mPmObj = safeGetValue(mPmField, packageManager);
        try {
            mPmObj =  ReflectUtils.readField(mPmField,packageManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (!(mPmObj instanceof IInterface)) {
            return;
        }

        IInterface mPmInterface = (IInterface) mPmObj;
        IBinder binder = mPmInterface.asBinder();
        if (binder != null) {
            mPmBinder = binder;
        }
        String packageName = mContext.getPackageName();
        final List<String> commandAndParams = new ArrayList<>();
        commandAndParams.add("compile");
        commandAndParams.add("-m");
        commandAndParams.add("speed");
        commandAndParams.add("-f");
        commandAndParams.add(packageName);
        String[] args = commandAndParams.toArray(new String[commandAndParams.size()]);
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeFileDescriptor(FileDescriptor.in);
        data.writeFileDescriptor(FileDescriptor.out);
        data.writeFileDescriptor(FileDescriptor.err);
        data.writeStringArray(args);
        data.writeStrongBinder(null);
        ResultReceiver resultReceiver = new ResultReceiver(null);
        resultReceiver.writeToParcel(data, 0);
        try {
            mPmBinder.transact(SHELL_COMMAND_TRANSACTION, data, reply, 0);
            reply.readException();
        } catch (Throwable e) {
            //Report info
            e.printStackTrace();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }


    //执行快速编译  反射报错，暂时不用
    public void dexOptQuicken(Context context, String pluginPackageName) {
        PluginDirHelper.makePluginBaseDir(context,pluginPackageName);
        //step1：如果没有初始化则初始化
        maybeInit(context);
        //step2:将apk路径进行注册到PMS
        registerDexModule(pluginPackageName);
        //step3:使用binder触发快速编译
        dexOpt(COMPILE_FILTER_QUICKEN, pluginPackageName);
        //step4:将apk路径反注册到PMS
        unregisterDexModule(pluginPackageName);

    }

    //执行全量编译  反射报错，暂时不用
    public void dexOptSpeed(Context context, String pluginPackageName) {
        //初始化文件管理器
        PluginDirHelper.makePluginBaseDir(context,pluginPackageName);
        //step1：如果没有初始化则初始化
        maybeInit(context);
        //step2:将apk路径进行注册到PMS
        registerDexModule(pluginPackageName);
        //step3:使用binder触发全量编译
        dexOpt(COMPILE_FILTER_SPEED, pluginPackageName);
        //step4:将apk路径反注册到PMS
        unregisterDexModule(pluginPackageName);

    }

    /*
     * Try To Init (Build Base env)
     */
    private void maybeInit(Context context) {
        if (mContext == null){
            mContext = context;
        }
        if (mPmBinder != null) {
            return;
        }

        PackageManager packageManager = mContext.getPackageManager();
        //Field mPmField = safeGetField(packageManager, "mPM");
        Field mPmField =  ReflectUtils.getField(packageManager, "mPM");
        if (mPmField == null) {
            return;
        }

        //mPmObj = safeGetValue(mPmField, packageManager);
        try {
            mPmObj =  ReflectUtils.readField(mPmField,packageManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (!(mPmObj instanceof IInterface)) {
            return;
        }

        IInterface mPmInterface = (IInterface) mPmObj;
        IBinder binder = mPmInterface.asBinder();
        if (binder != null) {
            mPmBinder = binder;
        }

    }


    /*
     * DexOpt (Add Retry Function)
     */
    private void dexOpt(String compileFilter, String pluginPackageName) {
        String tempFilePath = PluginDirHelper.getTempSourceFile(mContext,pluginPackageName);
        String tempCacheDirPath = PluginDirHelper.getPluginDalvikCacheDir(mContext,pluginPackageName);
//        String tempOatDexFilePath = tempCacheDirPath + File.separator + PluginDirHelper.getOatFileName(tempFilePath);
        String tempOatDexFilePath = tempCacheDirPath + File.separator + "base.oat";
        File tempOatDexFile = new File(tempOatDexFilePath);
        for (int retry = 1; retry <= MAX_RETRY_COUNT; retry++) {
            execCmd(buildDexOptArgs(compileFilter));
            if (tempOatDexFile.exists()) {
                break;
            }
        }
    }


    /*
     * Register DexModule(dex path) To PMS
     */

    private void registerDexModule(String pluginPackageName) {
        if (pluginPackageName == null || mContext == null) {
            return;
        }
        String originFilePath = PluginDirHelper.getSourceFile(mContext);
        String tempFilePath = PluginDirHelper.getTempSourceFile(mContext,pluginPackageName);
        FileUtil.safeCopyFile(originFilePath, tempFilePath);
        String loadingPackageName = mContext.getPackageName();
        String loaderIsa = getCurrentInstructionSet();
        notifyDexLoad(loadingPackageName, tempFilePath, loaderIsa);

    }



    /*
     * Register DexModule(dex path) To PMS By Binder
     */

    private void notifyDexLoad(String loadingPackageName, String dexPath, String loaderIsa) {
        if (Build.VERSION.SDK_INT >= 30) {
            //Build.VERSION_CODES.R
            //deal android 11\12
            realNotifyDexLoadForR(loadingPackageName, dexPath, loaderIsa);
        } else {
            //deal android 10
            realNotifyDexLoad(loadingPackageName, dexPath, loaderIsa);
        }

    }


    /*
     * Register DexModule(dex path) To PMS By Binder for R+
            */

    private void realNotifyDexLoadForR(String loadingPackageName, String dexPath, String loaderIsa) {
        if (mPmObj == null || loadingPackageName == null || dexPath == null || loaderIsa == null) {
            return;
        }
        Map<String, String> maps = Collections.singletonMap(dexPath, "PCL[]");
        try {
            ReflectUtils.invokeMethod(mPmObj, "notifyDexLoad",
                    new Class[]{String.class, Map.class, String.class}, new Object[]{loadingPackageName, maps, loaderIsa});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


    /*
     * Register DexModule(dex path) To PMS By Binder for Q
     */

    private void realNotifyDexLoad(String loadingPackageName, String dexPath, String loaderIsa) {
        if (mPmObj == null || loadingPackageName == null || dexPath == null || loaderIsa == null) {
            return;
        }
        List<String> classLoadersNames = Collections.singletonList("dalvik.system.DexClassLoader");
        List<String> classPaths = Collections.singletonList(dexPath);
        try {
            ReflectUtils.invokeMethod(mPmObj, "notifyDexLoad",
                    new Class[]{String.class, List.class, List.class, String.class}, new Object[]{loadingPackageName, classLoadersNames, classPaths, loaderIsa});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /*
     * UnRegister DexModule(dex path) To PMS
     */
    private void unregisterDexModule(String pluginPackageName) {
        if (pluginPackageName == null || mContext == null) {
            return;
        }
        String originDir = PluginDirHelper.getSourceDir(mContext,pluginPackageName);
        String tempDir = PluginDirHelper.getPluginApkDir(mContext,pluginPackageName);
        FileUtil.copyFolder(tempDir, originDir);
        String tempFilePath = PluginDirHelper.getTempSourceFile(mContext,pluginPackageName);
        FileUtil.deleteFile(tempFilePath);
        reconcileSecondaryDexFiles();

    }


   /*
     * Real UnRegister DexModule(dex path) To PMS (By Binder)
    */
    private void reconcileSecondaryDexFiles() {
        execCmd(buildReconcileSecondaryDexFilesArgs());
    }


    /*
     * Process CMD (By Binder)（Have system permissions）
            */

    private void execCmd(String[] args) {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeFileDescriptor(FileDescriptor.in);
        data.writeFileDescriptor(FileDescriptor.out);
        data.writeFileDescriptor(FileDescriptor.err);
        data.writeStringArray(args);
        data.writeStrongBinder(null);
        ResultReceiver resultReceiver = new ResultReceiver(null);
        resultReceiver.writeToParcel(data, 0);
        try {
            mPmBinder.transact(SHELL_COMMAND_TRANSACTION, data, reply, 0);
            reply.readException();
        } catch (Throwable e) {
            //Report info
        } finally {
            data.recycle();
            reply.recycle();

        }

    }


    /*
     * Build dexOpt args
     *
     * @param compileFilter compile filter
     * @return cmd args
     */

    private String[] buildDexOptArgs(String compileFilter) {
        return buildArgs("compile", "-m", compileFilter, "-f", "--secondary-dex",
                mContext == null ? "" : mContext.getPackageName());

    }


    /*
     * Build ReconcileSecondaryDexFiles Args
     *
     * @return cmd args
     */

    private String[] buildReconcileSecondaryDexFilesArgs() {
        return buildArgs("reconcile-secondary-dex-files", mContext == null ? "" : mContext.getPackageName());

    }

    private String[] buildArgs(String... string) {
        List argsLs = Arrays.asList(string);
        return (String[]) argsLs.toArray(new String[argsLs.size()]);
    }


    /*
     * Get the InstructionSet through reflection
     */

    private String getCurrentInstructionSet() {
        String currentInstructionSet;
        try {
            Class vmRuntimeClazz = Class.forName("dalvik.system.VMRuntime");
            currentInstructionSet = (String) ReflectUtils.invokeStaticMethod(vmRuntimeClazz,
                    "getCurrentInstructionSet");
        } catch (Throwable e) {
            currentInstructionSet = "arm64";
        }
        return currentInstructionSet;

    }
}
