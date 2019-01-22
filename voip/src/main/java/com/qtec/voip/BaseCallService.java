package com.qtec.voip;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import org.pjsip.mypj.MyAccount;
import org.pjsip.mypj.MyApp;
import org.pjsip.mypj.MyAppObserver;
import org.pjsip.mypj.MyBuddy;
import org.pjsip.mypj.MyCall;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_role_e;
import org.pjsip.pjsua2.pjsip_status_code;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by nova 2018/2/4.
 * 打电话的地方。
 */
public class BaseCallService extends Service implements MyAppObserver, Handler.Callback {
    public final static String SIP_RESISTER_TAG = "Registration successful";
    private static final String TAG = "BaseCallService";
    public static final String TYPE = "call.services.start.type";
    public static final int START = 0;
    public static final int STOP = 1;

    private final Handler handler = new Handler(this);

    public static MyAccount account = null;
    public static MyApp app = null;
    //使用上一个lastCallId  是用于标记上一个电话挂断的消息。避免hangup了没有收到disConnection消息。
    private static int lastCallId = 0;
    private static MyCall currentCall = null;
    private static String lastRegStatus = "";
    //表示正在打电话不能进来。
//    public static boolean MAKEING_CALL = true;
    private static AtomicBoolean makingCall = new AtomicBoolean(false);
    private static AtomicInteger callTimes = new AtomicInteger(0);

    public class MSG_TYPE {
        final static int INCOMING_CALL = 1;
        final static int REG_STATE = 3;
        final static int BUDDY_STATE = 4;
        final static int CALL_MEDIA_STATE = 5;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        init();
    }

    private void init() {
        if (!SPUtils.getInstance().getBoolean(ConstantValues.LOGIN_SUCCESS, false)) {
            return;
        }
        if (account == null) {
            if (app == null) {
                app = new MyApp();
                app.init(this, getFilesDir().getAbsolutePath());
            }
            if (app.accList.size() == 0) {
                //用户配置信息
                AccountConfig accCfg = new AccountConfig();
                accCfg.setIdUri("sip:localhost");
                accCfg.getNatConfig().setIceEnabled(false);
                accCfg.getVideoConfig().setAutoTransmitOutgoing(false);
                accCfg.getVideoConfig().setAutoShowIncoming(false);
                account = app.addAcc(accCfg);
            } else {
                account = app.accList.get(0);
            }
        }
    }

    public MyAccount initAccount() {
        MyAccount account = null;
        if (!SPUtils.getInstance().getBoolean(ConstantValues.LOGIN_SUCCESS, false)) {
            return null;
        }
        if (account == null) {
            if (app == null) {
                app = new MyApp();
                app.init(this, getFilesDir().getAbsolutePath());
            }
            if (app.accList.size() == 0) {
                //用户配置信息
                AccountConfig accCfg = new AccountConfig();
                accCfg.setIdUri("sip:localhost");
                accCfg.getNatConfig().setIceEnabled(false);
                accCfg.getVideoConfig().setAutoTransmitOutgoing(false);
                accCfg.getVideoConfig().setAutoShowIncoming(false);
                account = app.addAcc(accCfg);
            } else {
                account = app.accList.get(0);
            }
        }
        return account;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        decodeIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void decodeIntent(Intent intent) {
        if (intent == null) {
            init();
            register();
            return;
        }

        int type = intent.getIntExtra(TYPE, START);
        if (type == STOP) {
            deleteAccount();
        } else {
            init();
            register();
        }
    }

    private void deleteAccount() {
        try {
            pjsipLoginOut();
            app.delAcc(account);
            account = null;
            stopSelf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register() {
        if (!SPUtils.getInstance().getBoolean(ConstantValues.LOGIN_SUCCESS, false)) {
            return;
        }
        if (!SIP_RESISTER_TAG.equals(lastRegStatus)) {
            SipRegisterUtils.register();
            return;
        }
        long lastRegisterTime = SPUtils.getInstance().getLong(ConstantValues.SIP_REGISTER_TIME, 0);
        if (System.currentTimeMillis() - lastRegisterTime > 40 * 1000) {
            lastRegStatus = "";
            SipRegisterUtils.register();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean handleMessage(Message m) {
        switch (m.what) {
            case MSG_TYPE.CALL_MEDIA_STATE:
                break;
            case MSG_TYPE.BUDDY_STATE:
                MyBuddy buddy = (MyBuddy) m.obj;
                int idx = account.buddyList.indexOf(buddy);
                break;
            case MSG_TYPE.REG_STATE:
                registerResult(m.obj);
                break;
            case MSG_TYPE.INCOMING_CALL:
                callInComming(m.obj);
                break;
        }
        return true;
    }

    private void registerResult(Object obj) {
        lastRegStatus = (String) obj;
        if (SIP_RESISTER_TAG.equals(lastRegStatus)) {
            SPUtils.getInstance().saveLong(ConstantValues.SIP_REGISTER_TIME, System.currentTimeMillis());
        } else {
            //注册失败就不断地循环注册。
            register();
        }
    }

    private void callInComming(Object obj) {
        /*if (currentCall != null){
            MyCall myCall = (MyCall) obj;
            myCall.answer(pjsip_status_code.PJSIP_SC_BUSY_HERE);
            return;
        }
        *//*currentCall = (MyCall) obj;
        currentCall.answer(pjsip_status_code.PJSIP_SC_RINGING);*//*

        MyCall call = (MyCall) obj;
        call.answer(pjsip_status_code.PJSIP_SC_RINGING);*/
        currentCall.answer(pjsip_status_code.PJSIP_SC_RINGING);
        final MyCall call = currentCall;
        String keyid = KeysUASEntity.getInstance().getBookid();
        L.e(TAG, "接电话的秘钥Id=======》" + keyid);
        if (!TextUtils.isEmpty(keyid)) {
            new LoadHttpKeyTask(new OnLoadListener() {
                @Override
                public void onSuccess(CallKeyValues values) {
//                    InCallActivity.actionCallComming(BaseCallService.this);
                    answerComming(call, values);
                }

                @Override
                public void onFailed(RuntimeException re) {
//                    InCallActivity.actionCallComming(BaseCallService.this);
                    answerComming(call, null);
                }
            }).execute(keyid);
        } else {
//            InCallActivity.actionCallComming(BaseCallService.this);
            answerComming(call, null);

        }
    }

    private void answerComming(MyCall call, CallKeyValues callKeyValues) {
       /* if (currentCall!=null){
            call.answer(pjsip_status_code.PJSIP_SC_BUSY_HERE);
            return;
        }*/
        //避免退出登录  造成还可以接到电话的
        if (!SPUtils.getInstance().getBoolean(ConstantValues.LOGIN_SUCCESS, false)) {
//            call.answer(pjsip_status_code.PJSIP_SC_BUSY_HERE);
            return;
        }
        try {
            //检测这个电话是不是还有效
            synchronized (BaseCallService.class) {
                if (call.getId() != currentCall.getId()) {
                    return;
                }
                CallInfo callInfo = call.getInfo();
                if (callInfo.getState().swigValue() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED.swigValue()) {
                    return;
                }
                if (lastCallId == callInfo.getId() || !call.isActive()) {
                    return;
                }

                if (callKeyValues != null) {
                    KeysUASEntity.getInstance().setBookid(callKeyValues.getBookid());
                    KeysUASEntity.getInstance().setValue(callKeyValues.getValue());
                } else {
                    KeysUASEntity.getInstance().clearKeysEntity();
                }
                InCallActivity.actionCallComming(BaseCallService.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyIncomingCall(OnIncomingCallParam param) {
        L.e(TAG, "有电话进来============================");
        if (!SPUtils.getInstance().getBoolean(ConstantValues.LOGIN_SUCCESS, false)) {
            return;
        }
        if (currentCall != null || makingCall.get()) {//当前的电话不为空
            hungUpCall(new MyCall(account, param.getCallId()));
            return;
        }
        synchronized (BaseCallService.class) {
            if (currentCall != null || makingCall.get()) {//当前的电话不为空
                hungUpCall(new MyCall(account, param.getCallId()));
                return;
            }
            MyCall call = new MyCall(account, param.getCallId());
            currentCall = call;
            KeysUASEntity.getInstance().clearKeysEntity();
            KeysEntity.getInstance().clearKeysEntity();
            Message m = Message.obtain(handler, MSG_TYPE.INCOMING_CALL, call);
            m.sendToTarget();
        }
    }

    private void hungUpCall(MyCall call) {
        call.answer(pjsip_status_code.PJSIP_SC_BUSY_HERE);
        call = null;
    }

    //注册的状态码  子线程运行  注册内部自己会开启线程
    @Override
    public void notifyRegState(pjsip_status_code code, String reason, int expiration) {
        String msg_str = "";
        if (expiration == 0)
            msg_str += "Unregistration";
        else
            msg_str += "Registration";

        if (code.swigValue() / 100 == 2)
            msg_str += " successful";
        else
            msg_str += " failed: " + reason;
        Message m = Message.obtain(handler, MSG_TYPE.REG_STATE, msg_str);
        m.sendToTarget();
        //FLog.getInstance().e(TAG,"注册信息============================》"+msg_str);
        L.e(TAG, "注册信息====》" + msg_str);
    }

    //电话的状态变化。主线程  子线程都有
    @Override
    public void notifyCallState(MyCall call) {

        try {
            CallInfo ci = call.getInfo();
            L.e(TAG, "======状态=========" + call.getId() + ",电话是否活略=" + call.isActive() + ",电话的状态=" + ci.getState());
            if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                lastCallId = call.getId();
                L.e(TAG, "当前的关闭的callId=" + lastCallId);
            }
            if (currentCall == null || call.getId() != currentCall.getId())
                return;
            synchronized (BaseCallService.class) {
                if (currentCall == null || call.getId() != currentCall.getId())
                    return;
                L.e(TAG, "=====》" + Thread.currentThread().getName() + ",角色=" + ci.getRole() + ",状态=" + ci.getState() + "," + ci.getLastStatusCode());
                CallEvent callEvent = new CallEvent(ci.getState().swigValue(), ci.getRole().swigValue(), ci.getRemoteUri());
                if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                      /* getCallId();
                if (ci.getLastStatusCode().swigValue() == pjsip_status_code.PJSIP_SC_NOT_FOUND.swigValue()
                           && ci.getRole().swigValue() == pjsip_role_e.PJSIP_ROLE_UAC.swigValue()
                           && callTimes.decrementAndGet()>=0){
                        MyCall newCall = new MyCall(account, -1);
                        if (!newCall.makeCall(call.getPhone())) {
                            throw new CallException(ExceptionType.CALL_FAILED, "呼叫电话失败");
                        }
                        currentCall = newCall;
                        L.e(TAG,"===============》不断的重复拨打");
                        return;
                    }*/
                    //saveRecentCallLog(ci, System.currentTimeMillis());
                    currentCall.clearKeyByAndroid();
                    getCallId();
                    currentCall = null;
                    callEvent.setLastCode(ci.getLastStatusCode().swigValue());
                    //当为480,486,400,404错误重连的时候，不存储通话记录
                    if(callEvent.getLastCode() != 480 && callEvent.getLastCode() != 486 && callEvent.getLastCode() != 400 && callEvent.getLastCode() != 404){
                        saveRecentCallLog(ci, System.currentTimeMillis());
                    }
                } else if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                    callTimes = new AtomicInteger(0);
                    if (KeysUASEntity.getInstance().isEncrypt() && ci.getRole() == pjsip_role_e.PJSIP_ROLE_UAS) {
                        String value = getValues(KeysUASEntity.getInstance().getValue());
                        call.setKeyByAndroid(value, value.length());
                    }
                    //发起方
                    if (KeysEntity.getInstance().isEncrypt() && ci.getRole() == pjsip_role_e.PJSIP_ROLE_UAC) {
                        String remoteKey = ci.getRemote_key();
                        L.e(TAG, "本地的keyId=====>" + KeysEntity.getInstance().getBookid() + "，远方的keyId=" + remoteKey);
                        if (KeysEntity.getInstance().getBookid().equals(remoteKey)) {
                            String value = getValues(KeysEntity.getInstance().getValue());
                            call.setKeyByAndroid(value, value.length());
                        } else {
                            KeysEntity.getInstance().clearKeysEntity();
                        }
                    }
                }
                EventBus.getDefault().post(callEvent);
                L.e(TAG, "======onCallStateOver==========");
            }
        } catch (Exception e) {
            L.e(TAG, "=========打电话的异常=======》" + e);
            currentCall = null;
        }
    }

    private String getValues(String encryptValue) {
        String phone = SPUtils.getInstance().getString(ConstantValues.USER_ACCOUNT, "");
//        //从扫描的二维码中获取
//        ScanValuesEntity valuesEntity = MemoryDataCache.getInstance().getScanValuesEntity(phone);
//        if (valuesEntity == null || valuesEntity.getEncryptkey() == null) {
//            return encryptValue;
//        }
//        return EncryptAES.decryptNative(valuesEntity.getEncryptkey().getValue(), encryptValue);

        //从根秘钥中获取
        RootKeyValues valuesEntity = MemoryDataCache.getInstance().getRootKeyValuesEntity(phone);
        if (valuesEntity == null ) {
            return encryptValue;
        }
        return EncryptAES.decryptNative(valuesEntity.getValue(), encryptValue);
    }

    //保存通话记录
    public static void saveRecentCallLog(CallInfo ci, long callTime) {
        RecentCall recentCall = new RecentCall();
        if (ci == null) {
            return;
        }
        String remoteUri = ci.getRemoteUri();
        String phoneNumber = "";
        recentCall.setRemoteUri(remoteUri);
        if (remoteUri.contains("@")) {
            phoneNumber = remoteUri.substring(remoteUri.indexOf(":") + 1, remoteUri.indexOf("@"));
            recentCall.setPhoneNumber(phoneNumber);
        } else {
            recentCall.setPhoneNumber("");
        }
        final String number = phoneNumber;
        recentCall.setCallTime(callTime);//callTime以挂断时间为准
        recentCall.setCallLength(ci.getConnectDuration().getSec());//getMsec有问题，应该是long变int丢值了，直接保存秒
        //  1:呼入的电话  2:呼出的电话  3:未接的电话
        if (ci.getRole() == pjsip_role_e.PJSIP_ROLE_UAS) {
            //0代表安全，1代表不安全
            if (KeysUASEntity.getInstance().isEncrypt()) {
                recentCall.setSafe(0);
            } else {
                recentCall.setSafe(1);
            }
            if (ci.getLastStatusCode() == pjsip_status_code.PJSIP_SC_REQUEST_TERMINATED) {
                recentCall.setCallType(3);
            } else {
                recentCall.setCallType(1);
            }
        } else {
            //0代表安全，1代表不安全
            if (KeysEntity.getInstance().isEncrypt()) {
                recentCall.setSafe(0);
            } else {
                recentCall.setSafe(1);
            }
            recentCall.setCallType(2);
        }
        MyApplication.dbPool.execute(new Runnable() {
            @Override
            public void run() {
                List<Contact> contacts = DataSupport.select("username").where("mobilephone = ?", number).find(Contact.class);
                if (contacts.size() != 0) {
                    recentCall.setName(contacts.get(0).getUsername());
                } else {
                    recentCall.setName("");
                }
                recentCall.save();
            }
        });
    }

    //主线程
    @Override
    public void notifyCallMediaState(MyCall call) {
        Message m = Message.obtain(handler, MSG_TYPE.CALL_MEDIA_STATE, null);
        m.sendToTarget();
    }

    @Override
    public void notifyBuddyState(MyBuddy buddy) {
        Message m = Message.obtain(handler, MSG_TYPE.BUDDY_STATE, buddy);
        m.sendToTarget();
    }

    public static int getLastCallId() {
        return lastCallId;
    }

    public static MyCall getCurrentCall() {
        return currentCall;
    }

    public static void clearCall() {
        BaseCallService.currentCall = null;
    }

    public static void setMakingCall(boolean makingCall) {
        BaseCallService.makingCall.set(makingCall);
    }

    // 打电话的方法。"sip:" + phone + "@" + UrlHelper.SIP_IP（Sip服务器地址）;
    public static boolean makeCall(BaseEntity<CallKeyValues> baseEntity, String phone) {
        if (currentCall != null && lastCallId != currentCall.getId() && currentCall.isActive()) {
            throw new CallException(ExceptionType.CALL_BUSY, "已有电话");
        }
        synchronized (BaseCallService.class) {
            if (currentCall != null && lastCallId != currentCall.getId() && currentCall.isActive()) {
                throw new CallException(ExceptionType.CALL_BUSY, "已有电话");
            }
            if (!makingCall.get()) {
                throw new CallException(ExceptionType.HANG_UP, "挂断电话");
            }
            currentCall = null;
            if(baseEntity != null){
                //这个一定要在前面，在通话前把所有秘钥归位。
                KeysUASEntity.getInstance().clearKeysEntity();
                if (baseEntity.isFlag()) {
                    CallKeyValues callKeyValues = baseEntity.getResData();
                    KeysEntity.getInstance().setBookid(callKeyValues.getBookid());
                    KeysEntity.getInstance().setValue(callKeyValues.getValue());
                } else {
                    KeysEntity.getInstance().clearKeysEntity();
                }
            }
            MyCall call = new MyCall(account, -1);
//            MyCall call = BaseCallService.currentCall;
            callTimes = new AtomicInteger(8);
            if (!call.makeCall(phone)) {
                BaseCallService.setMakingCall(false);
                throw new CallException(ExceptionType.CALL_FAILED, "呼叫电话失败");
            }
            BaseCallService.setMakingCall(false);
            currentCall = call;
            L.e(TAG, "呼出电话keyId====》" + KeysEntity.getInstance().getBookid());
        }
        L.e(TAG, "====拨打电话成功======》");
        return true;
    }

    public static boolean hangup() {
       /* AppIOThreadPool.getInstance().execute(() -> {
            synchronized (BaseCallService.class){
                if (currentCall != null) {
           boolean hanged = currentCall.hangup(pjsip_status_code.PJSIP_SC_DECLINE);
            getCallId();
        }
        currentCall = null;
            }
        });*/
        synchronized (BaseCallService.class) {
            if (currentCall == null) {
                return true;
            }
            if (currentCall.hangup(pjsip_status_code.PJSIP_SC_DECLINE)) {
                L.e(TAG, "挂断电话======3333333333333");
                getCallId();
                //currentCall = null;
                return true;
            }
            return false;
        }
    }

    private static void getCallId() {
        try {
            if (currentCall != null)
                lastCallId = currentCall.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callTimeOut() {
        deleteCall();
    }

    public static void netError() {
        if (currentCall == null) {
            return;
        }
        deleteCall();
    }

    static void deleteCall() {
        AppIOThreadPool.getInstance().execute(() -> {
            if (currentCall != null)
                currentCall.hangup(pjsip_status_code.PJSIP_SC_REQUEST_TIMEOUT);
            getCallId();
            currentCall = null;
        });

    }

    public static void answer() {
        AppIOThreadPool.getInstance().execute(() -> {
            try {
                L.e(TAG, "接通电话的情况====》" + currentCall.isActive() + "," + currentCall.getId() + "," + lastCallId);
                CallOpParam prm = new CallOpParam();
                prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
                if (!KeysUASEntity.getInstance().isEncrypt()) {
                    BaseCallService.currentCall.answer(prm);
                } else {
                    BaseCallService.currentCall.answerWithKeyId(prm, KeysUASEntity.getInstance().getBookid());
                }
            } catch (Exception e) {
                currentCall = null;
            }
        });
    }

    //在Application中启动电话的服务。
    public static void startBaseCallService(Context context, int type) {
        Intent serviceIntent = new Intent(context, BaseCallService.class);
        serviceIntent.putExtra(TYPE, type);
        context.startService(serviceIntent);
    }

    public static void startBaseCallServiceByPush(Context context) {
        startBaseCallService(context, START);
        lastRegStatus = "";
    }

    public static void pjsipLoginOut() {
        try {
            account.setRegistration(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
