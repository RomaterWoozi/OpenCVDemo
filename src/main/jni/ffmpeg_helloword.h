//
// Created by WuZX on 2021/1/28.
//
/*
 * Protocol:  FFmpeg类库支持的协议
 * AVFormat:  FFmpeg类库支持的封装格式
 * AVCodec:   FFmpeg类库支持的编解码器
 * AVFilter:  FFmpeg类库支持的滤镜
 * Configure: FFmpeg类库的配置信息
 *
 */

#ifndef JCHSMART_FFMPEG_HELLOWORD_H
#define JCHSMART_FFMPEG_HELLOWORD_H
#include <stdio.h>
#ifdef WUZX
#include <android/log.h>
#define LOGE(format, ...)  __android_log_print(ANDROID_LOG_ERROR, "(>_<)", format, ##__VA_ARGS__)
#else
#define LOGE(format, ...)  printf("(>_<) " format "\n", ##__VA_ARGS__)
#endif

class ffmpeg_helloword {

};




#endif //JCHSMART_FFMPEG_HELLOWORD_H
