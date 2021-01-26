//
// Created by WuZX on 2021/1/20.
//
#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/objdetect.hpp>

#include <string>
#include <vector>

#include <android/log.h>


#define LOG_TAG "DetectBaseTracker"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))

using namespace std;
using namespace cv;

// 内联函数
inline void vector_Rect_to_Mat(vector<Rect>& v_rect,Mat& mat)
{
    mat=Mat(v_rect, true);
}


class CascadeDetectorAdapter: public DetectionBasedTracker::IDetector
{
public:
    CascadeDetectorAdapter(cv::Ptr<cv::CascadeClassifier> detector):
            IDetector(),
            Detector(detector)
    {
        LOGD("CascadeDetectorAdapter::Detect::Detect");
        CV_Assert(detector);
    }

    void detect(const cv::Mat &Image, std::vector<cv::Rect> &objects)
    {
        LOGD("CascadeDetectorAdapter::Detect: begin");
        LOGD("CascadeDetectorAdapter::Detect: scaleFactor=%.2f, minNeighbours=%d, minObjSize=(%dx%d), maxObjSize=(%dx%d)", scaleFactor, minNeighbours, minObjSize.width, minObjSize.height, maxObjSize.width, maxObjSize.height);
        Detector->detectMultiScale(Image, objects, scaleFactor, minNeighbours, 0, minObjSize, maxObjSize);
        LOGD("CascadeDetectorAdapter::Detect: end");
    }

    /**
    * @author WuZX
    * 时间  2021/1/20 15:23
    * 虚函数
    */
    virtual ~CascadeDetectorAdapter()
    {
        LOGD("CascadeDetectorAdapter::Detect::~Detect");
    }



private:
    CascadeDetectorAdapter();
    cv::Ptr<cv::CascadeClassifier> Detector;

};

struct DetectorAgregator
{
    cv::Ptr<CascadeDetectorAdapter> mainDetector;
    cv::Ptr<CascadeDetectorAdapter> trackingDetector;

    cv::Ptr<DetectionBasedTracker> tracker;
    DetectorAgregator(cv::Ptr<CascadeDetectorAdapter>& _mainDetector, cv::Ptr<CascadeDetectorAdapter>& _trackingDetector):
            mainDetector(_mainDetector),
            trackingDetector(_trackingDetector)
    {
        CV_Assert(_mainDetector);
        CV_Assert(_trackingDetector);

        DetectionBasedTracker::Parameters DetectorParams;
        tracker = makePtr<DetectionBasedTracker>(mainDetector, trackingDetector, DetectorParams);
    }
};

#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT jlong JNICALL
Java_com_wuzx_atest_opencv_DetectBaseTracker_nativeCreateObject(JNIEnv *env, jclass clazz,
                                                         jstring cascade_name, jint min_face_size) {

    LOGD("createObject");
    const char* jnamestr=env->GetStringUTFChars(cascade_name,NULL);
    string stdFileName(jnamestr);
    jlong result=0;
    try {
        Ptr<CascadeDetectorAdapter> mainDetector = makePtr<CascadeDetectorAdapter>(
                makePtr<CascadeClassifier>(stdFileName));
        Ptr<CascadeDetectorAdapter> trackingDetector = makePtr<CascadeDetectorAdapter>(
                makePtr<CascadeClassifier>(stdFileName));
        result = (jlong)new DetectorAgregator(mainDetector, trackingDetector);
        if(min_face_size>0){
            mainDetector->setMinObjectSize(Size(min_face_size,min_face_size));
        }

    }
    catch (const Exception& exception) {
        LOGD("nativeCreateObject caught cv::Exception: %s", exception.what());
        jclass je = env->FindClass("org/opencv/core/CvException");
        if(!je)
            je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, exception.what());
    }
    catch (...)
    {
        LOGD("nativeCreateObject caught unknown exception");
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "Unknown exception in JNI code of DetectionBasedTracker.nativeCreateObject()");
        return 0;
    }
    return result;
}

JNIEXPORT void JNICALL
Java_com_wuzx_atest_opencv_DetectBaseTracker_nativeStart(JNIEnv *env, jclass clazz, jlong thiz) {
    // TODO: implement nativeStart()
    LOGD("Start");
    try
    {
        ((DetectorAgregator*)thiz)->tracker->run();
    }
    catch(const cv::Exception& e)
    {
        LOGD("nativeStart caught cv::Exception: %s", e.what());
        jclass je = env->FindClass("org/opencv/core/CvException");
        if(!je)
            je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGD("nativeStart caught unknown exception");
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "Unknown exception in JNI code of DetectionBasedTracker.nativeStart()");
    }
}


JNIEXPORT void JNICALL
Java_com_wuzx_atest_opencv_DetectBaseTracker_nativeDestroyObject(JNIEnv *env, jclass clazz, jlong thiz) {
    // TODO: implement nativeDestroyObject()
    LOGD("DestroyObject");
    try
    {
        if(thiz != 0)
        {
            ((DetectorAgregator*)thiz)->tracker->stop();
            delete (DetectorAgregator*)thiz;
        }
    }
    catch(const cv::Exception& e)
    {
        LOGD("nativeestroyObject caught cv::Exception: %s", e.what());
        jclass je = env->FindClass("org/opencv/core/CvException");
        if(!je)
            je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGD("nativeDestroyObject caught unknown exception");
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "Unknown exception in JNI code of DetectionBasedTracker.nativeDestroyObject()");
    }
}

JNIEXPORT void JNICALL
Java_com_wuzx_atest_opencv_DetectBaseTracker_nativeSetFaceSize(JNIEnv *env, jclass clazz, jlong thiz,
                                                        jint faceSize) {
    // TODO: implement nativeSetFaceSize()
    LOGD("SetFaceSize");
    try
    {
        if (faceSize > 0)
        {
            ((DetectorAgregator*)thiz)->mainDetector->setMinObjectSize(Size(faceSize, faceSize));
            //((DetectorAgregator*)thiz)->trackingDetector->setMinObjectSize(Size(faceSize, faceSize));
        }
    }
    catch(const cv::Exception& e)
    {
        LOGD("nativeSetFaceSize caught cv::Exception: %s", e.what());
        jclass je = env->FindClass("org/opencv/core/CvException");
        if(!je)
            je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGD("nativeSetFaceSize caught unknown exception");
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "Unknown exception in JNI code of DetectionBasedTracker.nativeSetFaceSize()");
    }
}


JNIEXPORT void JNICALL
Java_com_wuzx_atest_opencv_DetectBaseTracker_nativeStop(JNIEnv *env, jclass clazz, jlong thiz) {
    // TODO: implement nativeStop()
    LOGD("Stop");
    try
    {
        ((DetectorAgregator*)thiz)->tracker->stop();
    }
    catch(const cv::Exception& e)
    {
        LOGD("nativeStop caught cv::Exception: %s", e.what());
        jclass je = env->FindClass("org/opencv/core/CvException");
        if(!je)
            je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGD("nativeStop caught unknown exception");
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "Unknown exception in JNI code of DetectionBasedTracker.nativeStop()");
    }
}

JNIEXPORT void JNICALL
Java_com_wuzx_atest_opencv_DetectBaseTracker_nativeDetect(JNIEnv *env, jclass clazz, jlong thiz,
                                                   jlong input_image, jlong faces) {
    // TODO: implement nativeDetect
    LOGD("Detect");
    try
    {
        vector<Rect> RectFaces;
        ((DetectorAgregator*)thiz)->tracker->process(*((Mat*)input_image));
        ((DetectorAgregator*)thiz)->tracker->getObjects(RectFaces);
        *((Mat*)faces) = Mat(RectFaces, true);
    }
    catch(const cv::Exception& e)
    {
        LOGD("nativeCreateObject caught cv::Exception: %s", e.what());
        jclass je = env->FindClass("org/opencv/core/CvException");
        if(!je)
            je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, e.what());
    }
    catch (...)
    {
        LOGD("nativeDetect caught unknown exception");
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "Unknown exception in JNI code DetectionBasedTracker.nativeDetect()");
    }
}



#ifdef __cplusplus
}
#endif
