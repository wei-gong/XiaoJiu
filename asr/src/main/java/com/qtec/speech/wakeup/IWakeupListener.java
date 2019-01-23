package com.qtec.speech.wakeup;

/**
 * @author gongw
 * @date 2018/12/6
 */

public interface IWakeupListener {

    void onSuccess(String word, WakeUpResult result);

    void onStop();

    void onError(int errorCode, String errorMessge, WakeUpResult result);

    void onASrAudio(byte[] data, int offset, int length);
}
