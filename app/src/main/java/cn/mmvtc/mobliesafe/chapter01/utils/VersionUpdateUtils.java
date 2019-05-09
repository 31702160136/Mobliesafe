package cn.mmvtc.mobliesafe.chapter01.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import cn.mmvtc.mobliesafe.HomeActivity;
import cn.mmvtc.mobliesafe.R;

/**
 * Created by Administrator on 2019/3/6.
 * 1.传递本地版本号
 * 2.获取网络服务器的版本号，发送消息进行UI更新（what,Handler）
 * 3.版本号做对比不一致提示更新对话框。
 * 4.下载进度条的对话框
 * 5.下载新的APK
 */

public class VersionUpdateUtils {
    //更新提醒工具
    private static final int MESSAGE_NET_EEOR = 101;
    private static final int MESSAGE_IO_EEOR = 102;
    private static final int MESSAGE_JSON_EEOR = 103;
    private static final int MESSAGE_SHOEW_DIALOG = 104;
    protected static final int MESSAGE_ENTERHOME = 105;
    //消息处理接收的方法创建 handler 选os
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
            //匹配消息状态码
                case MESSAGE_IO_EEOR:
                    Toast.makeText(context, "IO异常", Toast.LENGTH_SHORT).show();
                    //进入主页的调用
                    enterhome();
                    break;
                case MESSAGE_JSON_EEOR:
                    Toast.makeText(context, "JSON异常", Toast.LENGTH_SHORT).show();
                    //进入主页的调用
                    enterhome();
                    break;
                case MESSAGE_NET_EEOR:
                    Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    //进入主页的调用
                    enterhome();
                    break;
                case MESSAGE_SHOEW_DIALOG:
                    //弹出更新提示消息对话框的调用
                    showUpdateDialog(versionEntity);
                    break;
                case MESSAGE_ENTERHOME:
                    //进入主页的调用
                    Intent intent=new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    context.finish();//注销
                    break;
            }
        }
    };
//本地版本号
    private String mVersion;
    private Activity context;
    private ProgressDialog mProgressDialog;//进度条对话框
    private VersionEntity versionEntity;//版本实体类
//构造方法（alt+instert/constructor)
    public VersionUpdateUtils(String Version,Activity activity) {
        //this.mVersion = mVersion;//错误代码
        this.mVersion = Version;//正确代码
        this.context=activity;
    }
    /**
     * 访问网络服务器，
     * JSON解析获取版本信息，
     * 版本号对比
     */
    public void getCloudVersion(){
        try {
            HttpClient client=new DefaultHttpClient();
            //get请求方式创建，存放网络网址
            HttpGet httpGet =new HttpGet("https://www.yulemofang.cn/shangke/updateinfo.html");

            //发送请求，服务器做响应
            HttpResponse execute=client.execute(httpGet);//返回Json数据
           //判断请求是否成功
            if(execute.getStatusLine().getStatusCode()==200){//成功
                HttpEntity entity=execute.getEntity();
                String result=EntityUtils.toString(entity,"gbk");
                versionEntity=new VersionEntity();//创建版本实体类
                //创建jsonObjeck，JOSN解析
                JSONObject jsonObject=new JSONObject(result);
                String code=jsonObject.getString("code");
                String des=jsonObject.getString("des");
                String apkurl=jsonObject.getString("apkurl");
              //存放数据给versionEntity实体类对象
                versionEntity.versioncode=code;
                versionEntity.description=des;
                versionEntity.apkurl=apkurl;
                //版本号比较和网络服务器端比较
                if (!mVersion.equals(versionEntity.versioncode)){
                    //版本号不一致，做消息处理
                    //发送消息，含有what=MESSAGE_SHOEW_DIALOG，表示ui显示消息对话框
                    handler.sendEmptyMessage(MESSAGE_SHOEW_DIALOG);
                }


            }
        } catch (ClientProtocolException e){

            handler.sendEmptyMessage(MESSAGE_NET_EEOR);
            e.printStackTrace();
        } catch (IOException e) {
            handler.sendEmptyMessage(MESSAGE_IO_EEOR);
            e.printStackTrace();
        } catch (JSONException e) {
            handler.sendEmptyMessage(MESSAGE_JSON_EEOR);
            e.printStackTrace();
        }

    }
//弹出更新提示消息框
    private void showUpdateDialog(final VersionEntity versionEntity){
        //创建消息框
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        //设置消息框
        builder.setTitle("检查到新版本："+versionEntity.versioncode);
        builder.setMessage(versionEntity.description);//描述
        builder.setIcon(R.drawable.icon);//设置图标
        builder.setCancelable(false);//设置不能点击手机返回按钮隐藏对话框
        //消息框的按钮设置点击事件
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载使用的进度条的调用
                initProgressDialog();
                //下载新的apk方法的调用
                downloadNewApk(versionEntity.apkurl);
            }
        });
        builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("2","点击了不升级");
                dialog.dismiss();
                //不升级也要进入主页
                enterhome();
            }
        });
        builder.show();
    }
//初始化下载进度条
    private void initProgressDialog(){
        mProgressDialog=new ProgressDialog(context);
        mProgressDialog.setMessage("正在下载");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();//消息对话都必须show
    }
//下载新版本
    private void downloadNewApk(String apkurl){
        DownloadUtils downloadUtils=new DownloadUtils();
        downloadUtils.downapk(apkurl, "mnt/sdcard/mobliesafe2.0.apk",
                new MyCallBack() {
                    @Override
                    public void onSucess(ResponseInfo<File> arg0) {
                        mProgressDialog.dismiss();
                        MyUtils.installApk(context);

                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        mProgressDialog.setMessage("下载失败...");
                        mProgressDialog.dismiss();
                        enterhome();
                    }

                    @Override
                    public void onLoadding(long total, long current, boolean isUploading) {
                       mProgressDialog.setMax((int) total);
                        mProgressDialog.setMessage("正在下载...");
                        mProgressDialog.setProgress((int) current);


                    }
                });
    }


    /**
    *版本信息实体类，用于存放从服务器端获得的版本信息
     * code，des,apkurl
     */
    public class VersionEntity{
        public String versioncode;//服务器版本号
        public String description;//版本描述
        public String apkurl;//apk下载地址
    }
    private void enterhome(){
        handler.sendEmptyMessageDelayed(MESSAGE_ENTERHOME,2000);
    }
}
