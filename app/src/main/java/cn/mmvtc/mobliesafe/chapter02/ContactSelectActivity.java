package cn.mmvtc.mobliesafe.chapter02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.mmvtc.mobliesafe.R;
import cn.mmvtc.mobliesafe.chapter02.adapter.ContactAdapter;
import cn.mmvtc.mobliesafe.chapter02.entity.ContactInfo;
import cn.mmvtc.mobliesafe.chapter02.utils.ContactInfoParser;

/**
 * Created by S on 2019/4/16.
 */


public class ContactSelectActivity extends Activity implements View.OnClickListener {

    private ListView mListView;
    private ContactAdapter adapter;
    private List<ContactInfo> systemContacts;
    Handler mHandler  = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 10:
                    if(systemContacts != null){
                        adapter = new ContactAdapter(systemContacts,ContactSelectActivity.this);
                        mListView.setAdapter(adapter);
                    }
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contact_select);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("选择联系人");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        //设置导航栏颜色
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.purple));
        mListView = (ListView) findViewById(R.id.lv_contact);
        new Thread(){
            public void run() {
                //得到通讯录联系人的名字和电话号码
                systemContacts = ContactInfoParser.getSystemContact(ContactSelectActivity.this);
                //追加sim卡在联系人信息。在通讯录的联系人信息的基础上添加存放在sim卡的联系人名字和电话号码。
                systemContacts.addAll(ContactInfoParser.getSimContacts(ContactSelectActivity.this));
                mHandler.sendEmptyMessage(10);
            };
        }.start();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,//onItemClick为条目点击方法
                                    int position, long id) {
                ContactInfo item = (ContactInfo) adapter.getItem(position);//得到条目，存为
                Intent intent  = new Intent();
                intent.putExtra("phone", item.phone);
                setResult(0, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();//关闭当前窗口，在标题栏上的返回按钮
                break;

        }
    }
}
