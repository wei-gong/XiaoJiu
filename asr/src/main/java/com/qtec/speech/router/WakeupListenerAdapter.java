package com.qtec.speech.router;

import com.qtec.router.asr.ISpeakCallback;
import com.qtec.router.asr.IWakeupCallback;
import com.qtec.speech.wakeup.IWakeupListener;
import com.qtec.speech.wakeup.WakeUpResult;


/**
 * @author gongw
 * @date 2019/1/22
 */
public class WakeupListenerAdapter implements IWakeupListener {

    private IWakeupCallback callback;

    public WakeupListenerAdapter(IWakeupCallback callback){
        this.callback = callback;
    }

    @Override
    public void onSuccess(String word, WakeUpResult result) {
        callback.onSuccess(word);
    }

    @Override
    public void onStop() {
        callback.onStop();
    }

    @Override
    public void onError(int errorCode, String errorMessge, WakeUpResult result) {
        callback.onError(errorCode, errorMessge);
    }

    @Override
    public void onASrAudio(byte[] data, int offset, int length) {

    }
}
