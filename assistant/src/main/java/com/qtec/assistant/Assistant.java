package com.qtec.assistant;

import android.content.res.Resources;
import com.qtec.assistant.ChatData;
import com.qtec.assistant.ChatModel;
import com.qtec.assistant.ExtraContent;
import com.qtec.assistant.R;
import com.qtec.common.base.BaseApplication;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gongw
 * @date 2019/1/18
 */
public class Assistant {

    public void welcome() {
        Resources resources = BaseApplication.getBaseApplicationContext().getResources();
        String contnet = resources.getString(R.string.first_welcome);
        List<ExtraContent> extraContents = new ArrayList<>();
        String[] saySamples = resources.getStringArray(R.array.say_sample);
        for(int i=0;i<saySamples.length;i++){
            ExtraContent extraContent = new ExtraContent();
            extraContent.setTitle(saySamples[i]);
            extraContent.setStyle(ExtraContent.Style.SAY_SAMPLE);
            extraContents.add(extraContent);
        }
        ChatModel.getInstance().addChatDatas(contnet, extraContents, ChatData.Style.CHAT_BUBBLE_LEFT);

    }

    public void request(){

    }


}
