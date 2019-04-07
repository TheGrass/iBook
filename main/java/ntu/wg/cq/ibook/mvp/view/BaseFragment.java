package ntu.wg.cq.ibook.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import ntu.wg.cq.ibook.app.IApplication;

/**
 * Created by C_Q on 2018/2/4.
 */

public abstract class BaseFragment extends Fragment implements BaseView,View.OnClickListener{
    private String TAG = getClass().getSimpleName();
    protected BaseActivity activity;
    protected View view;
    public BaseFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (BaseActivity)context;
        Log.d(TAG,"onAttach");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        view = LayoutInflater.from(activity).inflate(getLayoutId(),container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");
        initView(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        doChange();
        Log.d(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    protected abstract void initView(View view, Bundle savedInstanceState);

    protected abstract void doChange();
    @Override
    public void onClick(View v) {
        click(v);
    }

    protected abstract void click(View v);
    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(getActivity(), clz));
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), clz);
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
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载提示
     * @param isShow
     */
    private QMUITipDialog dialog;
    protected void loadingDialog(boolean isShow){
        if(isShow){
            dialog = new QMUITipDialog.Builder(getActivity()).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).create();
            dialog.show();
        }else {
            if(null != dialog){
                dialog.dismiss();
            }
        }
    }
    public final int DIALOG_MODE_1 = 1; //单按钮 内容 无标题
    public final int DIALOG_MODE_NO_NET = 2; //无网络
    protected void showAppDialog(int mode,String... msg){
        switch (mode){
            case DIALOG_MODE_1:
                new QMUIDialog.MessageDialogBuilder(getActivity())
                        .setMessage(msg[0])
                        .addAction("确认", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case DIALOG_MODE_NO_NET:
                new QMUIDialog.MessageDialogBuilder(getActivity())
                        .setMessage("请检查网络连接")
                        .addAction("确认", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            default:
                break;
        }
    }
    public IApplication getApp(){
        return (IApplication)getActivity().getApplication();
    }
}
