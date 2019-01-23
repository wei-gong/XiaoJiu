package com.qtec.speech.synthesize;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.qtec.common.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gongw
 * @date 2018/12/6
 */
public class MySynthesizer {

    private static final String TAG = MySynthesizer.class.getSimpleName();
    private SpeechSynthesizer mSpeechSynthesizer;
    private ChainSynthesizerListener chainSynthesizerListener;
    private static volatile boolean isInitialized = false;
    private SynthesizerParams synthesizerParams;
    private Context applicationContext;
    private SharedPreferences paramsPreferences;

    public MySynthesizer(Context context, String appId, String apiKey, String secretKey){
        if(isInitialized){
            LogUtil.e(TAG, "Already holds a SpeechSynthesizer, release it firstly.");
            throw new RuntimeException("Already holds a SpeechSynthesizer, release it firstly.");
        }
        LogUtil.i(TAG, "start init speech synthesizer.");
        applicationContext = context.getApplicationContext();
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(applicationContext);
        chainSynthesizerListener = new ChainSynthesizerListener();
        mSpeechSynthesizer.setSpeechSynthesizerListener(chainSynthesizerListener);
        //设置语音开发者平台上注册应得到的App ID ,AppKey ，Secret Key
        mSpeechSynthesizer.setAppId(appId);
        mSpeechSynthesizer.setApiKey(apiKey, secretKey);
        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。选择纯在线可以不必调用auth方法。
        AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
        if (!authInfo.isSuccess()) {
            // 离线授权需要网站上的应用填写包名
            LogUtil.e(TAG, "speech synthesizer init failed at auth(ttsMode)");
            return;
        }
        //init params here
        paramsPreferences = applicationContext.getSharedPreferences("synthesizer_params", Context.MODE_PRIVATE);
        loadParams();
        int result = mSpeechSynthesizer.initTts(TtsMode.MIX);
        if(result != 0){
            LogUtil.e(TAG, "speech synthesizer init failed at initTts(ttsMode), errorCode is " + result);
            return;
        }
        isInitialized = true;
        LogUtil.i(TAG, "speech synthesizer initialized success.");
    }

    public void resetParams(){
        SharedPreferences.Editor editor = paramsPreferences.edit();
        synthesizerParams.params.clear();
        synthesizerParams = new SynthesizerParams(applicationContext);
        for(String key : synthesizerParams.params.keySet()){
            editor.putString(key, synthesizerParams.params.get(key));
        }
        editor.apply();
        setParams(synthesizerParams.params);
    }

    public void loadParams(){
        SharedPreferences.Editor editor = paramsPreferences.edit();
        if(synthesizerParams == null){
            synthesizerParams = new SynthesizerParams(applicationContext);
        }
        for(String key : synthesizerParams.params.keySet()){
            String value = paramsPreferences.getString(key, null);
            if(value != null){
                synthesizerParams.params.put(key, value);
            }else{
                editor.putString(key, synthesizerParams.params.get(key));
            }
        }
        editor.apply();
        setParams(synthesizerParams.params);
    }

    public void setParams(Map<String, String> params){
        if (params != null) {
            for (Map.Entry<String, String> e : params.entrySet()) {
                mSpeechSynthesizer.setParam(e.getKey(), e.getValue());
            }
        }
    }

    /**
     * 只合成不播放
     * @param text 小于1024 GBK字节，即512个汉字或者字母数字
     * @return
     */
    public int synthesize(String text) {
        LogUtil.i(TAG, "synthesize text: " + text);
        return mSpeechSynthesizer.synthesize(text);
    }

    /**
     * 合成并播放
     * @param text 小于1024 GBK字节，即512个汉字或者字母数字
     * @return
     */
    public int speak(String text){
        LogUtil.i(TAG, "speak text: " + text);
        return mSpeechSynthesizer.speak(text);
    }

    public int batchSpeak(List<Pair<String, String>> texts) {
        List<SpeechSynthesizeBag> bags = new ArrayList<>();
        for (Pair<String, String> pair : texts) {
            SpeechSynthesizeBag speechSynthesizeBag = new SpeechSynthesizeBag();
            speechSynthesizeBag.setText(pair.first);
            if (pair.second != null) {
                speechSynthesizeBag.setUtteranceId(pair.second);
            }
            bags.add(speechSynthesizeBag);
        }
        return mSpeechSynthesizer.batchSpeak(bags);
    }

    public int pause() {
        LogUtil.i(TAG, "speech synthesizer pause");
        return mSpeechSynthesizer.pause();
    }

    public int resume() {
        LogUtil.i(TAG, "speech synthesizer resume");
        return mSpeechSynthesizer.resume();
    }

    public int stop() {
        LogUtil.i(TAG, "speech synthesizer stop");
        return mSpeechSynthesizer.stop();
    }

    /**
     * 引擎在合成时该方法不能调用！！！
     * 注意 只有 TtsMode.MIX 才可以切换离线发音
     *
     * @return
     */
    public int setSpeechModel(String modelFilename, String textFilename) {
        int res  = mSpeechSynthesizer.loadModel(modelFilename, textFilename);
        LogUtil.i(TAG, "switch another speaker: " + modelFilename);
        return res;
    }

    /**
     * 设置播放音量，默认已经是最大声音
     * 0.0f为最小音量，1.0f为最大音量
     *
     * @param leftVolume  [0-1] 默认1.0f
     * @param rightVolume [0-1] 默认1.0f
     */
    public void setStereoVolume(float leftVolume, float rightVolume) {
        mSpeechSynthesizer.setStereoVolume(leftVolume, rightVolume);
    }

    public void addSynthesizerListener(SpeechSynthesizerListener listener){
        chainSynthesizerListener.addListener(listener);
    }

    public void removeSynthesizerListener(SpeechSynthesizerListener listener){
        chainSynthesizerListener.removeListener(listener);
    }

    public void release() {
        mSpeechSynthesizer.stop();
        mSpeechSynthesizer.release();
        mSpeechSynthesizer = null;
        applicationContext = null;
        isInitialized = false;
        LogUtil.i(TAG, "speech synthesizer release");
    }


}
