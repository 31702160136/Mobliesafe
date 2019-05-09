package cn.mmvtc.mobliesafe.chapter02;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2019/4/4.
 */

public class App extends Application {
    public void onCreate(){
        super.onCreate();
        correctSIM();
    }

    public void correctSIM() {
        //检查sim卡是否发生变化
        SharedPreferences sp=getSharedPreferences("config", Context.MODE_PRIVATE);
        //获取防盗保护的状态
        boolean protecting=sp.getBoolean("protecting",true);
        if(protecting)
        {
            //得到绑定的SIM卡串号
            String bindsim=sp.getString("sim","");

            //得到手机现在的sim卡串号
            TelephonyManager tm= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //为了测试在手机序列号上打发已模拟SIM卡背更换的情况
            String realsim=tm.getSimSerialNumber();//+"a"加上字符将sim卡号改变进行重启，重启后检查sim卡号记住恢复原样
            if(bindsim.equals(realsim)){
                Log.i("","sim卡未发生变化，还是您的手机");
            }else{
                Log.i("","SIM卡变化了");
                //由于系统版本的原因这里发短信可能与其他手机版本不兼容
                String safenumber=sp.getString("safephone","");
                if (!TextUtils.isEmpty(safenumber)){
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(safenumber,null,"您的亲友手机SIM卡已经被更换",null,null);
                }
            }
        }


    }

}
