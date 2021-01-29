//
// Created by WuZX on 2021/1/28.
//

#include "ffmpeg_helloword.h"
#include <jni.h>
#include <libavcodec/avcodec.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL
Java_com_wuzx_atest_media_FFmpegJniHelper_urlprotocolinfo(JNIEnv *env, jobject thiz){
return NULL;
}


JNIEXPORT jstring JNICALL
Java_com_wuzx_atest_media_FFmpegJniHelper_avformatinfo(JNIEnv *env, jobject thiz) {
    // TODO: implement avformatinfo()
    return NULL;
}

JNIEXPORT jstring JNICALL
Java_com_wuzx_atest_media_FFmpegJniHelper_avcodecinfo(JNIEnv *env, jobject thiz) {
    // TODO: implement avformatinfo()
    return NULL;
}

JNIEXPORT jstring JNICALL
Java_com_wuzx_atest_media_FFmpegJniHelper_avfilterinfo(JNIEnv *env, jobject thiz) {
    // TODO: implement avformatinfo()
    return NULL;
}

JNIEXPORT jstring JNICALL
Java_com_wuzx_atest_media_FFmpegJniHelper_configurationinfo(JNIEnv *env, jobject thiz) {
    // TODO: implement avformatinfo()
    return NULL;
}

#ifdef __cplusplus
}
#endif