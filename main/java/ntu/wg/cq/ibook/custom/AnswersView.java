package ntu.wg.cq.ibook.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ntu.wg.cq.ibook.R;

/**
 * Created by C_Q on 2018/3/28.
 */

public class AnswersView extends RelativeLayout {
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    public AnswersView(Context context) {
        this(context,null);
    }

    public AnswersView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public AnswersView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context,attrs,defStyleAttr,0);
    }

    public AnswersView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.custom_answers_list_view,this,true);
        rb1=(RadioButton)findViewById(R.id.q1);
        rb2=(RadioButton)findViewById(R.id.q2);
        rb3=(RadioButton)findViewById(R.id.q3);
        rb4=(RadioButton)findViewById(R.id.q4);
    }
}
