package com.qtec.speech.synthesize;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.baidu.tts.client.SpeechSynthesizer;
import com.qtec.common.utils.FileUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gongw
 * @date 2018/12/6
 */
public class SynthesizerParams {

    private String localPath = Environment.getExternalStorageDirectory() + "/SpeechParams";
    public Map<String, String> params = new HashMap<>();

    SynthesizerParams(Context context){
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);

        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        AssetManager assetManager = context.getAssets();
        FileUtil.makeDir(localPath);
        //文本模型文件路径
        FileUtil.copyFromAssets(assetManager, "bd_etts_text.dat", localPath + "/bd_etts_text.dat", true);
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, localPath + "/bd_etts_text.dat");
        //VOICE_MALE : bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat
        //VOICE_FEMALE : bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat
        //VOICE_DUXY : bd_etts_common_speech_yyjw_mand_eng_high_am-mix_v3.0.0_20170512.dat
        //VOICE_DUYY : bd_etts_common_speech_as_mand_eng_high_am_v3.0.0_20170516.dat
        FileUtil.copyFromAssets(assetManager, "bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat", localPath + "/bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat", false);
        //声学模型文件路径
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, localPath + "/bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat");
    }


}
