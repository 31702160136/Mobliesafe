package cn.mmvtc.mobliesafe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import cn.mmvtc.mobliesafe.R;
import cn.mmvtc.mobliesafe.chapter01.utils.MyUtils;
import cn.mmvtc.mobliesafe.chapter01.utils.VersionUpdateUtils;

public class SplashActivity extends Activity {
    private TextView mVersionTV;
    private String mVersion;//本地版本号
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去标题在加载页面前使用
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        //初始化控件
        mVersion= MyUtils.getVersion(getApplicationContext());
        mVersionTV= (TextView) findViewById(R.id.tv_splash_version);
        mVersionTV.setText("版本号"+mVersion);//本地版本号
        final VersionUpdateUtils updateUtils=new VersionUpdateUtils
                (mVersion,this);
        new Thread(){
            public void run(){
                //获取服务器版本号
                updateUtils.getCloudVersion();
            }
        }.start();
    }
}
