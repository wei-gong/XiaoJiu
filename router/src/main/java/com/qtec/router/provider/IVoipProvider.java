package com.qtec.router.provider;



import android.app.Activity;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author gongw
 * @date 2019/1/18
 */
public interface IVoipProvider extends IProvider {

    String PATH_VOIP_SERVICE = "/voip/service";

    void goAnswerCallActivity(Activity activity);

}
