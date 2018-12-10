package com.qtec.speech.synthesize;


import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;
import java.util.ArrayList;

/**
 * @author gongw
 * @date 2018/12/6
 */

public class ChainSynthesizerListener implements SpeechSynthesizerListener {

    private ArrayList<SpeechSynthesizerListener> listeners;

    public ChainSynthesizerListener() {
        listeners = new ArrayList<>();
    }

    public void addListener(SpeechSynthesizerListener listener) {
        if(listener != null){
            listeners.add(listener);
        }
    }

    public void removeListener(SpeechSynthesizerListener listener){
        if(listener != null){
            listeners.remove(listener);
        }
    }

    @Override
    public void onSynthesizeStart(String s) {
        for(SpeechSynthesizerListener listener : listeners){
            listener.onSynthesizeStart(s);
        }
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
        for(SpeechSynthesizerListener listener : listeners){
            listener.onSynthesizeDataArrived(s, bytes, i);
        }
    }

    @Override
    public void onSynthesizeFinish(String s) {
        for(SpeechSynthesizerListener listener : listeners){
            listener.onSynthesizeFinish(s);
        }
    }

    @Override
    public void onSpeechStart(String s) {
        for(SpeechSynthesizerListener listener : listeners){
            listener.onSpeechStart(s);
        }
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {
        for(SpeechSynthesizerListener listener : listeners){
            listener.onSpeechProgressChanged(s, i);
        }
    }

    @Override
    public void onSpeechFinish(String s) {
        for(SpeechSynthesizerListener listener : listeners){
            listener.onSpeechFinish(s);
        }
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        for(SpeechSynthesizerListener listener : listeners){
            listener.onError(s, speechError);
        }
    }
}
