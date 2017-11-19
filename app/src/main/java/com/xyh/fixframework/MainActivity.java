package com.xyh.fixframework;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

/**
 * 插件化开发的好处
 * 1、宿主和插件分开编译
 * 2、并发开发
 * 3、动态更新插件
 * 4、按需下载模块
 * 5、方法数或变量数
 * apk做插件化
 * 热修复
 */

/**
 * dalvik  4.4 使用；   4.4以上使用的 art虚拟机
 * /Users/xieyuhai/Documents/Source/android-4.4.4_r1/dalvik/vm/oo/Object.h
 * <p>
 * <p>
 * 1、android-4.4.4_r1\dalvik\libdex\DexFile.h
 * 2、android-4.4.4_r1\dalvik\vm\oo\Object.h
 */
public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("hotfix");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DexManager.getInstance().setContext(this);
    }

    /**
     * 调用计算
     * @param view
     */
    public void cacl(View view) {
        Calculate calcutor = new Calculate();
        Log.e("TAG", "result: " + calcutor.calc());
        Log.e("TAG", "result: " + calcutor.calculate(28090));
        ((Button) view).setText("result:" + calcutor.calc() + "");
    }

    /**
     * 修复按钮-》
     * @param view
     */
    public void fix(View view) {
        Log.e("TAG", "fix: " + Environment.getExternalStorageDirectory());
        DexManager.getInstance().loadFile(new File(Environment.getExternalStorageDirectory(), "out.dex"));
        Log.e("TAG", getString(R.string.success));
    }
}
