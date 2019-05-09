package cn.mmvtc.mobliesafe.chapter02.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class GPSLocationService extends Service {
   private LocationManager lm;
    private MyListener listener;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        lm= (LocationManager) getSystemService(LOCATION_SERVICE);
        listener=new MyListener();

        //criteria 查询条件
        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//获取准确的位置
        criteria.setCostAllowed(true);//是否允许付费
        String name=lm.getBestProvider(criteria,true);//最好位置的提供者
        System.out.println("最好位置的提供者："+name);
        lm.requestLocationUpdates(name,0,0,listener);

    }

    private class MyListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            StringBuilder sb = new StringBuilder();
            sb.append("accuracy:" + location.getAccuracy() + "\n");
            sb.append("speed:" + location.getSpeed() + "\n");
            sb.append("jingdu:" + location.getLongitude() + "\n");
            sb.append("weidu:" + location.getLatitude() + "\n");
            String result = sb.toString();
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            String safenumber = sp.getString("safephone", "");
            SmsManager.getDefault().sendTextMessage(safenumber, null, result, null, null);//给安全号码发送短信
            stopSelf();
        }

        //当前位置提供者状态发生变化时调用的 方法
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        //当某个位置提供者可以用的时候调用的方法
        @Override
        public void onProviderEnabled(String provider) {

            //当某个位置者不可用的时候调用的方法}
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
        public void onDestroy(){
        super.onDestroy();
            lm.removeUpdates(listener);
            listener=null;
        }
    }
