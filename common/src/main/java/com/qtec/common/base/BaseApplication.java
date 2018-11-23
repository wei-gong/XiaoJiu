package com.qtec.common.base;

import android.app.Application;
import android.content.Context;


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


    }

    public static Context getContext(){
        return app.getApplicationContext();
    }
}
