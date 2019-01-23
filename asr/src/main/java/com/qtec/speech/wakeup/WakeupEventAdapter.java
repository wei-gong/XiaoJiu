package com.qtec.speech.wakeup;

import com.baidu.speech.EventListener;
import com.baidu.speech.asr.SpeechConstant;
import com.qtec.common.utils.LogUtil;

/**
 * @author gongw
 * @date 2018/12/6
 */

public class WakeupEventAdapter implements EventListener {

    private static final String TAG = WakeupEventAdapter.class.getSimpleName();
    private IWakeupListener listener;

    public WakeupEventAdapter(IWakeupListener listener) {
        this.listener = listener;
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        WakeUpResult result = WakeUpResult.parseJson(name, params);
        LogUtil.i(TAG, "wakeup name:" + name + "; params:" + params);
        // 识别唤醒词成功
        if (SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS.equals(name)) {
            int errorCode = result.getErrorCode();
            // error不为0依旧有可能是异常情况
            if (result.hasError()) {
                listener.onError(errorCode, "", result);
            } else {
                String word = result.getWord();
                listener.onSuccess(word, result);
            }
        } else if (SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR.equals(name)) {
            // 识别唤醒词报错
            int errorCode = result.getErrorCode();
            if (result.hasError()) {
                listener.onError(errorCode, "", result);
            }
        } else if (SpeechConstant.CALLBACK_EVENT_WAKEUP_STOPED.equals(name)) {
            // 关闭唤醒词
            listener.onStop();
        } else if (SpeechConstant.CALLBACK_EVENT_WAKEUP_AUDIO.equals(name)) {
            // 音频回调
            listener.onASrAudio(data, offset, length);
        }
    }


}
