package com.itfitness.videodemo.manager

import android.media.MediaRecorder
import com.itfitness.videodemo.util.FileUtil
import com.itfitness.videodemo.widget.VideoRecord


class VideoRecordManager(private val videoRecord: VideoRecord) {
    private var mRecorder: MediaRecorder? = null
    /**
     * 开始录制
     */
    fun start() {
        if (mRecorder == null) {
            mRecorder = MediaRecorder()
        }
        videoRecord.getCamera().unlock()
        mRecorder!!.setCamera(videoRecord.getCamera())
        // 这两项需要放在setOutputFormat之前
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
        mRecorder!!.setVideoSource(MediaRecorder.VideoSource.CAMERA)

        // Set output file format
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)

        // 这两项需要放在setOutputFormat之后
        mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP)

        mRecorder!!.setVideoSize(640, 480)
        mRecorder!!.setVideoFrameRate(30)
        mRecorder!!.setVideoEncodingBitRate(3 * 1024 * 1024)
        mRecorder!!.setOrientationHint(270)
        //设置记录会话的最大持续时间（毫秒）
        mRecorder!!.setMaxDuration(30 * 1000)
        mRecorder!!.setPreviewDisplay(videoRecord.getSurfaceHolder().surface)
        mRecorder!!.setOutputFile(FileUtil.getVideoSavePath())
        mRecorder!!.prepare()
        mRecorder!!.start()
    }

    /**
     * 停止录制
     */
    fun stop() {
        mRecorder!!.stop()
        mRecorder!!.reset()
        mRecorder!!.release()
        mRecorder = null
    }
}