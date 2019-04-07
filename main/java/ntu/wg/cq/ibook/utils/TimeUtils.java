package ntu.wg.cq.ibook.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;

import ntu.wg.cq.ibook.pojo.PowerAndTime;

/**
 * Created by C_Q on 2018/3/10.
 */

public class TimeUtils {
    /**
     * 获取时间
     * @return
     */
    public static String getTime(){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String date = df.format(new Date());
        return date;
    }
}
