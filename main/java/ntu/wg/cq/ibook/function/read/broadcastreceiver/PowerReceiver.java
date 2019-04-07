package ntu.wg.cq.ibook.function.read.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ntu.wg.cq.ibook.interfaces.IPowerListener;

/**
 * Created by C_Q on 2018/3/10.
 */

public class PowerReceiver extends BroadcastReceiver {
    private IPowerListener powerListener;

    public void setPowerListener(IPowerListener powerListener) {
        this.powerListener = powerListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int current=intent.getExtras().getInt("level");//获得当前电量
        int total=intent.getExtras().getInt("scale");//获得总电量
        int percent=current*100/total;
        powerListener.power(percent);
    }
}
