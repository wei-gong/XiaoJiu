package com.qtec.xiaojiu;

import android.content.res.Resources;
import com.qtec.common.base.BaseApplication;
import com.qtec.router.provider.ITodoProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gongw
 * @date 2019/1/21
 */
public class SupportServicesModel {

    private List<SupportService> cachedSupportService;

    private static class InstanceHolder{
        private static SupportServicesModel instance = new SupportServicesModel();
    }

    private SupportServicesModel(){
        cachedSupportService = new ArrayList<>();
        Resources resources = BaseApplication.getBaseApplicationContext().getResources();
        //create remind service
        SupportService remindService = new SupportService();
        remindService.setTitle(resources.getString(R.string.remind));
        remindService.setSubTitle(resources.getString(R.string.remind_description));
        remindService.setRoutePath(ITodoProvider.PATH_TODO_SERVICE);
        cachedSupportService.add(remindService);
    }

    public static SupportServicesModel getInstance(){
        return InstanceHolder.instance;
    }

    public List<SupportService> getSupportServiceList(){
        return cachedSupportService;
    }
}
