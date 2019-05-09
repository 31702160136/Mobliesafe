package cn.mmvtc.mobliesafe.chapter01.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/**
 * Created by Administrator on 2019/3/6.
 * 获取版本号
 */

public class MyUtils{

    //1.获取本地版本号，从清单文件中的versionname 获取版本号
//Packagemanager
    public static String getVersion(Context context){
        PackageManager manager=context.getPackageManager();//得到对象
        try{
            //getPackagename()获取到当前程序的包名
            PackageInfo packageInfo=manager.getPackageInfo(
                    context.getPackageName(),0);//得到包
            return packageInfo.versionName;//获得本地版本号信息

        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return "";
        }
    }

    //2.安装新版本，使用y隐式意图调用Activity安装APK
    public static void installApk(Activity activity){
        Intent intent=new Intent("android.intent.action.VIEW");
//添加默认分类
        intent.addCategory("anfroid.intent.catgory.DEFAULT");
//设置数据和类型
        intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/mobilesafe2.0.apk")),
                "application/vnd.android.package-archive");
//如果开启的Activity退出时会调当前Activity的onActivityResult

        activity.startActivityForResult(intent,0);

    }

}