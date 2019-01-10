package com.qtec.speech.recog;

import android.content.Context;
import android.content.SharedPreferences;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.qtec.common.utils.LogUtil;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gongw
 * @date 2018/12/6
 */
public class MyRecognizer {

    private static final String TAG = MyRecognizer.class.getSimpleName();
    private EventManager recogEventManager;
    private ChainRecogListener chainRecogListener;
    private RecogEventAdapter recogEventAdapter;
    private RecogParams recogParams;
    private SharedPreferences paramsPreferences;
    private volatile boolean isOfflineEngineLoaded;
    private static volatile boolean isInitialized;

    public MyRecognizer(Context context){
        if(isInitialized){
            LogUtil.e(TAG, "Already holds a Recognize EventManager, release it firstly.");
            throw new RuntimeException("Already holds a Recognize EventManager, release it firstly.");
        }
        LogUtil.i(TAG, "start init recognizer.");
        Context applicationContext = context.getApplicationContext();
        recogEventManager = EventManagerFactory.create(context, "asr");
        chainRecogListener = new ChainRecogListener();
        recogEventAdapter = new RecogEventAdapter(chainRecogListener);
        recogEventManager.registerListener(recogEventAdapter);
        //init params here
        paramsPreferences = applicationContext.getSharedPreferences("recognizer_params", Context.MODE_PRIVATE);
        loadParams();
        loadOfflineEngine();
        isInitialized = true;
        LogUtil.i(TAG, "recognizer initialized success.");
    }

    public void resetParams(){
        SharedPreferences.Editor editor = paramsPreferences.edit();
        recogParams.params.clear();
        recogParams = new RecogParams();
        for(String key : recogParams.params.keySet()){
            editor.putString(key, (String) recogParams.params.get(key));
        }
        editor.apply();
    }

    public void loadParams(){
        SharedPreferences.Editor editor = paramsPreferences.edit();
        if(recogParams == null){
            recogParams = new RecogParams();
        }
        for(String key : recogParams.params.keySet()){
            String value = paramsPreferences.getString(key, null);
            if(value != null){
                recogParams.params.put(key, value);
            }else{
                editor.putString(key, recogParams.params.get(key).toString());
            }
        }
        editor.apply();
    }

    /**
     * 离线命令词，在线不需要调用
     */
    public void loadOfflineEngine() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(SpeechConstant.DECODER, 2);
        map.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets:///baidu_speech_grammar.bsg");
        String json = new JSONObject(map).toString();
        // 加载离线命令词
        // 没有ASR_KWS_LOAD_ENGINE这个回调表示失败，如缺少第一次联网时下载的正式授权文件。
        recogEventManager.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, json, null, 0, 0);
        isOfflineEngineLoaded = true;
    }

    public void loadSlotDataForNlu(JSONObject jsonObject){
        recogParams.params.put(SpeechConstant.SLOT_DATA, jsonObject.toString());
    }

    public void loadSlotDataForRecog(JSONObject jsonObject){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(SpeechConstant.DECODER, 2);
        map.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets:///baidu_speech_grammar.bsg");
        map.put(SpeechConstant.SLOT_DATA, jsonObject.toString());
        String json = new JSONObject(map).toString();
        // 加载离线命令词
        // 没有ASR_KWS_LOAD_ENGINE这个回调表示失败，如缺少第一次联网时下载的正式授权文件。
        recogEventManager.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, json, null, 0, 0);
        isOfflineEngineLoaded = true;
    }

    public void start() {
        String json = new JSONObject(recogParams.params).toString();
        recogEventManager.send(SpeechConstant.ASR_START, json, null, 0, 0);
        LogUtil.i(TAG, "start recognizing");
    }

    /**
     * 提前结束录音等待识别结果。
     */
    public void stop() {
        recogEventManager.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
        LogUtil.i(TAG, "stop recognizing");
    }

    /**
     * 取消本次识别，取消后将立即停止不会返回识别结果。
     * cancel 与stop的区别是 cancel在stop的基础上，完全停止整个识别流程，
     */
    public void cancel() {
        LogUtil.i(TAG, "cancel recognize");
        recogEventManager.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
    }


    public void addRecogListener(IRecogListener listener){
        chainRecogListener.addListener(listener);
    }

    public void removeRecogListener(IRecogListener listener){
        chainRecogListener.removeListener(listener);
    }

    public void release() {
        cancel();
        if (isOfflineEngineLoaded) {
            // 如果之前有调用过加载离线命令词，这里要对应释放
            recogEventManager.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0);
            isOfflineEngineLoaded = false;
        }
        recogEventManager.unregisterListener(recogEventAdapter);
        recogEventManager = null;
        isInitialized = false;
        LogUtil.i(TAG, "recognizer release");
    }


}
