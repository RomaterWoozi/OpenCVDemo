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
    Ptr<CascadeDetectorAdapter> mainDetector;
    Ptr<CascadeDetectorAdapter> trackingDetector;

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
Java_com_wuzx_atest_DetectBaseTracker_nativeCreateObject(JNIEnv *env, jclass clazz,
                                                         jstring cascade_name, jint min_face_size) {

    LOGD("createObject");
    const char* jnamestr
}

JNIEXPORT void JNICALL
Java_com_wuzx_atest_DetectBaseTracker_nativeStart(JNIEnv *env, jclass clazz, jlong thiz) {
    // TODO: implement nativeStart()
    LOGD("Start");
}


JNIEXPORT void JNICALL
Java_com_wuzx_atest_DetectBaseTracker_nativeDestroyObject(JNIEnv *env, jclass clazz, jlong thiz) {
    // TODO: implement nativeDestroyObject()
    LOGD("DestroyObject");
}

JNIEXPORT void JNICALL
Java_com_wuzx_atest_DetectBaseTracker_nativeSetFaceSize(JNIEnv *env, jclass clazz, jlong thiz,
                                                        jint size) {
    // TODO: implement nativeSetFaceSize()
    LOGD("SetFaceSize");
}


JNIEXPORT void JNICALL
Java_com_wuzx_atest_DetectBaseTracker_nativeStop(JNIEnv *env, jclass clazz, jlong thiz) {
    // TODO: implement nativeStop()
    LOGD("Stop");
}

JNIEXPORT void JNICALL
Java_com_wuzx_atest_DetectBaseTracker_nativeDetect(JNIEnv *env, jclass clazz, jlong thiz,
                                                   jlong input_image, jlong faces) {
    // TODO: implement nativeDetect
    LOGD("Detect");
}


#ifdef __cplusplus
}
#endif

