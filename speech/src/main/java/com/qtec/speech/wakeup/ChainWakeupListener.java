package com.qtec.speech.wakeup;

import java.util.ArrayList;

/**
 * @author gongw
 * @date 2018/12/7
 */
public class ChainWakeupListener implements IWakeupListener {

    private ArrayList<IWakeupListener> listeners = new ArrayList<>();

    public void addListener(IWakeupListener listener){
        if(listener != null){
            listeners.add(listener);
        }
    }

    public void removeListener(IWakeupListener listener){
        if(listener != null){
            listeners.remove(listener);
        }
    }

    @Override
    public void onSuccess(String word, WakeUpResult result) {
        for(IWakeupListener listener : listeners){
            listener.onSuccess(word, result);
        }
    }

    @Override
    public void onStop() {
        for(IWakeupListener listener : listeners){
            listener.onStop();
        }
    }

    @Override
    public void onError(int errorCode, String errorMessge, WakeUpResult result) {
        for(IWakeupListener listener : listeners){
            listener.onError(errorCode, errorMessge, result);
        }
    }

    @Override
    public void onASrAudio(byte[] data, int offset, int length) {
        for(IWakeupListener listener : listeners){
            listener.onASrAudio(data, offset, length);
        }
    }
}
