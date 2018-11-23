package com.qtec.speech.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.support.v7.app.AppCompatActivity
import com.qtec.speech.R
import com.qtec.speech.asr.MyRecognizer
import com.qtec.speech.asr.listener.StatusRecogListener

/**
 *
 * @author gongw
 * @date 2018/11/22
 */
class RecognitionActivity : AppCompatActivity(){

    private var myRecognizer:MyRecognizer? = null

    private var mySyntherizer:MyRecognizer? = null

    companion object {
        fun startActivity(activity: Activity){
            val intent = Intent(activity, RecognitionActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recognition)
    }

    private fun initRecognizer(){
        myRecognizer = MyRecognizer(this@RecognitionActivity, object:StatusRecogListener(){
            override fun onAsrBegin() {
                super.onAsrBegin()
                SpeechRecognizer
            }
        })
    }

    private fun initSyntherizer(){
    }

    override fun onResume() {
        super.onResume()
        myRecognizer?.start()
    }

    override fun onPause() {
        super.onPause()
        myRecognizer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        myRecognizer?.release()
    }


}