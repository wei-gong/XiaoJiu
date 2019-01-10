package org.pjsip.mypj;

import android.util.Log;

import com.im.qtec.service.BaseCallService;
import com.im.qtec.utils.L;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnRegStateParam;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by nova on 2018/1/25.
 * 自己账号
 */
public class MyAccount extends Account {
    private final String TAG = getClass().getSimpleName();
    public ArrayList<MyBuddy> buddyList = new ArrayList<MyBuddy>();
    public AccountConfig cfg;
    private boolean isRegistering = false;
    public MyAccount(AccountConfig config) {
        super();
        cfg = config;
    }

    public MyBuddy addBuddy(BuddyConfig bud_cfg) {
    /* Create Buddy */
        MyBuddy bud = new MyBuddy(bud_cfg);
        try {
            bud.create(this, bud_cfg);
        } catch (Exception e) {
            bud.delete();
            bud = null;
        }

        if (bud != null) {
            buddyList.add(bud);
            if (bud_cfg.getSubscribe())
                try {
                    bud.subscribePresence(true);
                } catch (Exception e) {
                }
        }

        return bud;
    }

    public void delBuddy(MyBuddy buddy) {
        buddyList.remove(buddy);
        buddy.delete();
    }

    public void setRegistering(boolean registering) {
        isRegistering = registering;
    }

    public boolean isRegistering() {
        return isRegistering;
    }

    public void delBuddy(int index) {
        MyBuddy bud = buddyList.get(index);
        buddyList.remove(index);
        bud.delete();
    }

    @Override
    public void onRegState(OnRegStateParam prm) {
        setRegistering(false);
        MyApp.observer.notifyRegState(prm.getCode(), prm.getReason(), prm.getExpiration());
    }
    //有电话来的时候
    @Override
    public void onIncomingCall(OnIncomingCallParam prm) {
        L.e(TAG,"====电话进来的线程是====11111====》"+Thread.currentThread().getName());
        MyApp.observer.notifyIncomingCall(prm);
    }


    @Override
    public void onInstantMessage(OnInstantMessageParam prm) {
        System.out.println("======== Incoming pager ======== ");
        System.out.println("From     : " + prm.getFromUri());
        System.out.println("To       : " + prm.getToUri());
        System.out.println("Contact  : " + prm.getContactUri());
        System.out.println("Mimetype : " + prm.getContentType());
        System.out.println("Body     : " + prm.getMsgBody());
    }
}