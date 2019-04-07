package ntu.wg.cq.ibook.utils;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by C_Q on 2018/3/2.
 */

public class ViewUtils {
    /**
     * 按钮倒计时
     * @param button 按钮
     * @param second 秒数
     */
    public static void buttonCountDown(final Button button, int second){
        button.setClickable(false);
        new CountDownTimer(second*1000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                button.setText(millisUntilFinished/1000+"秒后重新发送");
            }
            @Override
            public void onFinish() {
                button.setText("获取验证码");
                button.setClickable(true);
            }
        }.start();
    }
    public static void setUnClick(final Button button, int second){
        button.setClickable(false);
        new CountDownTimer(second*1000,second*1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                button.setClickable(true);
            }
        }.start();
    }
    /**
     * 设置亮度
     * @param activity
     * @param brightness
     */
    public static void setLight(Activity activity, int brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        activity.getWindow().setAttributes(lp);
    }

    /**
     * px转sp
     * @param context
     * @param px
     * @return
     */
    public static float px2sp(Context context,float px){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return px / fontScale;
    }

    /**
     * sp转px
     * @param context
     * @param sp
     * @return
     */
    public static float sp2px(Context context, float sp) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * fontScale;
    }
}
