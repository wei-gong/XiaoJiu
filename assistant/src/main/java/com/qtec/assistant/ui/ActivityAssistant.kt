package com.qtec.assistant.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import com.alibaba.android.arouter.launcher.ARouter
import com.qtec.assistant.ChatModel
import com.qtec.assistant.R
import com.qtec.router.asr.IAsrProvider
import com.qtec.router.asr.IRecogCallback
import kotlinx.android.synthetic.main.activity_assistant.*

class ActivityAssistant : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistant)

        chatRecyclerView.layoutManager = LinearLayoutManager(this@ActivityAssistant, LinearLayoutManager.VERTICAL, false)
        chatRecyclerView.adapter = ChatAdapter(this@ActivityAssistant, R.layout.adapter_chat_bubble_left, ChatModel.getInstance().chatDatas)

        val asrService : IAsrProvider = ARouter.getInstance().build(IAsrProvider.PATH_ASR_SERVICE).navigation() as IAsrProvider

        ivSay.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> asrService.startRecognize(recogCallback)
                MotionEvent.ACTION_UP -> asrService.stopRecognize()
            }
            true
        }
    }

    private val recogCallback = object:IRecogCallback{
        override fun onStart() {

        }

        override fun onEnd() {

        }

        override fun onResult(result: String?) {

        }

        override fun onError(errorCode: Int, errorMsg: String?) {

        }

    }
}
