package com.mariko.karaoke;

import android.content.Context;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/** 添加本地的音频资源
 * @author: mariko
 * @date: 2022/9/7
 */
class LocalDataHelp {
    public static void initLocalData(Context context) {
        copyAssetsToFile(context, "houlai_bz.mp3");
        copyAssetsToFile(context, "houlai_yc.mp3");

        copyAssetsToFile(context, "qfdy_yc.mp3");
        copyAssetsToFile(context, "qfdy_bz.mp3");

        copyAssetsToFile(context, "xq_bz.mp3");
        copyAssetsToFile(context, "xq_yc.mp3");

        copyAssetsToFile(context, "nuannuan_bz.mp3");
        copyAssetsToFile(context, "nuannuan_yc.mp3");

        copyAssetsToFile(context, "jda.mp3");
        copyAssetsToFile(context, "jda_bz.mp3");

        copyAssetsToFile(context, "houlai_lrc.vtt");
        copyAssetsToFile(context, "qfdy_lrc.vtt");
        copyAssetsToFile(context, "xq_lrc.vtt");
        copyAssetsToFile(context, "nuannuan_lrc.vtt");
        copyAssetsToFile(context, "jda_lrc.vtt");
    }

    public static void copyAssetsToFile(Context context, String name) {
        String savePath = ContextCompat.getExternalFilesDirs(context, null)[0].getAbsolutePath();
        String filename = savePath + "/" + name;
        File dir = new File(savePath);
        // 如果目录不存在，创建这个目录
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            if (!(new File(filename)).exists()) {
                InputStream is = context.getResources().getAssets().open(name);
                FileOutputStream fos = new FileOutputStream(filename);
                byte[] buffer = new byte[7168];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
