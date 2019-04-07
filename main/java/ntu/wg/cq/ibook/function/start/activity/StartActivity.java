package ntu.wg.cq.ibook.function.start.activity;


import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.pojo.Book;
import ntu.wg.cq.ibook.function.start.presenter.StartPresenter;
import ntu.wg.cq.ibook.mvp.view.BaseNetActivity;
import ntu.wg.cq.ibook.utils.StringUtils;
import ntu.wg.cq.ibook.utils.VersionUtil;
import ntu.wg.cq.ibook.utils.ViewUtils;

import static ntu.wg.cq.ibook.app.Constants.*;

/**
 * Created by C_Q on 2018/2/5.
 */

public class StartActivity extends BaseNetActivity {
    private Activity activity= this;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    @Override
    protected StartPresenter getPresenter() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        textView1=findView(R.id.content);
        textView2=findView(R.id.content1);
        textView3=findView(R.id.content2);
    }

    @Override
    protected void initData() {
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
        initTextSize(1,textView1);
        initTextSize(2,textView2);
        initTextSize(3,textView3);
        initSetting();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(IBookActivity.class);
                    finish();
                }
            }
        }).start();
    }

    private void initSetting(){
        if(StringUtils.haveEmpty(getApp().getSetting(SIZE_MODE))){
            getApp().addSetting(SIZE_MODE,"1");
        }
        if(StringUtils.haveEmpty(getApp().getSetting(IS_NIGHT))){
            getApp().addSetting(IS_NIGHT,"0");
        }
        if(StringUtils.haveEmpty(getApp().getSetting(BACKGROUND))){
            getApp().addSetting(BACKGROUND,"1");
        }
        if(StringUtils.haveEmpty(getApp().getSetting(IS_BARRAGE))){
            getApp().addSetting(IS_BARRAGE,"0");
        }
       /* if(StringUtils.haveEmpty(getApp().getSetting(IS_CLICK))){
            getApp().addSetting(IS_CLICK,"0");
        }*/
        if(StringUtils.haveEmpty(getApp().getSetting(LIGHT))){
            getApp().addSetting(LIGHT,"44");
        }
    }

    private void initTextSize(final int mode, final TextView textView){
        if(StringUtils.haveEmpty(getApp().getSetting(LINES+mode),getApp().getSetting(CHAR_COUNT+mode),getApp().getSetting(SIZE+mode),getApp().getSetting(SPACE+mode))){
            textView.post(new Runnable() {
                @Override
                public void run() {
                    int topOfLastLine = textView.getHeight() - textView.getPaddingTop() - textView.getPaddingBottom() - textView.getLineHeight();
                    getApp().addSetting(LINES+mode,textView.getLayout().getLineForVertical(topOfLastLine)+"");
                    getApp().addSetting(CHAR_COUNT+mode,textView.getLayout().getLineEnd(0)+"");
                    getApp().addSetting(SIZE+mode, ViewUtils.px2sp(activity,textView.getTextSize())+"");  //getTextSize单位PX   setTextSize 单位为SP
                    getApp().addSetting(SPACE+mode,textView.getLineSpacingExtra()+"");
                }
            });
        }
    }
    @Override
    public void click(View v) {
    }
}
