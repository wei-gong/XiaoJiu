package com.qtec.speech.router;

import com.qtec.router.asr.IRecogCallback;
import com.qtec.speech.recog.IRecogListener;
import com.qtec.speech.recog.RecogResult;

import java.util.Arrays;

/**
 * @author gongw
 * @date 2019/1/22
 */
public class RecogListenerAdapter implements IRecogListener {

    private IRecogCallback callback;

    public RecogListenerAdapter(IRecogCallback callback){
        this.callback = callback;
    }

    @Override
    public void onAsrReady() {

    }

    @Override
    public void onAsrBegin() {
        callback.onStart();
    }

    @Override
    public void onAsrEnd() {
        callback.onEnd();
    }

    @Override
    public void onAsrPartialResult(String[] results, RecogResult recogResult) {

    }

    @Override
    public void onAsrOnlineNluResult(String nluResult) {

    }

    @Override
    public void onAsrFinalResult(String[] results, RecogResult recogResult) {
        callback.onResult(Arrays.toString(results));
    }

    @Override
    public void onAsrFinish(RecogResult recogResult) {

    }

    @Override
    public void onAsrFinishError(int errorCode, int subErrorCode, String descMessage, RecogResult recogResult) {
        callback.onError(errorCode, descMessage);
    }

    @Override
    public void onAsrLongFinish() {

    }

    @Override
    public void onAsrVolume(int volumePercent, int volume) {

    }

    @Override
    public void onAsrAudio(byte[] data, int offset, int length) {

    }

    @Override
    public void onAsrExit() {

    }

    @Override
    public void onOfflineLoaded() {

    }

    @Override
    public void onOfflineUnLoaded() {

    }
}
