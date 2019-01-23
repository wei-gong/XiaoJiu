package com.qtec.speech.router;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.qtec.router.asr.ISpeakCallback;

/**
 * @author gongw
 * @date 2019/1/22
 */
public class SpeechSynthesizerListenerAdapter implements SpeechSynthesizerListener {

    private ISpeakCallback callback;

    public SpeechSynthesizerListenerAdapter(ISpeakCallback callback){
        this.callback = callback;
    }

    @Override
    public void onSynthesizeStart(String s) {
        callback.onSynthesizeStart(s);
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

    }

    @Override
    public void onSynthesizeFinish(String s) {
        callback.onSynthesizeEnd(s);
    }

    @Override
    public void onSpeechStart(String s) {
        callback.onSpeechStart(s);
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {
        callback.onSpeechProgress(s, i);
    }

    @Override
    public void onSpeechFinish(String s) {
        callback.onSpeechEnd(s);
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        callback.onError(0, s);
    }
}
