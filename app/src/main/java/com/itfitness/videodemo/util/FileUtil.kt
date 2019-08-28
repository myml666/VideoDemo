package com.itfitness.videodemo.util

import android.content.Context
import android.os.Environment
import java.io.File

object FileUtil {
    /**
     * 获取视频保存路径
     */
    fun getVideoSavePath():String{
        return Environment.getExternalStorageDirectory().path+File.separator+"test.mp4"
    }
}