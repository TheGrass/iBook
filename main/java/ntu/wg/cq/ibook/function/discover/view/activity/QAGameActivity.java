package ntu.wg.cq.ibook.function.discover.view.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.API;
import ntu.wg.cq.ibook.function.discover.game.Client;
import ntu.wg.cq.ibook.function.discover.game.MinaBroadCastReceiver;
import ntu.wg.cq.ibook.function.discover.view.adapter.AnswerAdapter;
import ntu.wg.cq.ibook.interfaces.IMina;
import ntu.wg.cq.ibook.interfaces.ISimpleInterface;
import ntu.wg.cq.ibook.mvp.view.BaseActivity;
import ntu.wg.cq.ibook.pojo.Answer;
import ntu.wg.cq.ibook.pojo.GameIO;
import ntu.wg.cq.ibook.pojo.QA;
import ntu.wg.cq.ibook.utils.StringUtils;

import static ntu.wg.cq.ibook.function.discover.game.Constants.*;


/**
 * Created by C_Q on 2018/3/28.
 */

public class QAGameActivity extends BaseActivity {
    private Client client;
    private MinaBroadCastReceiver receiver;
    private TextView tvQuestion;
    private TextView tvMyScore;
    private TextView tvOtherScore;
    private ImageView ivCancel;
    private RecyclerView rv;
    private View view;
    private TextView tvTime;
    private LinearLayoutManager manager;
    private AnswerAdapter answerAdapter;
    private List<Answer> answerList = new ArrayList<>();

    private int compId;
    private ImageView ivStart;
    private boolean[] click = {false,false,false,false};
    private int pos = 0;
    private int seconds;
    private int score = 0;
    @Override
    public int bindLayout() {
        return R.layout.activity_qa_game;
    }

    @Override
    protected void initView() {
        tvQuestion = findView(R.id.tv_question);
        tvMyScore = findView(R.id.tv_score_i);
        tvOtherScore = findView(R.id.tv_score_other);
        view = findView(R.id.view);
        rv = findView(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(manager);
        rv.setAdapter(answerAdapter);
        tvTime = findView(R.id.tv_time);
        ivCancel = findView(R.id.cancel);
        ivCancel.setOnClickListener(this);
        ivStart = findView(R.id.iv_start);
        ivStart.setOnClickListener(this);
        view.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        answerAdapter = new AnswerAdapter(answerList,this,click);
        answerAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = (Integer)v.getTag();
                if (answerList.get(pos).getIsTrue()==1){
                    score += (seconds+1)*10;
                    tvMyScore.setText(score+"");
                    client.write(new GameIO(SCORE,compId,score+""));
                }
                click[pos] = true;
                view.setVisibility(View.VISIBLE);
                answerAdapter.notifyDataSetChanged();
            }
        });
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
    }

    @Override
    protected boolean allowFullScreen() {
        return true;
    }

    @Override
    protected boolean allowScreenRoate() {
        return false;
    }

    @Override
    public void doBusiness() {
        receiver = new MinaBroadCastReceiver();
        registerReceiver(receiver,new IntentFilter("mina_message"));
        receiver.setMina(new IMina() {
            @Override
            public void receive(String message) {
                if (message.length()<3){
                    seconds = Integer.valueOf(message);
                    if(seconds == 4){
                        tvTime.setTextColor(Color.RED);
                    }else if(seconds == 10){
                        tvTime.setTextColor(Color.BLACK);
                    }
                    tvTime.setText(message);
                }else {
                    GameIO in = JSON.parseObject(message, GameIO.class);
                    switch (in.getFlag()){
                        case QUESTION:
                            QA qa = JSON.parseObject(in.getValue(), QA.class);
                            tvQuestion.setText(qa.getQ());
                            List<Answer> temp = JSON.parseArray(qa.getA(), Answer.class);
                            answerList.clear();
                            answerList.addAll(temp);
                            Log.d("QUESTION",answerList.size()+"");
                            click[pos] = false;
                            view.setVisibility(View.GONE);
                            answerAdapter.notifyDataSetChanged();
                            break;
                        case SCORE:
                            Log.d("SCORE",in.getValue());
                            tvOtherScore.setText(in.getValue());
                            break;
                        case FINISH:
                            Log.d("FINISH","-----");
                            showToast(in.getValue());
                            ivStart.setVisibility(View.VISIBLE);
                            ivStart.setImageResource(R.mipmap.pipei);
                            tvOtherScore.setText(0+"");
                            tvMyScore.setText(0+"");
                            ivStart.setClickable(true);
                            score = 0;
                            client.close();
                            break;
                        case MATCHED:
                            Log.d("MATCHED","-----");
                            compId = in.getId();
                            ivCancel.setVisibility(View.GONE);
                            ivStart.setVisibility(View.GONE);
                            break;
                        case MATCHING:
                            Log.d("MATCHING","-----");
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void click(View v) {
        switch (v.getId()){
            case R.id.iv_start:
                ivStart.setClickable(false);
                try {
                    client = new Client(this);
                    client.init();
                    ivStart.setImageResource(R.mipmap.loading);
                    ivCancel.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    showToast("无法连接到服务器(；′⌒`)");
                    ivStart.setClickable(true);
                }
                break;
            case R.id.cancel:
                ivCancel.setVisibility(View.GONE);
                ivStart.setImageResource(R.mipmap.pipei);
                ivStart.setClickable(true);
                client.cancel();
                break;
            case R.id.view:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != client){
            client.close();
        }
        unregisterReceiver(receiver);
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("再按一次退出游戏");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
