package org.pjsip.mypj;

import android.util.Log;

import com.im.qtec.utils.L;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.IpChangeParam;
import org.pjsip.pjsua2.JsonDocument;
import org.pjsip.pjsua2.LogConfig;
import org.pjsip.pjsua2.MediaConfig;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.UaConfig;
import org.pjsip.pjsua2.pj_log_decoration;
import org.pjsip.pjsua2.pjsip_transport_type_e;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by shishengjie on 2018/2/4.
 */

public class MyApp {
    public static final String TAG = "MyApp";
    //no need video ssj
    static {
        try {
            System.loadLibrary("openh264");
            // Ticket #1937: libyuv is now included as static lib
            //System.loadLibrary("yuv");
        } catch (UnsatisfiedLinkError e) {
            System.out.println("UnsatisfiedLinkError: " + e.getMessage());
            System.out.println("This could be safely ignored if you " +
                    "don't need video.");
        }
        try {
            System.loadLibrary("pjsua2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Library loaded");
    }

    public static Endpoint ep = new Endpoint();
    public static MyAppObserver observer;
    //存储MyAccount的集合。
    public ArrayList<MyAccount> accList = new ArrayList<MyAccount>();

    private ArrayList<MyAccountConfig> accCfgs =
            new ArrayList<MyAccountConfig>();
    private EpConfig epConfig = new EpConfig();
    private TransportConfig sipTpConfig = new TransportConfig();
    private String appDir;

    /* Maintain reference to log writer to avoid premature cleanup by GC */
    private MyLogWriter logWriter;

    private final String configName = "pjsua2.json";
    private final int SIP_PORT = 6000;
    private final int LOG_LEVEL = 9;

    public void init(MyAppObserver obs, String app_dir) {
        init(obs, app_dir, false);
    }
//设置观察
    public void init(MyAppObserver obs, String app_dir, boolean own_worker_thread) {
        observer = obs;
        appDir = app_dir;
	/* Create endpoint */
        try {
            ep.libCreate();
        } catch (Exception e) {
            return;
        }
	/* Load config */
        String configPath = appDir + "/" + configName;
        File f = new File(configPath);
        if (f.exists()) {
            loadConfig(configPath);
        } else {
        /* Set 'default' values */
            sipTpConfig.setPort(SIP_PORT);
        }

	/* Override log level setting */
        epConfig.getLogConfig().setLevel(LOG_LEVEL);
        epConfig.getLogConfig().setConsoleLevel(LOG_LEVEL);

	/* Set log config. */
        LogConfig log_cfg = epConfig.getLogConfig();
        logWriter = new MyLogWriter();
        log_cfg.setWriter(logWriter);
        log_cfg.setDecor(log_cfg.getDecor() & ~(pj_log_decoration.PJ_LOG_HAS_CR.swigValue() | pj_log_decoration.PJ_LOG_HAS_NEWLINE.swigValue()));

	/* Write log to file (just uncomment whenever needed) */
        //String log_path = android.os.Environment.getExternalStorageDirectory().toString();
        //log_cfg.setFilename(log_path + "/pjsip.log");

	/* Set ua config. */
        UaConfig ua_cfg = epConfig.getUaConfig();
        ua_cfg.setUserAgent("Pjsua2 Android " + ep.libVersion().getFull());

	/* STUN server. */
        //StringVector stun_servers = new StringVector();
        //stun_servers.add("stun.pjsip.org");
        //ua_cfg.setStunServer(stun_servers);

        //novatest
        epConfig.getMedConfig().setClockRate(8000L);
        epConfig.getMedConfig().setSndClockRate(8000L);
//        epConfig.getMedConfig().setQuality(2L);

	/* No worker thread */
        if (own_worker_thread) {
            ua_cfg.setThreadCnt(0);
            ua_cfg.setMainThreadOnly(true);
        }

	/* Init endpoint */
        try {
            ep.libInit(epConfig);
        } catch (Exception e) {
            return;
        }

	/* Create transports. */
        try {
            ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, sipTpConfig);
        } catch (Exception e) {
            System.out.println(e);
        }
//要去掉TCP的，要不有可能会有TCP的协议
//        try {
//            ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_TCP,
//                    sipTpConfig);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//
//        try {
//            sipTpConfig.setPort(SIP_PORT+1);
//            ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_TLS,
//                    sipTpConfig);
//        } catch (Exception e) {
//            System.out.println(e);
//        }

        /* Set SIP port back to default for JSON saved config */
        sipTpConfig.setPort(SIP_PORT);

	/* Create accounts. */
        for (int i = 0; i < accCfgs.size(); i++) {
            MyAccountConfig my_cfg = accCfgs.get(i);

	    /* Customize account config */
            my_cfg.accCfg.getNatConfig().setIceEnabled(false);
            my_cfg.accCfg.getVideoConfig().setAutoTransmitOutgoing(false);
            my_cfg.accCfg.getVideoConfig().setAutoShowIncoming(false);

            MyAccount acc = addAcc(my_cfg.accCfg);
            if (acc == null)
                continue;

	    /* Add Buddies */
            for (int j = 0; j < my_cfg.buddyCfgs.size(); j++) {
                BuddyConfig bud_cfg = my_cfg.buddyCfgs.get(j);
                acc.addBuddy(bud_cfg);
            }
        }
        //设置speex/8000/1 最高优先级
        try {
            ep.codecSetPriority("speex/8000/1", (short) 255);
        } catch (Exception e) {
            e.printStackTrace();
        }
	/* Start. */
        try {
            ep.libStart();
        } catch (Exception e) {
            return;
        }
    }

    //通过配置增加账号。
    public MyAccount addAcc(AccountConfig cfg) {
        MyAccount acc = new MyAccount(cfg);
        try {
            acc.create(cfg);
        } catch (Exception e) {
            acc = null;
            L.e(TAG,"=========创建账号失败====》"+e);
            return null;
        }

        accList.add(acc);
        return acc;
    }

    public void delAcc(MyAccount acc) {
        accList.remove(acc);
    }

    private void loadConfig(String filename) {
        JsonDocument json = new JsonDocument();

        try {
	    /* Load file */
            json.loadFile(filename);
            ContainerNode root = json.getRootContainer();

	    /* Read endpoint config */
            epConfig.readObject(root);

	    /* Read transport config */
            ContainerNode tp_node = root.readContainer("SipTransport");
            sipTpConfig.readObject(tp_node);

	    /* Read account configs */
            accCfgs.clear();
            ContainerNode accs_node = root.readArray("accounts");
            while (accs_node.hasUnread()) {
                MyAccountConfig acc_cfg = new MyAccountConfig();
                acc_cfg.readObject(accs_node);
                accCfgs.add(acc_cfg);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

	/* Force delete json now, as I found that Java somehow destroys it
	* after lib has been destroyed and from non-registered thread.
	*/
        json.delete();
    }

    private void buildAccConfigs() {
	/* Sync accCfgs from accList */
        accCfgs.clear();
        for (int i = 0; i < accList.size(); i++) {
            MyAccount acc = accList.get(i);
            MyAccountConfig my_acc_cfg = new MyAccountConfig();
            my_acc_cfg.accCfg = acc.cfg;

            my_acc_cfg.buddyCfgs.clear();
            for (int j = 0; j < acc.buddyList.size(); j++) {
                MyBuddy bud = acc.buddyList.get(j);
                my_acc_cfg.buddyCfgs.add(bud.cfg);
            }

            accCfgs.add(my_acc_cfg);
        }
    }

    //保存账户的配置信息。写入本地文本中
    private void saveConfig(String filename) {
        JsonDocument json = new JsonDocument();

        try {
	    /* Write endpoint config */
            json.writeObject(epConfig);

	    /* Write transport config */
            ContainerNode tp_node = json.writeNewContainer("SipTransport");
            sipTpConfig.writeObject(tp_node);

	    /* Write account configs */
	        //创建配置信息。也就是MyAccountConfig。
            buildAccConfigs();
            ContainerNode accs_node = json.writeNewArray("accounts");
            for (int i = 0; i < accCfgs.size(); i++) {
                accCfgs.get(i).writeObject(accs_node);
            }

	    /* Save file */
            json.saveFile(filename);
        } catch (Exception e) {
        }

	/* Force delete json now, as I found that Java somehow destroys it
	* after lib has been destroyed and from non-registered thread.
	*/
        json.delete();
    }

   /* public void handleNetworkChange() {
        try {
            Log.d(TAG, "nova  ===== handleNetworkChange: Network change detected");
            IpChangeParam changeParam = new IpChangeParam();
            ep.handleIpChange(changeParam);
        } catch (Exception e) {
            System.out.println(e);
        }
    }*/

    public void deinit() {
        String configPath = appDir + "/" + configName;
        saveConfig(configPath);

	/* Try force GC to avoid late destroy of PJ objects as they should be
	* deleted before lib is destroyed.
	*/
        Runtime.getRuntime().gc();

	/* Shutdown pjsua. Note that Endpoint destructor will also invoke
	* libDestroy(), so this will be a test of double libDestroy().
	*/
        try {
            ep.libDestroy();
        } catch (Exception e) {
        }

	/* Force delete Endpoint here, to avoid deletion from a non-
	* registered thread (by GC?).
	*/
        ep.delete();
        ep = null;
    }
}
