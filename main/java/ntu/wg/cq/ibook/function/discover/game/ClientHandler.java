package ntu.wg.cq.ibook.function.discover.game;

/**
 * Created by C_Q on 2018/3/30.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import ntu.wg.cq.ibook.app.IApplication;
import ntu.wg.cq.ibook.interfaces.IMina;

public class ClientHandler extends IoHandlerAdapter {
    private Context context;
    public ClientHandler(Context context){
        this.context = context;
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);
        Log.d("received",message.toString());
        if(null != context){
            Intent intent = new Intent();
            intent.setAction("mina_message");
            intent.putExtra("msg",message.toString());
            context.sendBroadcast(intent);
        }
    }
}
