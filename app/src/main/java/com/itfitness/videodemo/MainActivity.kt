package com.itfitness.videodemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.itfitness.videodemo.manager.VideoRecordManager
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {
    private var flagPlay:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val videoRecordManager = VideoRecordManager(camera)
        bt.setOnClickListener {
            if(flagPlay){
                videoRecordManager.stop()
                bt.text = "开始"
                Toast.makeText(this@MainActivity,"结束",Toast.LENGTH_SHORT).show()
            }else{
                videoRecordManager.start()
                bt.text = "结束"
                Toast.makeText(this@MainActivity,"开始",Toast.LENGTH_SHORT).show()
            }
            flagPlay = !flagPlay
        }
        bt_play.setOnClickListener {
            startActivity(Intent(this@MainActivity, PlayerRecordActivity::class.java))
        }
    }
}
