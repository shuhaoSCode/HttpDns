package com.shuhao.dns;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by luke on 2017/9/13.
 */

public class test {
    public static void  getToast(Object msg, Context context) {
        Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show();
    }
}
