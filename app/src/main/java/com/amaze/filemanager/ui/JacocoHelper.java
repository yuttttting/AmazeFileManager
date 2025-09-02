package com.amaze.filemanager.ui;
//package com.android.jarvis.jacoco;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import android.content.Context;

public class JacocoHelper {

    private static final String TAG = "JacocoHelper";

    //ec文件的路径
    private  String DEFAULT_COVERAGE_FILE_PATH = "";

    // 通过构造函数传递 Context
    public JacocoHelper(Context context) {
        Context context1 = context.getApplicationContext(); // 避免内存泄漏
        DEFAULT_COVERAGE_FILE_PATH= Objects.requireNonNull(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS))
                .getPath() + "/coverage.ec";
    }
    /**
     * 生成ec文件
     *
     * @param isNew 是否重新创建ec文件
     */
    public void generateEcFile(boolean isNew) {
        OutputStream out = null;

        Log.d("EC目录", DEFAULT_COVERAGE_FILE_PATH);
        File mCoverageFilePath = new File(DEFAULT_COVERAGE_FILE_PATH);
        try {
            if (isNew && mCoverageFilePath.exists()) {
                Log.d(TAG, "清除旧的ec文件");
                mCoverageFilePath.delete();
            }
            if (!mCoverageFilePath.exists()) {
                mCoverageFilePath.createNewFile();
            }
            out = new FileOutputStream(mCoverageFilePath.getPath(), true);
            Object agent = Class.forName("org.jacoco.agent.rt.RT")
                    .getMethod("getAgent")
                    .invoke(null);
            if (agent != null) {
                out.write((byte[]) agent.getClass().getMethod("getExecutionData", boolean.class)
                        .invoke(agent, false));
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}