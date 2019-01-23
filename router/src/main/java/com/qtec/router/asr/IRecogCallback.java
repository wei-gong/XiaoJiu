package com.qtec.router.asr;

/**
 * @author gongw
 * @date 2019/1/22
 */
public interface IRecogCallback {

    void onStart();

    void onEnd();

    void onResult(String result);

    void onError(int errorCode, String errorMsg);

}
