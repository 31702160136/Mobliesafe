package cn.mmvtc.mobliesafe.chapter02;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import cn.mmvtc.mobliesafe.R;

public class LostFindActivity extends Activity implements View.OnClickListener {
    private TextView mSafePhoneTV;
    private RelativeLayout mInterSetupRL;
    private SharedPreferences msharedPreferences;
    private ToggleButton mToggleButton;
    private TextView mProtectStatusTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lostfind);
        msharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        if(!isSetUp()){
            //如果没有进入过设置向导，则进入
            startSetUp1Activity();
        }
        initView();
    }


    public boolean isSetUp() {
        return msharedPreferences.getBoolean("isSetUp",false);
    }
    /**初始化控件*/
    private void initView() {
        TextView mTitleTV= (TextView) findViewById(R.id.tv_title);
        mTitleTV.setText("手机防盗");
        ImageView mLeftImagv=(ImageView)findViewById(R.id.imgv_leftbtn);
        mLeftImagv.setOnClickListener(this);
        mLeftImagv.setImageResource(R.drawable.back);
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.purple)
        );
        mSafePhoneTV= (TextView) findViewById(R.id.tv_safephone);
        mSafePhoneTV.setText(msharedPreferences.getString("safephone",""));
        mToggleButton= (ToggleButton) findViewById(R.id.togglebtn_lostfind);
        mInterSetupRL= (RelativeLayout) findViewById(R.id.rl_inter_setup_wizard);
        mInterSetupRL.setOnClickListener(this);
        mProtectStatusTV= (TextView) findViewById(R.id.tv_lostfind_protectstauts);
        //查询手机防盗时候开启
        boolean protecting=msharedPreferences.getBoolean("protecting",true);
        if(protecting){
            mProtectStatusTV.setText("防盗保护已经开启");
            mToggleButton.setChecked(true);
        }else{
            mProtectStatusTV.setText("防盗保护没有开启");
            mToggleButton.setChecked(false);
        }
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked){
                    mProtectStatusTV.setText("防盗保护已经开启");
                }else{
                    mProtectStatusTV.setText("防盗保护没有开启");
                }
                SharedPreferences.Editor editor=msharedPreferences.edit();
                editor.putBoolean("protecting",isChecked);
                editor.commit();
            }
        });

    }
    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.rl_inter_setup_wizard:
        //重新进入设置向导
        startSetUp1Activity();
            break;
        case R.id.imgv_leftbtn:
            //返回
            finish();
            break;
    }
    }

    private void startSetUp1Activity() {
        Intent intent=new Intent(LostFindActivity.this,SetUp1Activity.class);
        startActivity(intent);
        finish();
    }




}
