package org.pjsip.mypj;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.im.qtec.db.Contact;
import com.im.qtec.db.RecentCall;
import com.im.qtec.entity.CallKeyValues;
import com.im.qtec.entity.KeysEntity;
import com.im.qtec.entity.KeysUASEntity;
import com.im.qtec.service.BaseCallService;
import com.im.qtec.sip.LoadHttpKeyTask;
import com.im.qtec.sip.OnLoadListener;
import com.im.qtec.utils.L;
import com.im.qtec.utils.UrlHelper;

import org.greenrobot.eventbus.EventBus;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallSdpCreatedParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.VideoPreview;
import org.pjsip.pjsua2.VideoWindow;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua2;
import org.pjsip.pjsua2.pjsua_call_media_status;

import java.util.List;

public class MyCall extends Call {
    private static final String TAG = "MyCall";
    public VideoWindow vidWin;
    public VideoPreview vidPrev;
    private String  phone;
    public MyCall(MyAccount acc, int call_id) {
        super(acc, call_id);
        vidWin = null;
    }

    /**
     * @param prm pjsip_inv_state.PJSIP_INV_STATE_CALLING
     *            接听是下面四个，拨打是总共5个，就那个PJSIP_INV_STATE_INCOMING不输出
     *            pjsip_inv_state.PJSIP_INV_STATE_EARLY
     *            pjsip_inv_state.PJSIP_INV_STATE_CONNECTING  //连接
     *            pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED //被确认，也就是被接受
     *            pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED //断开电话，拒接，或者没接
     */
    //通话的状态发生变化的地方
    @Override
    public void onCallState(OnCallStateParam prm) {
        MyApp.observer.notifyCallState(this);
    }


    @Override
    public void onCallSdpCreated(OnCallSdpCreatedParam prm) {
        super.onCallSdpCreated(prm);
        L.e(TAG, "========onCallSdpCreated  线程========》" + Thread.currentThread().getName());
        if (TextUtils.isEmpty(prm.getRemSdp().getWholeSdp())) {
            if (KeysEntity.getInstance().isEncrypt()) {
                L.e(TAG, "nova========打加密电话id========" + KeysEntity.getInstance().getBookid());
                setKeyId(prm, KeysEntity.getInstance().getBookid());
            }
        } else {
            KeysUASEntity.getInstance().setBookid(getKeyId(prm));
            L.e(TAG, "接加密电话id ==11==========" + KeysUASEntity.getInstance().getBookid());
        }
    }

    @Override
    public void onCallMediaState(OnCallMediaStateParam prm) {
        CallInfo ci;
        try {
            ci = getInfo();
        } catch (Exception e) {
            return;
        }

        CallMediaInfoVector cmiv = ci.getMedia();

        for (int i = 0; i < cmiv.size(); i++) {
            CallMediaInfo cmi = cmiv.get(i);
            if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO &&
                    (cmi.getStatus() ==
                            pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE ||
                            cmi.getStatus() ==
                                    pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)) {
                // unfortunately, on Java too, the returned Media cannot be
                // downcasted to AudioMedia
                Media m = getMedia(i);
                AudioMedia am = AudioMedia.typecastFromMedia(m);
                // connect ports
                try {
                    MyApp.ep.audDevManager().getCaptureDevMedia().
                            startTransmit(am);
                    am.startTransmit(MyApp.ep.audDevManager().
                            getPlaybackDevMedia());
                } catch (Exception e) {
                    continue;
                }
            } else if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_VIDEO &&
                    cmi.getStatus() ==
                            pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE &&
                    cmi.getVideoIncomingWindowId() != pjsua2.INVALID_ID) {
                vidWin = new VideoWindow(cmi.getVideoIncomingWindowId());
                vidPrev = new VideoPreview(cmi.getVideoCapDev());
            }
        }

        MyApp.observer.notifyCallMediaState(this);
    }

    public boolean makeCall(String phone) {
        try {
            this.phone = phone;
            String buddy_uri = "sip:" + this.phone + "@" + UrlHelper.SIP_IP;
            CallOpParam prm = new CallOpParam(true);
            makeCall(buddy_uri, prm);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getPhone() {
        return phone;
    }

    public boolean hangup(pjsip_status_code status_code) {
        try {
            CallOpParam callOpParamHangup = new CallOpParam();
            callOpParamHangup.setStatusCode(status_code);
            hangup(callOpParamHangup);
            L.e(TAG,"=======》挂断电话成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            L.e(TAG,"挂断电话失败======》"+e);
            //挂断电话异常后页面也需要正常关闭处理
            return true;
        }
        //return false;
    }

    public boolean answer(pjsip_status_code status_code) {
        try {
            CallOpParam prm = new CallOpParam();
            prm.setStatusCode(status_code);
            answer(prm);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}