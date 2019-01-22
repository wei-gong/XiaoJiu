package com.qtec.assistant;

import android.content.res.Resources;

import com.qtec.common.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gongw
 * @date 2019/1/17
 */
public class ChatModel {

    private List<ChatData> cachedChatDatas;

    private static class InstanceHolder{
        private static ChatModel instance = new ChatModel();
    }

    private ChatModel(){
        cachedChatDatas = new ArrayList<>();
        Resources resources = BaseApplication.getBaseApplicationContext().getResources();
        List<ExtraContent> extraContents = new ArrayList<>();
        String[] saySamples = resources.getStringArray(R.array.say_sample);
        for(String sample : saySamples){
            ExtraContent extraContent = new ExtraContent();
            extraContent.setTitle(sample);
            extraContent.setStyle(ExtraContent.Style.SAY_SAMPLE);
            extraContents.add(extraContent);
        }
        addChatDatas(resources.getString(R.string.first_welcome), extraContents, ChatData.Style.CHAT_BUBBLE_LEFT);
    }

    public static ChatModel getInstance(){
        return InstanceHolder.instance;
    }

    public List<ChatData> getChatDatas(){
        return cachedChatDatas;
    }

    public void addChatDatas(String content, List<ExtraContent> extraContents, int style){
        ChatData chatData = new ChatData();
        chatData.setContent(content);
        chatData.setExtraContent(extraContents);
        chatData.setStyle(style);
        chatData.setTime(System.currentTimeMillis());
//        chatData.setImage();
        cachedChatDatas.add(chatData);
    }

}
