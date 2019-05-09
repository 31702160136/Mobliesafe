package cn.mmvtc.mobliesafe.chapter01.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.mmvtc.mobliesafe.R;

/**
 * Created by Administrator on 2019/3/14.
 * 适配器
 */

public class HomeAdapter extends BaseAdapter {

     int [] imageId={R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
            R.drawable.trojan,R.drawable.sysoptimize,R.drawable.taskmanager,
            R.drawable.netmanager,R.drawable.atools,R.drawable.settings};

    String[] names={"手机防盗","通讯卫士","软件管家","手机杀毒","缓存清理",
            "进程管理","流量统计","高级工具","设置中心"};
    private Context context;
    public HomeAdapter(Context context){
        this.context=context;

    }
    //设置gridView一共有多少个条目
    @Override
    public int getCount() {
        return 9;
    }


    //设置每个条目的界面
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context,R.layout.item_home,null);//创建视图
        ImageView iv_icon=(ImageView)view.findViewById(R.id.iv_icon);//图标
        TextView tv_name= (TextView) view.findViewById(R.id.tv_name);//文字
        iv_icon.setImageResource(imageId[position]);//imageId[position]定义到位置
        tv_name.setText(names[position]);
        return view;
    }
    //暂时不设置
    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
