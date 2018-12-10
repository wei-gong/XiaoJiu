package com.qtec.speech;

import android.content.Context;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.qtec.common.utils.LogUtil;
import com.qtec.speech.recog.IRecogListener;
import com.qtec.speech.recog.MyRecognizer;
import com.qtec.speech.synthesize.MySynthesizer;
import com.qtec.speech.wakeup.IWakeupListener;
import com.qtec.speech.wakeup.MyWakeup;

/**
 * @author gongw
 * @date 2018/12/6
 */
public class AsrManager {

    private static final String TAG = AsrManager.class.getSimpleName();
    private MyRecognizer recognizer;
    private MyWakeup wakeup;
    private MySynthesizer synthesizer;
    private static volatile boolean isInitialized = false;
    private static AsrManager instance;

    private AsrManager(){}


    public static void initialize(Context context, String appId, String apiKey, String secretKey){
        release();
        instance = new AsrManager();
        instance.recognizer = new MyRecognizer(context);
        instance.wakeup = new MyWakeup(context);
        instance.synthesizer = new MySynthesizer(context, appId, apiKey, secretKey);
    }

    public static boolean checkInitial(){
        if(instance == null){
            LogUtil.e(TAG, "Please invoke AsrManager.initialize() firstly!");
            return false;
        }else{
            return true;
        }
    }

    public static void startRecognize(IRecogListener listener){
        if(!checkInitial()){
            return;
        }
        instance.recognizer.addRecogListener(listener);
        instance.recognizer.start();
    }

    public static void startWakeup(IWakeupListener listener){
        if(!checkInitial()){
            return;
        }
        instance.wakeup.addWakeupListener(listener);
        instance.wakeup.start();
    }

    public static void speakText(String text){
        speakText(text, null);
    }

    public static void speakText(String text, SpeechSynthesizerListener listener){
        if(!checkInitial()){
            return;
        }
        instance.synthesizer.addSynthesizerListener(listener);
        instance.synthesizer.speak(text);
    }

    public static void stopRecognize(){
        if(!checkInitial()){
            return;
        }
        instance.recognizer.stop();
    }

    public static void stopWakeup(){
        if(!checkInitial()){
            return;
        }
        instance.wakeup.stop();
    }



    public static void release(){
        if(!checkInitial()){
            return;
        }
        if(instance.recognizer != null){
            instance.recognizer.release();
        }
        if(instance.wakeup != null){
            instance.wakeup.release();
        }
        if(instance.synthesizer != null){
            instance.synthesizer.release();
        }
        instance = null;
        isInitialized = false;
    }
}
