package cn.mmvtc.mobliesafe.chapter02;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.mmvtc.mobliesafe.R;

public class SetUp2Activity extends BaseSetUpActivity implements View.OnClickListener {
    private TelephonyManager mTelephonyManager;
    private Button mBindSIMBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        mTelephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        initView();//第二小圆点颜色设置为紫色
    }

    private void initView() {
        //第二个小圆点颜色
        ((RadioButton)findViewById(R.id.rb_second)).setChecked(true);
        mBindSIMBtn= (Button) findViewById(R.id.btn_bind_sim);
        mBindSIMBtn.setOnClickListener(this);
        if(isBind()){
            mBindSIMBtn.setEnabled(false);//设置按钮使用状态
        }else{
            mBindSIMBtn.setEnabled(true);
        }
    }
    //判断是否绑定SIM卡
    public boolean isBind() {
        String simString=sp.getString("sim",null);
        if(TextUtils.isEmpty(simString)){
            return false;
        }
        return true;
    }

    @Override
    public void showNext() {
    if(!isBind()){
        Toast.makeText(this, "您还没有绑定SIM卡", Toast.LENGTH_SHORT).show();
        return;
    }
        startActivityAndFinishSelf(SetUp3Activity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(SetUp1Activity.class);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bind_sim:
                //绑定SIM卡
                bindSIM();
                break;
        }
    }
    //绑定SIM卡
    private void bindSIM() {
        //获取sim卡序列号
        if(!isBind()){
            String simSerialNumber=mTelephonyManager.getSimSerialNumber();
            SharedPreferences.Editor edit=sp.edit();//放入编辑器
            //edit.putString("sim",simSerialNumber);//通过编辑器存放到sp里面
            edit.putString("sim","155648951245679884");//通过编辑器存放到sp里面
            edit.commit();//上传
            Toast.makeText(this, "SIM卡绑定成功", Toast.LENGTH_SHORT).show();
            mBindSIMBtn.setEnabled(false);

        }else{
            //已绑定，提醒用户
            Toast.makeText(this, "SIM卡已经绑定", Toast.LENGTH_SHORT).show();
            mBindSIMBtn.setEnabled(false);
        }
    }


}
