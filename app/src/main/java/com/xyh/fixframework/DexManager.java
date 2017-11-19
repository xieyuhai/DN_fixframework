package com.xyh.fixframework;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import dalvik.system.DexFile;

/**
 * 小于等于18 可用
 * Created by xieyuhai on 2017/11/16.
 */

public class DexManager {

    private Context context;

    private static final DexManager instance = new DexManager();

    public static DexManager getInstance() {
        return instance;
    }


    private DexManager() {

    }

    public void setContext(Context ctx) {
        this.context = ctx;
    }

    /**
     * @param file
     */
    public void loadFile(File file) {
        try {
            //加载修复dex文件
            DexFile dexFile = DexFile.loadDex(file.getAbsolutePath(),
                    new File(context.getCacheDir(), "opt").getAbsolutePath(), Context.MODE_PRIVATE);
//        得到class  --取出修复好的Method
            Enumeration<String> entry = dexFile.entries();

//            循环修复
            while (entry.hasMoreElements()) {
                //拿到全类名
                String className = entry.nextElement();

                Class clazz = dexFile.loadClass(className, context.getClassLoader());
                if (clazz != null) {
                    fixClazz(clazz);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 找出注解中的函数并对其进行修复
     * 仅支持sdk18 及 以下的版本
     *
     * @param realClazz
     */
    private void fixClazz(Class realClazz) {
        //服务器修复好的  realClazz
        Method[] methods = realClazz.getDeclaredMethods();
        for (Method rightMethod : methods) {
            Replace replace = rightMethod.getAnnotation(Replace.class);
            if (replace == null) {
                continue;
            }
//            找到了修复好的Method  找到出bug的Method
            String wrongClass = replace.clazz();
            String wrongMethodName = replace.method();
            try {
                Class clazz = Class.forName(wrongClass);
                Method wrongMethod = clazz.getDeclaredMethod(wrongMethodName, rightMethod.getParameterTypes());
                if (Build.VERSION.SDK_INT <= 18) {
                    replace(Build.VERSION.SDK_INT, wrongMethod, rightMethod);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修复
     *
     * @param version sdk版本
     * @param wrongMethod 错误的方法
     * @param rightMethod 修复好的方法
     */
    private native void replace(int version, Method wrongMethod, Method rightMethod);
}
