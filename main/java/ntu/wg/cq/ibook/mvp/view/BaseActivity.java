package ntu.wg.cq.ibook.mvp.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.IApplication;
import ntu.wg.cq.ibook.function.read.adapter.CatalogAdapter;
import ntu.wg.cq.ibook.function.user.view.activity.LoginActivity;

/**
 * Created by C_Q on 2018/1/28.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView,View.OnClickListener{
    private String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting(allowFullScreen(),allowScreenRoate());
        Log.d(TAG,"onCreate");
        initData();
        initView();
        doBusiness();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onReStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }


    public IApplication getApp() {
        return (IApplication)getApplication();
    }
    /**
     * [绑定布局]
     *
     * @return
     */
    public abstract int bindLayout();

    protected abstract void initView();
    protected abstract void initData();
    /**
     * 是否全屏
     * */
    protected abstract boolean allowFullScreen();
    /***
     * 屏幕能否旋转
     */
    protected abstract boolean allowScreenRoate();

    /**
     * 设置
     * */
    private void setting(boolean isAllowFullScreen,boolean isAllowScreenRoate){
        if (isAllowFullScreen) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getColor(R.color.bottom_background));
            }
        }
       /* else {
            QMUIStatusBarHelper.translucent(this);
        }*/
        setContentView(bindLayout());
        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * [绑定控件]
     *
     * @param resId
     *
     * @return
     */
    protected <T extends View>T findView(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * [业务操作]
     *
     * @param
     */
    public abstract void doBusiness();

    /**
     * [页面跳转]
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * Toast
     * @param msg
     */
    protected void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



    protected<T extends BaseFragment> void replaceFragment(int resId,T fragment){
        getSupportFragmentManager().beginTransaction().replace(resId,fragment).commit();
    }
    protected<T extends BaseFragment> void replaceFragment(int resId,T from,T to){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(to.isAdded()){
            if(null != from){
                transaction.hide(from);
            }
            transaction.show(to).commit();
        }else {
            if(null != from){
                transaction.hide(from);
            }
            transaction.add(resId,to).commit();
        }
    }
    protected String getStrById(int id){
        return getResources().getString(id)==null ? "":getResources().getString(id);
    }
    protected int getColorById(int id){
        return getResources().getColor(id,null);
    }
    @Override
    public void onClick(View v) {
        click(v);
    }

    public abstract void click(View v);

    public final int DIALOG_MODE_1 = 1; //单按钮 内容
    public final int DIALOG_MODE_NO_NET = 2; //无网络
    public final int DIALOG_MODE_TO_LOGIN = 3; //登录
    public final int DIALOG_MODE_POPUP=4; //列表
    public final int POPUP_WINDOW_DISMISS=5; //隐藏popup window
    public final int DIALOG_MODE_2 = 6;//双按钮
    private PopupWindow popupWindow;
    protected void showAppDialog(int mode,Object... msg){
        switch (mode){
            case DIALOG_MODE_1:
                new QMUIDialog.MessageDialogBuilder(this)
                        .setMessage((String)msg[0])
                        .addAction("确认", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case DIALOG_MODE_NO_NET:
                new QMUIDialog.MessageDialogBuilder(this)
                        .setMessage("请检查网络连接")
                        .addAction("确认", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case DIALOG_MODE_TO_LOGIN:
                new QMUIDialog.MessageDialogBuilder(this)
                        .setMessage("请登陆后再试")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("登录", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                startActivity(LoginActivity.class);
                            }
                        }).show();
                break;
            case DIALOG_MODE_POPUP:
                View view = LayoutInflater.from(this).inflate(R.layout.pop_list,null,false);
                ImageView ivClose = (ImageView)view.findViewById(R.id.iv_close);
                LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                CatalogAdapter catalogAdapter = (CatalogAdapter)msg[0];
                recyclerView.setAdapter(catalogAdapter);
                popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(false);
                final View start = (View)msg[1];
                popupWindow.showAtLocation(start, Gravity.START,0,0);
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null!=popupWindow){
                            popupWindow.dismiss();
                        }
                    }
                });
                manager.scrollToPosition((Integer) msg[2]);
                break;
            case POPUP_WINDOW_DISMISS:
                if(null!=popupWindow){
                    popupWindow.dismiss();
                }
                break;
            case DIALOG_MODE_2:
                new QMUIDialog.MessageDialogBuilder(this)
                        .setMessage((String)msg[0])
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定",(QMUIDialogAction.ActionListener)msg[1])
                        .show();
                break;
            default:
                break;
        }
    }
}
