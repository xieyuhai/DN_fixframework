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
 * ����������ĺô�
 * 1�������Ͳ���ֿ�����
 * 2����������
 * 3����̬���²��
 * 4����������ģ��
 * 5���������������
 * apk�������
 * ���޸�
 */

/**
 * dalvik  4.4 ʹ�ã�   4.4����ʹ�õ� art�����
 * /Users/xieyuhai/Documents/Source/android-4.4.4_r1/dalvik/vm/oo/Object.h
 * <p>
 * <p>
 * 1��android-4.4.4_r1\dalvik\libdex\DexFile.h
 * 2��android-4.4.4_r1\dalvik\vm\oo\Object.h
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
     * ���ü���
     * @param view
     */
    public void cacl(View view) {
        Calculate calcutor = new Calculate();
        Log.e("TAG", "result: " + calcutor.calc());
        Log.e("TAG", "result: " + calcutor.calculate(28090));
        ((Button) view).setText("result:" + calcutor.calc() + "");
    }

    /**
     * �޸���ť-��
     * @param view
     */
    public void fix(View view) {
        Log.e("TAG", "fix: " + Environment.getExternalStorageDirectory());
        DexManager.getInstance().loadFile(new File(Environment.getExternalStorageDirectory(), "out.dex"));
        Log.e("TAG", getString(R.string.success));
    }
}
