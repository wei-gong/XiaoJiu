package com.qtec.router.asr;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author gongw
 * @date 2019/1/18
 */
public interface IAsrProvider extends IProvider {

    String PATH_ASR_SERVICE = "/asr/service";

    void startRecognize(IRecogCallback callback);
    void stopRecognize();
    void startWakeup(IWakeupCallback callback);
    void stopWakeup();
    void speakText(String text, ISpeakCallback callback);
    void release();

}
