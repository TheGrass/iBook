package ntu.wg.cq.ibook.function.discover.game;

/**
 * Created by C_Q on 2018/3/30.
 */

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import ntu.wg.cq.ibook.pojo.GameIO;

import static ntu.wg.cq.ibook.function.discover.game.Constants.CANCEL;

public class Client {
    private IoSession session = null;
    private IoConnector connector;
    private Context context;
    public Client(Context context){
        this.context =context;
    }
    public void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                connector = new NioSocketConnector();
                connector.setConnectTimeoutMillis(30000);
                connector.getFilterChain().
                        addLast("codec",
                                new ProtocolCodecFilter(
                                        new TextLineCodecFactory(
                                                Charset.forName("UTF-8"),
                                                LineDelimiter.WINDOWS.getValue(),
                                                LineDelimiter.WINDOWS.getValue())));
                connector.setHandler(new ClientHandler(context));
                ConnectFuture future = connector.connect(new InetSocketAddress("106.15.195.16",7777));//创建链接
                future.awaitUninterruptibly();// 等待连接创建完成
                session = future.getSession();//获得session
            }
        }).start();
    }
    public void close(){
        if(null != session){
            session.closeNow();
        }
    }
    public void write(final GameIO out){
        new Thread(new Runnable() {
            @Override
            public void run() {
                session.write(JSON.toJSONString(out));
            }
        }).start();
    }
    public void cancel(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = JSON.toJSONString(new GameIO(Constants.CANCEL));
                session.write(s);
                Log.d("CANCEL---",s);
                close();
            }
        }).start();
    }
}

