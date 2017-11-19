#include <jni.h>
#include "dalvik.h"

typedef Object *(*FindObject )(void *thread, jobject jobj);

typedef void *(*FindThread)();

FindObject findObject;
FindThread findThread;

/**
 * 修复
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_xyh_fixframework_DexManager_replace(JNIEnv *env,
                                             jobject instance,
                                             jint version,
                                             jobject wrongMethod,
                                             jobject rightMethod) {
 //   java虚拟机Method
//    首先 找到虚拟机对应的Method 结构体
    Method *wrong = (Method *) env->FromReflectedMethod(wrongMethod);
    Method *right = (Method *) env->FromReflectedMethod(rightMethod);
//     把right对应的Object  第一个成员变量ClassObject  status
//    ClassObject

//     hook libdvm
    void *dvm_hand = dlopen("libdvm.so", RTLD_NOW);
    //根据 sdk 版本 10 决定
    findObject = (FindObject) dlsym(dvm_hand,
                                    version > 10 ? "_Z20dvmDecodeIndirectRefP6ThreadP8_jobject"
                                                 : "dvmDecodeIndirectRef");
    findThread = (FindThread) dlsym(dvm_hand,
                                    version > 10 ? "_Z13dvmThreadSelfv" : "dvmThreadSelf");

//  反射获取
    jclass methodClass = env->FindClass("java/lang/reflect/Method");
    jmethodID rightmid = env->GetMethodID(methodClass, "getDeclaringClass", "()Ljava/lang/Class;");

//    method 所声明的Class
    jobject ndkObject = env->CallObjectMethod(rightMethod, rightmid);
    ClassObject *firstField = (ClassObject *) findObject(findThread(), ndkObject);
//    修改状态
    firstField->status = CLASS_INITIALIZED;
//    使用对的参数替换错误的参数
    wrong->accessFlags |= ACC_PUBLIC;
    wrong->methodIndex = right->methodIndex;
    wrong->jniArgInfo = right->jniArgInfo;
    wrong->registersSize = right->registersSize;
    wrong->outsSize = right->outsSize;
//    方法参数原型
    wrong->prototype = right->prototype;
//
    wrong->insns = right->insns;
    wrong->nativeFunc = right->nativeFunc;

}
