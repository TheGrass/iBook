package ntu.wg.cq.ibook.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ntu.wg.cq.ibook.R;


/**
 * Created by C_Q on 2018/3/22.
 */

public class TopBar extends RelativeLayout {
    private TextView tvTitle;
    private Button leftButton;
    private Button rightButton;
    private RelativeLayout relativeLayout;
    public TopBar(Context context) {
        this(context,null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context,attrs,defStyleAttr,0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.topbar,this,true);
        tvTitle = (TextView)findViewById(R.id.tv_title);
        leftButton = (Button)findViewById(R.id.btn_left);
        rightButton = (Button)findViewById(R.id.btn_right);
        relativeLayout = (RelativeLayout)findViewById(R.id.rl);
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setLeftButton(String msg,OnClickListener onClickListener){
        leftButton.setVisibility(VISIBLE);
        leftButton.setText(msg);
        leftButton.setOnClickListener(onClickListener);
    }
    public void setRightButton(String msg,OnClickListener onClickListener){
        rightButton.setVisibility(VISIBLE);
        rightButton.setText(msg);
        rightButton.setOnClickListener(onClickListener);
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public Button getLeftButton() {
        return leftButton;
    }

    public Button getRightButton() {
        return rightButton;
    }
    public void setBackGroundColor(int resId){
        relativeLayout.setBackgroundResource(resId);
        leftButton.setBackgroundResource(resId);
        rightButton.setBackgroundResource(resId);
    }
}
