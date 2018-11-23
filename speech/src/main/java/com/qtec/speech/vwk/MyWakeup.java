package com.qtec.speech.vwk;

import android.content.Context;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.qtec.common.utils.LogUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fujiayi on 2017/6/20.
 */

public class MyWakeup {


    private static boolean isInited = false;

    private EventManager wp;
    private EventListener eventListener;

    private static final String TAG = "MyWakeup";

    public MyWakeup(Context context, EventListener eventListener) {
        if (isInited) {
            LogUtil.e(TAG, "还未调用release()，请勿新建一个新类");
            throw new RuntimeException("还未调用release()，请勿新建一个新类");
        }
        isInited = true;
        this.eventListener = eventListener;
        wp = EventManagerFactory.create(context, "wp");
        wp.registerListener(eventListener);
    }

    public MyWakeup(Context context, IWakeupListener eventListener) {
        this(context, new WakeupEventAdapter(eventListener));
    }

    public void start(){
        Map<String, Object> params = new HashMap<>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        start(params);
    }

    public void start(Map<String, Object> params) {
        String json = new JSONObject(params).toString();
        LogUtil.i(TAG + ".Debug", "wakeup params(反馈请带上此行日志):" + json);
        wp.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
    }

    public void stop() {
        LogUtil.i(TAG, "唤醒结束");
        wp.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0);
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setEventListener(IWakeupListener eventListener) {
        this.eventListener =  new WakeupEventAdapter(eventListener);
    }

    public void release() {
        stop();
        wp.unregisterListener(eventListener);
        wp = null;
        isInited = false;
    }
}
