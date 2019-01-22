package com.qtec.router.provider;



import android.app.Activity;
import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author gongw
 * @date 2019/1/18
 */
public interface IAssistantProvider extends IProvider {

    String PATH_ASSISTANT_SERVICE = "/assistant/service";

    void goToAssistantActivity(Activity activity);

}
