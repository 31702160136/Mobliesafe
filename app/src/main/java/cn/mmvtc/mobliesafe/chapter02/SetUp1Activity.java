package cn.mmvtc.mobliesafe.chapter02;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.mmvtc.mobliesafe.R;

public class SetUp1Activity extends BaseSetUpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        initView();
    }

    private void initView() {
        //设置第一个小圆点的颜色
        ((RadioButton)findViewById(R.id.rb_first)).setChecked(true);

    }
    public void showNext(){
        Log.d("sss","ss");
        startActivityAndFinishSelf(SetUp2Activity.class);

    }
    public void showPre(){
        Toast.makeText(this, "当前页面已经是第一页", Toast.LENGTH_SHORT).show();
    }

}
