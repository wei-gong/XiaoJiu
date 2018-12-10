package com.qtec.speech.recog;

import android.os.Environment;
import com.baidu.speech.asr.SpeechConstant;
import com.qtec.speech.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 语音识别相关设置参数，详细参考http://ai.baidu.com/docs#/ASR-Android-SDK/top
 * @author gongw
 * @date 2018/12/6
 */
public class RecogParams {

    private String localPath = Environment.getExternalStorageDirectory() + "/SpeechParams";
    public Map<String, Object> params = new HashMap<>();

    RecogParams(){

        //设置离在线识别策略，0表示禁用离线功能 2表示启用离线功能，但是强制在线优先
        //离线时只能识别bsg文件中预定义的固定短语，
        params.put(SpeechConstant.DECODER, 2);
        //用于支持本地语义解析的bsg文件，离线和在线都可使用。NLU开启生效，其它说明见NLU参数。注意bsg文件同时也用于ASR_KWS_LOAD_ENGINE离线命令词功能。
        //String：文件路径
        //支持assets路径
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH,"assets:///baidu_speech_grammar.bsg");
        //与ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH参数一起使用后生效。 用于代码中动态扩展本地语义bsg文件的词条部分（bsg文件下载页面的左侧表格部分）
        //String（JSON格式）
        JSONObject json = new JSONObject();
        try {
            json.put("name", new JSONArray().put("妈妈").put("老伍"))
                    .put("appname", new JSONArray().put("手百").put("度秘"));
            params.put(SpeechConstant.SLOT_DATA, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //	本地语义解析设置。必须设置ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH参数生效，无论网络状况，都可以有本地语义结果。并且本地语义结果会覆盖在线语义结果。本参数不控制在线语义输出，需要在线语义输出见PID参数
        //disable 禁用
        //enable 启用
        //enable-all 在enable的基础上，临时结果也会做本地语义解析
        params.put(SpeechConstant.NLU, "enable");
        //
        params.put(SpeechConstant.PID, "1536");
        //语音活动检测， 根据静音时长自动断句。注意不开启长语音的情况下，SDK只能录制60s音频。静音时长及长语音请设置VAD_ENDPOINT_TIMEOUT参数
        //VAD_DNN 新一代VAD，各方面信息优秀，推荐使用。
        //VAD_TOUCH 关闭语音活动检测。注意关闭后不要开启长语音。适合用户自行控制音频结束，如按住说话松手停止的场景。功能等同于60s限制的长语音。需要手动调用ASR_STOP停止录音
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_TOUCH);
        //静音超时断句及长语音
        //0 开启长语音。即无静音超时断句。手动调用ASR_STOP停止录音。 请勿和VAD=touch联用！
        //>0（毫秒) 不开启长语音。开启VAD尾点检测，即静音判断的毫秒数。建议设置800ms-3000ms
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 800);
        //该参数支持设置为：文件系统路径，如：/sdcard/test/test.pcm；
        //java资源路径，如：res:///com/baidu.test/16k_test.pcm；录音文件不要超过3分钟
        //数据源方法全名，格式如：”#com.test.Factory.create16KInputStream()”（解释：Factory类中存在一个返回InputStream的方法create16kInputStream()），注意：必须以井号开始；方法原型必须为：public static InputStream yourMethod()。 超过3分钟的录音文件，请在每次read中sleep，避免SDK内部缓冲不够。
        //String：文件路径 资源路径或回调方法名
//        params.put(SpeechConstant.IN_FILE, "");
        //保存识别过程产生的录音文件, 该参数需要开启ACCEPT_AUDIO_DATA后生效,
        //String ：文件路径
//        params.put(SpeechConstant.OUT_FILE, "");
        //录音开始的时间点。用于唤醒+识别连续说。SDK有15s的录音缓存。如设置为(System.currentTimeMillis() - 1500),表示回溯1.5s的音频。
        //int：毫秒
//        params.put(SpeechConstant.AUDIO_MILLS, );
        //在选择PID为长句（输入法模式）的时候，是否禁用标点符号
        //true	禁用标点
        //false	不禁用标点，无法使用本地语义
        params.put(SpeechConstant.DISABLE_PUNCTUATION, false);
        //是否需要语音音频数据回调，开启后有CALLBACK_EVENT_ASR_AUDIO事件
        //true	需要音频数据回调
        //false	不需要音频数据回调
        params.put(SpeechConstant.ACCEPT_AUDIO_DATA, false);
        //是否需要语音音量数据回调，开启后有CALLBACK_EVENT_ASR_VOLUME事件回调
        //true 	需要音量数据回调
        //false	不需要音量数据回调
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, true);
        //说话开始的提示音 int：资源ID
        params.put(SpeechConstant.SOUND_START, R.raw.bdspeech_recognition_start);
        //说话结束的提示音 int：资源ID
        params.put(SpeechConstant.SOUND_END, R.raw.bdspeech_speech_end);
        //识别成功的提示音 int：资源ID
        params.put(SpeechConstant.SOUND_SUCCESS, R.raw.bdspeech_recognition_success);
        //识别出错的提示音 int：资源ID
        params.put(SpeechConstant.SOUND_ERROR, R.raw.bdspeech_recognition_error);
        //识别取消的提示音 int：资源ID
        params.put(SpeechConstant.SOUND_CANCEL, R.raw.bdspeech_recognition_cancel);

    }


}
