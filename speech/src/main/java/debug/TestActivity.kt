package debug

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.qtec.speech.R
import com.qtec.speech.ui.RecognitionActivity
import com.qtec.speech.vwk.IWakeupListener
import com.qtec.speech.vwk.MyWakeup
import com.qtec.speech.vwk.WakeUpResult
import kotlinx.android.synthetic.main.activity_test.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.AppSettingsDialog



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

    private var myWakeup:MyWakeup? = null

    private val permissions:Array<String> = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        supportWakeup.setOnCheckedChangeListener { _, p1 ->
            if(p1) {
                initWakeup()
            }else{
                myWakeup?.release()
            }
        }

        startRecognition.setOnClickListener {
            RecognitionActivity.startActivity(this@TestActivity)
            myWakeup?.stop()
        }
    }

    @AfterPermissionGranted(100)
    private fun initWakeup() {
        if(EasyPermissions.hasPermissions(this@TestActivity, *permissions)){
            myWakeup = MyWakeup(this, object:IWakeupListener{
                override fun onStop() {
                }

                override fun onError(errorCode: Int, errorMessge: String?, result: WakeUpResult?) {
                }

                override fun onASrAudio(data: ByteArray?, offset: Int, length: Int) {
                }

                override fun onSuccess(word: String?, result: WakeUpResult?) {
                    RecognitionActivity.startActivity(this@TestActivity)
                    myWakeup?.stop()
                }

            })
            myWakeup?.start()
        }else{
            EasyPermissions.requestPermissions(this@TestActivity, "使用语音识别需要这些权限", 100, *permissions)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this@TestActivity)
    }

//    private fun initSpeech(){
//        myWakeup = MyWakeup(this, RecogWakeupListener(handler))
//        myRecognizer = MyRecognizer(this, MessageStatusRecogListener(handler))
//
//        val initConfig = InitConfig(Constant.APP_ID, Constant.API_KEY, Constant.SECRET_KEY, TtsMode.MIX, getParams(), UiMessageListener(handler))
//        mySyntherizer = NonBlockSyntherizer(this, initConfig, handler)
//    }

//    protected fun getParams(): Map<String, String> {
//        val params = HashMap<String, String>()
//        // 以下参数均为选填
//        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
//        params[SpeechSynthesizer.PARAM_SPEAKER] = "0"
//        // 设置合成的音量，0-9 ，默认 5
//        params[SpeechSynthesizer.PARAM_VOLUME] = "9"
//        // 设置合成的语速，0-9 ，默认 5
//        params[SpeechSynthesizer.PARAM_SPEED] = "5"
//        // 设置合成的语调，0-9 ，默认 5
//        params[SpeechSynthesizer.PARAM_PITCH] = "5"
//
//        params[SpeechSynthesizer.PARAM_MIX_MODE] = SpeechSynthesizer.MIX_MODE_DEFAULT
//        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
//        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
//        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
//        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
//        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
//
//        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
//        val offlineResource = createOfflineResource( OfflineResource.VOICE_MALE)
//        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
//        params[SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE] = offlineResource!!.textFilename
//        params[SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE] = offlineResource.modelFilename
//        return params
//    }
//
//    protected fun createOfflineResource(voiceType: String): OfflineResource? {
//        var offlineResource: OfflineResource? = null
//        try {
//            offlineResource = OfflineResource(this, voiceType)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return offlineResource
//    }

}