#include <jni.h>
#include <string>

// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("mylibrary");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("mylibrary")
//      }
//    }
extern "C"
JNIEXPORT jstring JNICALL
Java_com_zhao_mylibrary_MyStringUtil_getStringNdk(JNIEnv *env, jclass clazz) {
    std::string hello = "hello";
    return env->NewStringUTF(hello.c_str());
}