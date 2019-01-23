package com.qtec.speech.wakeup;

import com.baidu.speech.asr.SpeechConstant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gongw
 * @date 2018/12/6
 */
public class WakeupParams {

    public Map<String, String> params = new HashMap<>();

    WakeupParams(){
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        // params.put(SpeechConstant.ACCEPT_AUDIO_DATA,true);
        // params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME,true);
        // params.put(SpeechConstant.IN_FILE,"res:///com/baidu/android/voicedemo/wakeup.pcm");
    }


}
