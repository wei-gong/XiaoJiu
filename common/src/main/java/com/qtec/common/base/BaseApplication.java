package com.qtec.common.base;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;


/**
 * Base Application, all applications should extend this.
 * @author gongw
 * @date 2018/7/10
 */
public class BaseApplication extends Application {

    private static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        //init ARouter
        ARouter.init(this);
    }

    public static Context getBaseApplicationContext(){
        return app.getApplicationContext();
    }
}
