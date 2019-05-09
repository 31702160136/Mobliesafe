package cn.mmvtc.mobliesafe.chapter02.utils;

import android.os.Message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Created by S on 2019/3/14.
 */

public class MD5Utills {
    public static String encode(String text) {


        try {
            MessageDigest  digest = MessageDigest.getInstance("md5");
            byte[]result=digest.digest(text.getBytes());
            StringBuilder sb=new StringBuilder();
            for (byte b:result){
                int number=b&0xff;
                String hex=Integer.toHexString(number);
                if(hex.length()==1){
                    sb.append("0"+hex);

                }else{
                    sb.append(hex);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }


    }

}
