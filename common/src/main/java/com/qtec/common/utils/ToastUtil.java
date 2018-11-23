package com.qtec.common.utils;

import android.widget.Toast;
import com.qtec.common.base.BaseApplication;

/**
 * Toast Util class, use one toast object to show.
 * @author gongw
 * @date 2018/11/22
 */
public class ToastUtil {

    private static Toast toast = Toast.makeText(BaseApplication.getContext(), "", Toast.LENGTH_SHORT);

    public static void showShort(CharSequence message){
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showShort(int msgId){
        toast.setText(msgId);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLong(CharSequence message){
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showLong( int msgId){
        toast.setText(msgId);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static void hide(){
        toast.cancel();
    }

}
