package cn.mmvtc.mobliesafe.chapter02.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.mmvtc.mobliesafe.chapter02.App;

/**
 * Created by Administrator on 2019/4/4.
 */

public class BootCompleteReciever extends BroadcastReceiver {

    private  static final String TAG=BootCompleteReciever.class.getSimpleName();
    public void onReceive(Context context, Intent intent) {
        ((App)context.getApplicationContext()).correctSIM();
    }
}
