package cn.mmvtc.mobliesafe;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import cn.mmvtc.mobliesafe.chapter01.adapter.HomeAdapter;
import cn.mmvtc.mobliesafe.chapter02.LostFindActivity;
import cn.mmvtc.mobliesafe.chapter02.dialog.InterPasswordDialog;
import cn.mmvtc.mobliesafe.chapter02.dialog.SetUpPasswordDialog;
import cn.mmvtc.mobliesafe.chapter03.SecurityPhoneActivity;

public class HomeActivity extends Activity {
    /**声明GridView 该控件类似ListView*/
    private GridView gv_home;
    //手机防盗密码的存储
    private SharedPreferences msharedPreferences;
    /**设备管理员*/
    private DevicePolicyManager policyManager;
    /**申请权限*/
    private ComponentName componentName;
    private long mExitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    //初始化布局
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        msharedPreferences=getSharedPreferences("config",MODE_PRIVATE);//实例化声明模式
        //初始化Gridview
        gv_home= (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));
        //设置条目的点击条件
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //parent代表gridView，view代表每个条目的view对象，position代表每个条目的位置
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://手机防盗
                        if(isSetUpPassword()){
                            //弹出输入密码对话框
                            showInterPswdDialog();
                        }else{
                            //弹出设置密码对话框
                            showSetUpPswDialog();
                        }
                       break;
                    case 1://点击通讯卫士f
                        startActivity(SecurityPhoneActivity.class);
                          break;
//                    case 2://软件管家
//                        startActivity(AppManagerActivity.class);
//                        break;
//                    case 3://手机杀毒
//                        startActivity(VirusScanActivity.class);
//                        break;
//                    case 4://缓存清理
//                        startActivity(CacheClearListActivity.class);
//                        break;
//                    case 5://进程管理
//                        startActivity(ProcessManagerActiviy.class);
//                        break;
//                    case 6://流量统计
//                        startActivity(TrafficMonitoringActivity.class);
//                        break;
//                    case 7://高级工具
//                        startActivity(AdvancedToolsActivity.class);
//                        break;
//                    case 8://设置中
//                        startActivity(SettingsActivity.class);
//                        break;
                }
            }
        });



    //1.获取设备管理员
    policyManager=(DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
    //2.申请权限，MyDeviceAdminReciever继承DeviceAdminReceiver
  //  ComponentName=new ComponentName(this,MyDeviceAdminReciever.class);
    //3.判断，如果没有权限则申请权限
    boolean active=policyManager.isAdminActive(componentName);
    if(!active){
        //没有管理员的权限，则获取管理员的权限
        Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"获取超级管理员权限，用于远程锁屏和清除数据");
        startActivity(intent);
    }
}






    /***
     * 弹出设置对话框
     */
    private void showSetUpPswDialog(){
        final SetUpPasswordDialog setUpPasswordDialog=new SetUpPasswordDialog(
                HomeActivity.this
        );
        setUpPasswordDialog.setCallBack(new SetUpPasswordDialog.MyCallBack() {
            @Override
            public void ok() {
                String firstPwsd= setUpPasswordDialog.mFirstPWDET.
                        getText().toString().trim();
                String affirmPwsd=setUpPasswordDialog.mFirstPWDET.
                        getText().toString().trim();
                if(!TextUtils.isEmpty(firstPwsd)&&!TextUtils.isEmpty(affirmPwsd))
                {
                    if(firstPwsd.equals(affirmPwsd))
                    {
                        //两次密码一致，存储密码
                        savePswd(affirmPwsd);
                        setUpPasswordDialog.dismiss();
                        //显示输入密码对话框
//                        showInterPswdDialog();

                    }else{
                        Toast.makeText(HomeActivity.this, "两次密码不一致",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void cancle() {
                setUpPasswordDialog.dismiss();
            }
        });
        setUpPasswordDialog.setCancelable(true);
        setUpPasswordDialog.show();
    }
    /**
     * 弹出输入密码对话框
     */
    private void showInterPswdDialog(){
        //获取密码
        final String password=getPassword();
        final InterPasswordDialog mInpswdDialog=
                new InterPasswordDialog(HomeActivity.this);
        //密码做对比
        mInpswdDialog.setCallBack(new InterPasswordDialog.MyCallBack() {
            @Override
            public void confirm() {
                if(TextUtils.isEmpty(mInpswdDialog.getPassword())){
                    Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }else if(password.equals(mInpswdDialog.getPassword())){
                    //进入防盗主界面
                    mInpswdDialog.dismiss();
                     startActivity(LostFindActivity.class);

                 }else{
                     Toast.makeText(HomeActivity.this, "密码不一致",
                     Toast.LENGTH_SHORT).show();
                  }
                 }
@Override
             public void cancle() {
                     mInpswdDialog.dismiss();
                 }
        });
                mInpswdDialog.setCancelable(true);
                mInpswdDialog.show();
        }

    /**
     * 保存密码
     * affirmPwsd
     *
     * */
    private void savePswd(String affirmPwsd){
        SharedPreferences.Editor edit=msharedPreferences.edit();
        //密码保存但为加密需要补充
        edit.putString("PhoneAntiTheftPWD",affirmPwsd);//MD5Utils.encode(affirmPwsd)
        edit.commit();
    }
    /**
     * 读取密码
     * SharedPreferences 存储的密码
     */
    private String getPassword()
    {
        String password=msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if(TextUtils.isEmpty(password))
        {
            return "";
        }
        return password;

    }
    /**
     * 判断用户 是否 设置过手机防盗密码
     */
    private boolean isSetUpPassword()
    {
        String password=msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if(TextUtils.isEmpty(password))
        {
            return false;
        }
        return true;
    }


    /**
     * 开启新的activity
     * cls
     */
    private void startActivity(Class<?> cls) {
        Intent intent=new Intent(getApplicationContext(), cls);//意图显示器
        startActivity(intent);
    }
    /**
     * 按两次返回键退出程序
     */
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis()-mExitTime)>2000){
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime=System.currentTimeMillis();
            }else{
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
