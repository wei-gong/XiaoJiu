package com.qtec.assistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qtec.assistant.ui.ActivityAssistant;
import com.qtec.router.provider.IAssistantProvider;

/**
 * @author gongw
 * @date 2019/1/21
 */
@Route(path = IAssistantProvider.PATH_ASSISTANT_SERVICE, name = "assistant")
public class AssistantRouteService implements IAssistantProvider {

    @Override
    public void goToAssistantActivity(Activity activity) {
        Intent intent = new Intent(activity, ActivityAssistant.class);
        activity.startActivity(intent);
    }

    @Override
    public void init(Context context) {

    }
}
