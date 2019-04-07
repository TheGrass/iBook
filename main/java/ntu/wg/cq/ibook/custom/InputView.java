package ntu.wg.cq.ibook.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ntu.wg.cq.ibook.R;

/**
 * Created by C_Q on 2018/3/31.
 */

public class InputView extends LinearLayout {
    private TextView textView;
    private EditText editText;
    private String hint;
    public InputView(Context context) {
        this(context,null);
    }

    public InputView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public InputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public InputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.custom_input,this,true);
        textView = (TextView) findViewById(R.id.tv_name);
        editText = (EditText) findViewById(R.id.edt_value);
    }
    public void init(String name,String value){
        textView.setText(name);
        editText.setHint(value);
        hint = value;
    }
    public void clear(){
        editText.setText("");
        editText.setHint(hint);
    }
    public String getInput(){
        return editText.getText().toString();
    }
}
