package com.amaze.filemanager.ui;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class JacocoHelper {

    private static final String TAG = "JacocoHelper";

    //ec文件的路径
    private static final String DEFAULT_COVERAGE_FILE_PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/coverage.ec";
//    private static final String DEFAULT_COVERAGE_FILE_PATH = "/sdcard/coverage.ec";

    /**
     * 生成ec文件
     *
     * @param isNew 是否重新创建ec文件
     */
    public static void generateEcFile(boolean isNew) {
        OutputStream out = null;
        File mCoverageFilePath = new File(DEFAULT_COVERAGE_FILE_PATH);
        try {
            if (isNew && mCoverageFilePath.exists()) {
                Log.d(TAG, "清除旧的ec文件");
                mCoverageFilePath.delete();
            }
            if (!mCoverageFilePath.exists()) {
                Log.d(TAG, "新建ec文件");
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
