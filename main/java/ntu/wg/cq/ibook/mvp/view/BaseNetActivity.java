package ntu.wg.cq.ibook.mvp.view;


import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import ntu.wg.cq.ibook.mvp.presenter.BasePresenter;

/**
 * Created by C_Q on 2018/2/5.
 */

public abstract class BaseNetActivity<T extends BasePresenter> extends BaseActivity {

    protected abstract T getPresenter();

    /**
     * 加载提示
     * @param isShow
     */
    private QMUITipDialog dialog;
    protected void loadingDialog(boolean isShow){
        if(isShow){
            dialog = new QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).create();
            dialog.show();
        }else {
            if(null != dialog){
                dialog.dismiss();
            }
        }
    }
}
