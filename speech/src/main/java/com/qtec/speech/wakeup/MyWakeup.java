package com.qtec.speech.wakeup;

import android.content.Context;
import android.content.SharedPreferences;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.qtec.common.utils.LogUtil;
import org.json.JSONObject;
import java.util.Map;

/**
 * @author gongw
 * @date 2018/12/6
 */
public class MyWakeup {

    private static final String TAG = MyWakeup.class.getSimpleName();
    private EventManager wakeupEventManager;
    private ChainWakeupListener chainWakeupListener;
    private WakeupEventAdapter wakeupEventAdapter;
    private static volatile boolean isInitialized = false;
    private WakeupParams wakeupParams;
    private SharedPreferences paramsPreferences;

    public MyWakeup(Context context){
        if(isInitialized){
            LogUtil.e(TAG, "Already holds a Wakeup EventManager, release it firstly.");
            throw new RuntimeException("Already holds a Wakeup EventManager, release it firstly.");
        }
        LogUtil.i(TAG, "start init wake up.");
        Context applicationContext = context.getApplicationContext();
        wakeupEventManager = EventManagerFactory.create(context, "wp");
        chainWakeupListener = new ChainWakeupListener();
        wakeupEventAdapter = new WakeupEventAdapter(chainWakeupListener);
        wakeupEventManager.registerListener(wakeupEventAdapter);
        paramsPreferences = applicationContext.getSharedPreferences("wakeup_params", Context.MODE_PRIVATE);
        //init params here
        loadParams();
        isInitialized = true;
        LogUtil.i(TAG, "wake up initialized success.");
    }

    private void init(Context context){

    }

    public void resetParams(){
        SharedPreferences.Editor editor = paramsPreferences.edit();
        wakeupParams.params.clear();
        wakeupParams = new WakeupParams();
        for(String key : wakeupParams.params.keySet()){
            editor.putString(key, wakeupParams.params.get(key));
        }
        editor.apply();
    }

    public void loadParams(){
        SharedPreferences.Editor editor = paramsPreferences.edit();
        if(wakeupParams == null){
            wakeupParams = new WakeupParams();
        }
        for(String key : wakeupParams.params.keySet()){
            String value = paramsPreferences.getString(key, null);
            if(value != null){
                wakeupParams.params.put(key, value);
            }else{
                editor.putString(key, wakeupParams.params.get(key));
            }
        }
        editor.apply();
    }

    public void start() {
        String json = new JSONObject(wakeupParams.params).toString();
        wakeupEventManager.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
        LogUtil.i(TAG, "start wake up");
    }

    public void stop() {
        wakeupEventManager.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0);
        LogUtil.i(TAG, "stop wake up");
    }



    public void addWakeupListener(IWakeupListener listener){
        chainWakeupListener.addListener(listener);
    }

    public void removeWakeupListener(IWakeupListener listener){
        chainWakeupListener.removeListener(listener);
    }

    public void release() {
        stop();
        wakeupEventManager.unregisterListener(wakeupEventAdapter);
        wakeupEventManager = null;
        isInitialized = false;
        LogUtil.i(TAG, "wake up release");
    }


}
