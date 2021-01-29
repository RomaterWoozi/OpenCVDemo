package com.wuzx.atest.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CrashException implements Thread.UncaughtExceptionHandler{
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private static CrashException myCrashHandler;
    private String TAG="CrashException";
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();


    public static synchronized CrashException getInstance(){
        if(myCrashHandler==null){
            myCrashHandler=new CrashException();
        }
        return myCrashHandler;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context){
        mContext = context;
        //系统默认处理类
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该类为系统默认处理类
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        if(!handleExample(e) && mDefaultHandler != null) { //判断异常是否已经被处理
            mDefaultHandler.uncaughtException(t, e);
        }else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfoToFile(e);
    }

    /**
     * 收集设备参数信息
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 提示用户出现异常
     * 将异常信息保存
     * @param ex
     * @return
     */
    private boolean handleExample(Throwable ex) {
        if(ex == null)
            return false;

        new Thread(() -> {
            Looper.prepare();
            Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }).start();
        saveCrashInfoToFile(ex);

        return true;
    }


    /**
     * 保存错误信息到文件中
     * @param ex
     */
    private void saveCrashInfoToFile(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable exCause = ex.getCause();
        while (exCause != null) {
            exCause.printStackTrace(printWriter);
            exCause =exCause.getCause();
        }
        printWriter.close();

        long timeMillis = System.currentTimeMillis();
        //错误日志文件名称
        String fileName = "crash-" + timeMillis + ".log";
        //判断sd卡可正常使用
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //文件存储位置
            String path = Environment.getExternalStorageDirectory().getPath() + "/crash_logInfo/";
            File fl = new File(path);
            //创建文件夹
            if(!fl.exists()) {
                fl.mkdirs();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(path + fileName);
                fileOutputStream.write(writer.toString().getBytes());
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
