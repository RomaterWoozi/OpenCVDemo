package com.wuzx.atest.media;

public class FFmpegJniHelper {

  static {
      System.loadLibrary("libavcodec");
      System.loadLibrary("libavutil");
      System.loadLibrary("libswresample");
      System.loadLibrary("libavformat");
  }

    //JNI
    public native String urlprotocolinfo();
    public native String avformatinfo();
    public native String avcodecinfo();
    public native String avfilterinfo();
    public native String configurationinfo();
}
