package com.wuzx.atest;

import android.app.Application;
import android.graphics.BitmapFactory;

import com.jchsmart.network.AndroidNetworking;
import com.jchsmart.network.common.ConnectionQuality;
import com.jchsmart.network.interfaces.ConnectionQualityChangeListener;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.wuzx.atest.exception.CrashException;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //设置系统默异常处理类
        CrashException crashException=new CrashException();
        crashException.init(this);
        initLogger();
        initNetworking();
    }

    /**
     * @author WuZX
     * 时间  2021/1/14 11:33
     *  日志初始化
     */
    private void initLogger(){
        FormatStrategy customStrategy = PrettyFormatStrategy.newBuilder()
                .tag("WuZX")   // 自定义TAG全部标签，默认PRETTY_L
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(customStrategy){
            @Override
            public boolean isLoggable(int priority, String tag) {
                return true;
//              return LogUtil.isDebug;
            }
        });
        CsvFormatStrategy csvformatStrategy = CsvFormatStrategy.newBuilder()
                .tag("WuZX")
                .build();
        Logger.addLogAdapter(new DiskLogAdapter(csvformatStrategy));
        Logger.d("Android Test");
    }

    /**
     * @author WuZX
     * 时间  2021/1/14 11:33
     * network初始化
     */
    private void initNetworking(){
        AndroidNetworking.initialize(getApplicationContext());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        AndroidNetworking.enableLogging();
        AndroidNetworking.setConnectionQualityChangeListener(new ConnectionQualityChangeListener() {

            @Override
            public void onChange(ConnectionQuality currentConnectionQuality, int currentBandwidth) {
                Logger.d("onChange: currentConnectionQuality : " + currentConnectionQuality + " currentBandwidth : " + currentBandwidth);
            }
        });
    }
}
