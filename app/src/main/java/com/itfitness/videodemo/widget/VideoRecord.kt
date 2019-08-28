package com.itfitness.videodemo.widget

import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder

interface VideoRecord {
    /**
     * 返回相机对象
     */
    fun getCamera():Camera

    /**
     * 返回SurfaceHolder对象
     */
    fun getSurfaceHolder():SurfaceHolder
}