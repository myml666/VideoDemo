package com.itfitness.videodemo.widget

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class VideoCameraView : SurfaceView, SurfaceHolder.Callback, Camera.AutoFocusCallback, Runnable ,VideoRecord{
    override fun getCamera(): Camera {
        return mCamera!!
    }

    override fun getSurfaceHolder(): SurfaceHolder {
        return holder
    }
    private var mCamera: Camera? = null//相机
    private var isSupportAutoFocus: Boolean = false//是否支持自动对焦
    private var screenHeight: Int = 0//屏幕的高度
    private var screenWidth: Int = 0//屏幕的宽度
    private var isPreviewing: Boolean = false//是否在预览

    constructor(context: Context) : super(context){
        preDispose()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        preDispose()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        preDispose()
    }

    /**
     * 预处理
     */
    private fun preDispose(){
        val displayMetrics = context.resources.displayMetrics
        screenHeight = displayMetrics.widthPixels
        screenWidth = displayMetrics.heightPixels
        //判断是否支持自动对焦
        isSupportAutoFocus = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)
        holder.addCallback(this@VideoCameraView)
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }
    /**
     * 加载相机配置
     */
    private fun initCamera() {
        try {
            mCamera!!.setPreviewDisplay(holder)//当前控件显示相机数据
            mCamera!!.setDisplayOrientation(90)//调整预览角度
            setCameraParameters()
            startPreview()//打开相机
        } catch (e: Exception) {
            releaseCamera()
        }

    }

    /**
     * 配置相机参数
     */
    private fun setCameraParameters() {
        val parameters = mCamera!!.parameters
        val sizes = parameters.supportedPreviewSizes
        //确定前面定义的预览宽高是camera支持的，不支持取就更大的
        for (i in 0 .. sizes.size) {
            if (sizes[i].width >= screenWidth && sizes[i].height >= screenHeight || i == sizes.size - 1) {
                screenWidth = sizes[i].width
                screenHeight = sizes[i].height
                break
            }
        }
        //设置最终确定的预览大小
        parameters.setPreviewSize(screenWidth, screenHeight)
        mCamera!!.parameters = parameters
    }

    /**
     * 释放相机
     */
    private fun releaseCamera() {
        if (mCamera != null) {
            stopPreview()
            mCamera!!.setPreviewCallback(null)
            mCamera!!.release()
            mCamera = null
        }
    }

    /**
     * 停止预览
     */
    fun stopPreview() {
        if (mCamera != null && isPreviewing) {
            mCamera!!.stopPreview()
            isPreviewing = false
        }
    }

    /**
     * 开始预览
     */
    fun startPreview() {
        if (mCamera != null) {
            mCamera!!.startPreview()
            if (isSupportAutoFocus) {
                mCamera!!.autoFocus(this@VideoCameraView)
            }
            isPreviewing = true
        }
    }

    /**
     * 打开指定摄像头
     */
    fun openCamera() {
        val cameraInfo = Camera.CameraInfo()
        for (cameraId in 0 until Camera.getNumberOfCameras()) {
            Camera.getCameraInfo(cameraId, cameraInfo)
            //打开前置摄像头
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    mCamera = Camera.open(cameraId)
                } catch (e: Exception) {
                    if (mCamera != null) {
                        mCamera!!.release()
                        mCamera = null
                    }
                }

                break
            }
        }
    }

    override fun onAutoFocus(success: Boolean, camera: Camera?) {
        //每隔500毫秒进行对焦
        postDelayed(this@VideoCameraView, 500)
    }

    override fun run() {
        if (mCamera != null) {
            try {
                mCamera!!.autoFocus(this@VideoCameraView)
            } catch (e: Exception) {
                mCamera = null
            }

        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        stopPreview()
        initCamera()

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        releaseCamera()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        try {
            releaseCamera()
            openCamera()
        } catch (e: Exception) {
            mCamera = null
        }
    }
}