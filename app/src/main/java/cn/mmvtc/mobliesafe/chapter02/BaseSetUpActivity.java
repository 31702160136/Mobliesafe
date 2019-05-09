package cn.mmvtc.mobliesafe.chapter02;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import cn.mmvtc.mobliesafe.R;

/**
 * 功能手势识别器父类，其他的类可以继承该类
 *
 */
public abstract class BaseSetUpActivity extends Activity {
    public SharedPreferences sp;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        //1.初始化手势识别器
        mGestureDetector=new GestureDetector(this,new
        GestureDetector.SimpleOnGestureListener(){
           //e1代表手指第一次触摸屏幕的事件，e2 代表手指离开屏幕的一瞬间的事件
            //velocityX 水平方向的速度 单位 pix/s,velocityY竖直方向的速度

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
               if(Math.abs(velocityX)<100){
                   Toast.makeText(getApplicationContext(), "无效动作，移动太慢", Toast.LENGTH_SHORT).show();
                   return true;
               }
              if((e2.getRawX()-e1.getRawX())>100){
                    //从左向右滑动屏幕，显示上一个页面
                  showPre();
                  overridePendingTransition(R.anim.pre_in,R.anim.pre_out);
                  return true;
                }
                if((e1.getRawX()-e2.getRawX())>100){
                    //从右向右滑动屏幕，显示下一个页面
                    showNext();
                    overridePendingTransition(R.anim.next_in,R.anim.next_out);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);

            }
        });

    }
    public abstract void showNext();
    public abstract void showPre();
    //2.用手势识别器去识别事件
    public boolean onTouchEvent(MotionEvent event){
        //分析手势事件，做屏幕触摸才会发生调用手势识别器
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 开启新的activity并且关闭自己
     * @param cls
     */
    public void startActivityAndFinishSelf(Class<?>cls){
    Intent intent=new Intent(this,cls);
    startActivity(intent);
    finish();
}
}
