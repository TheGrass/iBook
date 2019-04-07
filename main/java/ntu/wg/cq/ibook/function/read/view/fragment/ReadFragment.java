package ntu.wg.cq.ibook.function.read.view.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.function.read.broadcastreceiver.PowerReceiver;
import ntu.wg.cq.ibook.interfaces.IPowerListener;
import ntu.wg.cq.ibook.interfaces.ITextSize;
import ntu.wg.cq.ibook.mvp.view.BaseFragment;
import ntu.wg.cq.ibook.utils.TimeUtils;


/**
 * Created by C_Q on 2018/2/9.
 */

public class ReadFragment extends BaseFragment {
    //private TextView tvTitle;
    private RelativeLayout relativeLayout;
    private TextView tvContent;
    private TextView tvPower;
    private TextView tvTime;
    private TextView tvProgress;
    private Bundle bundle;
    private PowerReceiver powerReceiver;
    private ITextSize iTextSize;

    public void setTextSize(ITextSize iTextSize) {
        this.iTextSize = iTextSize;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_read;
    }

    @Override
    protected void initData() {
        bundle=getArguments();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        //tvTitle=(TextView)view.findViewById(R.id.title);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.rl);
        tvContent=(TextView) view.findViewById(R.id.content);
        tvPower = (TextView)view.findViewById(R.id.tv_power);
        tvTime = (TextView)view.findViewById(R.id.tv_time);
        tvProgress = (TextView)view.findViewById(R.id.tv_pro);
        //tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    protected void doChange() {
        //tvTitle.setText(bundle.getString(Constants.TITLE));
        tvContent.setTextSize(iTextSize.size());
        tvContent.setText(bundle.getString(Constants.CONTENT));
        //tvContent.setTextSize(Float.valueOf(getApp().get(Constants.SIZE)));
        powerReceiver = new PowerReceiver();
        getActivity().registerReceiver(powerReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        powerReceiver.setPowerListener(new IPowerListener() {
            @Override
            public void power(int power) {
                tvPower.setText(power+"%");
                tvTime.setText(TimeUtils.getTime());
            }
        });
        tvProgress.setText(bundle.getInt("this")+"/"+bundle.getInt("total"));
       // getApp().putCache(getApp().getSetting(Constants.TOKEN)+bundle.getString("bid"),bundle.getString("position"));
    }


    @Override
    protected void click(View v) {

    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(powerReceiver);
    }

}
