package ntu.wg.cq.ibook.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import ntu.wg.cq.ibook.mvp.presenter.BasePresenter;

/**
 * Created by C_Q on 2018/2/5.
 */

public abstract class BaseNetFragment<T extends BasePresenter> extends BaseFragment {

    protected abstract T getPresenter();

}
