package debug

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.qtec.speech.AsrManager
import com.qtec.speech.R
import com.qtec.speech.wakeup.IWakeupListener
import com.qtec.speech.wakeup.WakeUpResult
import kotlinx.android.synthetic.main.activity_test.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 *
 * @author gongw
 * @date 2018/11/21
 */
class TestActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this@TestActivity, perms)) {
            AppSettingsDialog.Builder(this@TestActivity).setRationale("因为缺乏权限，一些功能将无法正常使用，您可以去设置页面修改权限").build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    private val permissions:Array<String> = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
//            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initAsr()
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
                        speakText()
                    }
                })
            }else{
                AsrManager.stopWakeup()
            }
        }
        startRecognition.setOnClickListener {
            speakText()
        }
    }

    @AfterPermissionGranted(100)
    private fun initAsr(){
        if(!EasyPermissions.hasPermissions(this@TestActivity, *permissions)){
            EasyPermissions.requestPermissions(this@TestActivity, "使用语音识别需要这些权限", 100, *permissions)
        }else{
            AsrManager.initialize(this@TestActivity, "14889959", "7ALzvKGloG4wRP9dZoMaqqOu", "Lt6dSDAEMnBdOW2Gyl2bnPIGE0Qx0vp5")
        }
    }

    @AfterPermissionGranted(200)
    private fun speakText() {
        if(!EasyPermissions.hasPermissions(this@TestActivity, *permissions)){
            EasyPermissions.requestPermissions(this@TestActivity, "使用语音识别需要这些权限", 200, *permissions)
        }else{
            initAsr()
            AsrManager.speakText("Hello World!")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this@TestActivity)
    }


    override fun onDestroy() {
        super.onDestroy()
        AsrManager.release()
    }

}