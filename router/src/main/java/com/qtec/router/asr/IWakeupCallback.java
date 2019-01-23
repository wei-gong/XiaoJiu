package com.qtec.router.asr;

/**
 * @author gongw
 * @date 2019/1/22
 */
public interface IWakeupCallback {

    void onSuccess(String word);

    void onStop();

    void onError(int errorCode, String errorMsg);

}
