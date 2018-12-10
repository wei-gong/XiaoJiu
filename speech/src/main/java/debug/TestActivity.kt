package debug

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.qtec.speech.AsrManager
import com.qtec.speech.R
import com.qtec.speech.wakeup.IWakeupListener
import com.qtec.speech.wakeup.WakeUpResult
import kotlinx.android.synthetic.main.activity_test.*
import java.util.ArrayList

/**
 *
 * @author gongw
 * @date 2018/11/21
 */
class TestActivity : AppCompatActivity() {

//    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this@TestActivity, perms)) {
//            AppSettingsDialog.Builder(this@TestActivity).setRationale("因为缺乏权限，一些功能将无法正常使用，您可以去设置页面修改权限").build().show()
//        }
//    }
//
//    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
//    }

    private val permissions:Array<String> = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initPermission()
        AsrManager.initialize(this@TestActivity, "14889959", "7ALzvKGloG4wRP9dZoMaqqOu", "Lt6dSDAEMnBdOW2Gyl2bnPIGE0Qx0vp5 ")
        supportWakeup.setOnCheckedChangeListener { _, p1 ->
            if(p1) {
                AsrManager.startWakeup(object:IWakeupListener{
                    override fun onStop() {
                    }

                    override fun onError(errorCode: Int, errorMessge: String?, result: WakeUpResult?) {
                    }

                    override fun onASrAudio(data: ByteArray?, offset: Int, length: Int) {
                    }

                    override fun onSuccess(word: String?, result: WakeUpResult?) {
                        AsrManager.speakText("Hello World!")
                    }
                })
            }else{
                AsrManager.stopWakeup()
            }
        }
        startRecognition.setOnClickListener {
//            if(!EasyPermissions.hasPermissions(this@TestActivity, *permissions)){
//                EasyPermissions.requestPermissions(this@TestActivity, "使用语音识别需要这些权限", 200, *permissions)
//            }else{
                AsrManager.speakText("Hello World!")
//            }
        }
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private fun initPermission() {
        val permissions = arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE)

        val toApplyList = ArrayList<String>()

        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm)
                // 进入到这里代表没有权限.
            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toTypedArray(), 123)
        }

    }

//    @AfterPermissionGranted(200)
//    private fun speakText(text:String) {
//        AsrManager.singleInstance(this@TestActivity).speakText(text)
//    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this@TestActivity)
//    }


    override fun onDestroy() {
        super.onDestroy()
        AsrManager.release()
    }

}