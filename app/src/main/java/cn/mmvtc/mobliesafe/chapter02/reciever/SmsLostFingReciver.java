package cn.mmvtc.mobliesafe.chapter02.reciever;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import cn.mmvtc.mobliesafe.R;

public class SmsLostFingReciver extends BroadcastReceiver {
    private static final String TAG=SmsLostFingReciver.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    public SmsLostFingReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences=context.getSharedPreferences("config",
                Activity.MODE_APPEND);
        boolean protecting=sharedPreferences.getBoolean("protecting",true);
        if(protecting){
            //防盗保护开启
            //获取超级管理员
            DevicePolicyManager dpm= (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            Object[]objs=(Object[])intent.getExtras().get("pdus");//获得短信数据
            for (Object obj:objs){//遍历
                SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) obj);
                String sender=smsMessage.getOriginatingAddress();//获得发送方号码
                String body =smsMessage.getMessageBody();//获得短信内容
                String safephone=sharedPreferences.getString("safephone",null);
                //如果该短信是安全号码发送的
                if(!TextUtils.isEmpty(safephone)&sender.equals(safephone)){
                    if("#*location*#".equals(body)){
                        Log.i(TAG,"返回位置信息.");
                        //获取位置
//                        Intent service=new Intent(context.GPSLocationService.class);
//                        context.startService(service);
                        abortBroadcast();
                    }else if("#*alarm*#".equals(body)){
                        Log.i(TAG,"播放报警音乐.");
                        MediaPlayer player=MediaPlayer.create(context, R.raw.ylzs);
                        player.setVolume(1.0f,1.0f);//音量大小左右声道
                        //player.setLooping(true); //循环播放音乐
                        player.start();//播放
                        abortBroadcast();
                    }else if("#*wipedata*#".equals(body)){
                        Log.i(TAG,"远程清除数据.");
                        dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                        abortBroadcast();

                    }else if("#*lockscreen*#".equals(body)){
                        Log.i(TAG,"远程锁屏");
                        dpm.resetPassword("123",0);//重置密码123
                        dpm.lockNow();//没有管理人员权限，调用时会崩溃  锁屏
                        abortBroadcast();
                    }
                }
            }
        }

    }
}
