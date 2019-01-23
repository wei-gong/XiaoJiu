package com.qtec.speech.router;

import android.content.Context;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.qtec.router.asr.IAsrProvider;
import com.qtec.router.asr.IRecogCallback;
import com.qtec.router.asr.ISpeakCallback;
import com.qtec.router.asr.IWakeupCallback;
import com.qtec.speech.AsrManager;

/**
 * @author gongw
 * @date 2019/1/22
 */
@Route(path = IAsrProvider.PATH_ASR_SERVICE, name = "asr")
public class AsrRouteService implements IAsrProvider {
    @Override
    public void startRecognize(IRecogCallback callback) {
        AsrManager.startRecognize(new RecogListenerAdapter(callback));
    }

    @Override
    public void stopRecognize() {
        AsrManager.stopRecognize();
    }

    @Override
    public void startWakeup(IWakeupCallback callback) {
        AsrManager.startWakeup(new WakeupListenerAdapter(callback));
    }

    @Override
    public void stopWakeup() {
        AsrManager.stopWakeup();
    }

    @Override
    public void speakText(String text, ISpeakCallback callback) {
        AsrManager.speakText(text, new SpeechSynthesizerListenerAdapter(callback));
    }

    @Override
    public void release() {
        AsrManager.release();
    }

    @Override
    public void init(Context context) {
        AsrManager.initialize(context, "14889959", "7ALzvKGloG4wRP9dZoMaqqOu", "Lt6dSDAEMnBdOW2Gyl2bnPIGE0Qx0vp5");
    }
}
