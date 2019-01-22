package com.qtec.assistant.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.qtec.assistant.ChatModel
import com.qtec.assistant.R
import kotlinx.android.synthetic.main.activity_assistant.*

class ActivityAssistant : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistant)

        chatRecyclerView.layoutManager = LinearLayoutManager(this@ActivityAssistant, LinearLayoutManager.VERTICAL, false)
        chatRecyclerView.adapter = ChatAdapter(this@ActivityAssistant, R.layout.adapter_chat_bubble_left, ChatModel.getInstance().chatDatas)
    }
}
