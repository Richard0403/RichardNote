package me.richard.note.util;

import android.support.annotation.StringRes;
import android.widget.Toast;

import me.richard.note.PalmApp;

/**
 * Created by Richard on 2017/2/25. */
public class ToastUtils {

    private static Toast toast;

    public static void makeToast(String msg) {
        if (toast == null){
            toast = Toast.makeText(PalmApp.getContext(), msg, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(msg);
            toast.show();
        }
    }

    public static void makeToast(@StringRes int msgRes) {
        if (toast == null){
            toast = Toast.makeText(PalmApp.getContext(), msgRes, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(msgRes);
            toast.show();
        }
    }
}
