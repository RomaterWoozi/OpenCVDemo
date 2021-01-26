package com.wuzx.atest.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.wuzx.atest.R;

/**
* @author WuZX
* 时间  2021/1/22 17:47
*  水波视图
*/
public class WaterWaveView extends View {
    private Paint paint;//画笔
    private Path path;//当前的path
    private int waveLenght = 400;//波长
    private int waveHeight = 80;//波峰
    private boolean isRise = false;//是否涨水
    private int  dx;//涨水的dx
    private int dy;//涨水的dy
    private Bitmap bitmap;
    private int width ;//当前的宽度
    private int height ;//当前的高度
    private int waveView_boatBitmap;
    private int diration;//次序的时间
    private int originY;//记录车的高度
    private Region region;
    private Context context;
    private ValueAnimator valueAnimator;

    public WaterWaveView(Context context) {
        this(context,null);
        
    }

    public WaterWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaterWaveView);
        if(typedArray != null){
            waveLenght = (int) typedArray.getDimension(R.styleable.WaterWaveView_waveLegth,500);
            waveHeight = (int) typedArray.getDimension(R.styleable.WaterWaveView_waveHeight,200);
            isRise =  typedArray.getBoolean(R.styleable.WaterWaveView_rise,false);
            originY = (int) typedArray.getDimension(R.styleable.WaterWaveView_originY,500);
            diration =  typedArray.getInteger(R.styleable.WaterWaveView_diration,2000);
            waveView_boatBitmap =  typedArray.getResourceId(R.styleable.WaterWaveView_waveView_boatBitmap, 0);
        }
        typedArray.recycle();
        this.context = context;
        initVews();
    }

    public WaterWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }


    private void initVews() {
        paint = new Paint();paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;//缩放图片
        if(waveView_boatBitmap != 0){
            // bitmap = BitmapFactory.decodeResource(context.getResources(),waveView_boatBitmap,options);
            bitmap = BitmapFactory.decodeResource(context.getResources(),waveView_boatBitmap,options);
        }else{
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.sun_pic,options);
        }
        path = new Path();
    }



}
