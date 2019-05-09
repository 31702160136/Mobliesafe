package cn.mmvtc.mobliesafe.chapter01.utils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * Created by Administrator on 2019/2/28.
 * x下载文件工具类
 * 使用xUtils 的 HttpUtils模块download（URL，target，RequesCallBACK）
 *
 */

public class DownloadUtils {
    public  void downapk(String url,String targerFile,
                         final MyCallBack myCallBack){
        //创建HttpUtils对象
        HttpUtils httpUtils=new HttpUtils();
        //调用HttpUtils下载的方法下载指定文件
        httpUtils.download(url,targerFile,new RequestCallBack<File>(){
            public void onSuccess(ResponseInfo<File> arg0){
                myCallBack.onSucess(arg0);

            }
            public void onFailure(HttpException arg0, String arg1){
                myCallBack.onFailure(arg0,arg1);
            }
            public void onLoading(long total,long current,boolean isUploading){
                super.onLoading(total,current,isUploading);
                myCallBack.onLoadding(total, current, isUploading);
            }
        });
    }
}
//接口方法
interface MyCallBack{
    void onSucess(ResponseInfo<File> arg0);//下载成功调用
    void onFailure(HttpException arg0,String arg1);//下载失败调用
    void onLoadding(long total,long current,boolean isUploading);//下载中调用

}
