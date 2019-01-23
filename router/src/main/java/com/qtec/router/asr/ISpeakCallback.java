package com.qtec.router.asr;

/**
 * @author gongw
 * @date 2019/1/22
 */
public interface ISpeakCallback {

    void onSynthesizeStart(String text);
    void onSynthesizeEnd(String text);

    void onSpeechStart(String text);
    void onSpeechEnd(String text);
    void onSpeechProgress(String text, int current);

    void onError(int errorCode, String errorMsg);

}
