#include <jni.h>
#include "dalvik.h"

typedef Object *(*FindObject )(void *thread, jobject jobj);

typedef void *(*FindThread)();

FindObject findObject;
FindThread findThread;

/**
 * �޸�
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_xyh_fixframework_DexManager_replace(JNIEnv *env,
                                             jobject instance,
                                             jint version,
                                             jobject wrongMethod,
                                             jobject rightMethod) {
 //   java�����Method
//    ���� �ҵ��������Ӧ��Method �ṹ��
    Method *wrong = (Method *) env->FromReflectedMethod(wrongMethod);
    Method *right = (Method *) env->FromReflectedMethod(rightMethod);
//     ��right��Ӧ��Object  ��һ����Ա����ClassObject  status
//    ClassObject

//     hook libdvm
    void *dvm_hand = dlopen("libdvm.so", RTLD_NOW);
    //���� sdk �汾 10 ����
    findObject = (FindObject) dlsym(dvm_hand,
                                    version > 10 ? "_Z20dvmDecodeIndirectRefP6ThreadP8_jobject"
                                                 : "dvmDecodeIndirectRef");
    findThread = (FindThread) dlsym(dvm_hand,
                                    version > 10 ? "_Z13dvmThreadSelfv" : "dvmThreadSelf");

//  �����ȡ
    jclass methodClass = env->FindClass("java/lang/reflect/Method");
    jmethodID rightmid = env->GetMethodID(methodClass, "getDeclaringClass", "()Ljava/lang/Class;");

//    method ��������Class
    jobject ndkObject = env->CallObjectMethod(rightMethod, rightmid);
    ClassObject *firstField = (ClassObject *) findObject(findThread(), ndkObject);
//    �޸�״̬
    firstField->status = CLASS_INITIALIZED;
//    ʹ�öԵĲ����滻����Ĳ���
    wrong->accessFlags |= ACC_PUBLIC;
    wrong->methodIndex = right->methodIndex;
    wrong->jniArgInfo = right->jniArgInfo;
    wrong->registersSize = right->registersSize;
    wrong->outsSize = right->outsSize;
//    ��������ԭ��
    wrong->prototype = right->prototype;
//
    wrong->insns = right->insns;
    wrong->nativeFunc = right->nativeFunc;

}
