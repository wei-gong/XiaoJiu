package com.qtec.speech.vwk;


import com.qtec.common.utils.LogUtil;

/**
 * Created by fujiayi on 2017/6/21.
 */

public class SimpleWakeupListener implements IWakeupListener {

    private static final String TAG = "SimpleWakeupListener";

    @Override
    public void onSuccess(String word, WakeUpResult result) {
        LogUtil.i(TAG, "唤醒成功，唤醒词：" + word);
    }

    @Override
    public void onStop() {
        LogUtil.i(TAG, "唤醒词识别结束：");
    }

    @Override
    public void onError(int errorCode, String errorMessge, WakeUpResult result) {
        LogUtil.i(TAG, "唤醒错误：" + errorCode + ";错误消息：" + errorMessge + "; 原始返回" + result.getOrigalJson());
    }

    @Override
    public void onASrAudio(byte[] data, int offset, int length) {
        LogUtil.i(TAG, "audio data： " + data.length);
    }

}
