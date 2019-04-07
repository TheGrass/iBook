package ntu.wg.cq.ibook.function.discover.game;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ntu.wg.cq.ibook.interfaces.IMina;

/**
 * Created by C_Q on 2018/3/30.
 */

public class MinaBroadCastReceiver extends BroadcastReceiver {
    private IMina mina;

    public void setMina(IMina mina) {
        this.mina = mina;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getExtras().getString("msg");
        mina.receive(msg);
    }
}
