package ntu.wg.cq.ibook.function.user.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.function.user.presenter.UserPresenter;
import ntu.wg.cq.ibook.function.user.view.activity.AddBookActivity;
import ntu.wg.cq.ibook.function.user.view.activity.ForgetActivity;
import ntu.wg.cq.ibook.function.user.view.activity.LoginActivity;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.view.BaseNetFragment;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/2/6.
 */

public class MineFragment extends BaseNetFragment {
    private QMUIRoundButton toLogin;
    private TextView tvMail;
    private TextView tvAddBook;
    private TextView tvClearCache;
    private TextView tvChangePwd;
    private TextView tvLogout;

    private String token;
    private String mail;

    private UserPresenter presenter;
    @Override
    protected UserPresenter getPresenter() {
        return new UserPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData() {
        presenter = getPresenter();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        toLogin=(QMUIRoundButton)view.findViewById(R.id.btn_to_login);
        tvMail=(TextView)view.findViewById(R.id.tv_mail);
        tvAddBook=(TextView)view.findViewById(R.id.tv_add_book);
        tvClearCache=(TextView)view.findViewById(R.id.tv_clear_cache);
        tvChangePwd=(TextView)view.findViewById(R.id.tv_change_password);
        tvLogout=(TextView)view.findViewById(R.id.tv_logout);
        tvLogout.setOnClickListener(this);
        toLogin.setOnClickListener(this);
        tvChangePwd.setOnClickListener(this);
        tvClearCache.setOnClickListener(this);
        tvAddBook.setOnClickListener(this);
    }

    @Override
    protected void doChange() {
        token = getApp().getSetting(Constants.TOKEN);
        mail = getApp().getSetting(Constants.MAIL);
        if(StringUtils.haveEmpty(token,mail)){
            toLogin.setVisibility(View.VISIBLE);
        }else {
            toLogin.setVisibility(View.GONE);
            tvMail.setVisibility(View.VISIBLE);
            tvAddBook.setVisibility(View.VISIBLE);
            tvChangePwd.setVisibility(View.VISIBLE);
            tvLogout.setVisibility(View.VISIBLE);
            tvMail.setText(mail);
        }
    }

    @Override
    protected void click(View v) {
        switch (v.getId()){
            case R.id.btn_to_login:
                startActivity(LoginActivity.class);
                break;
            case R.id.tv_logout:
                presenter.doLogout(getActivity(), token, new IResult() {
                    @Override
                    public void doChange(Object result) {
                        getApp().addSetting(Constants.TOKEN,"");
                        getApp().addSetting(Constants.MAIL,"");
                        tvMail.setVisibility(View.GONE);
                        tvAddBook.setVisibility(View.GONE);
                        tvChangePwd.setVisibility(View.GONE);
                        tvLogout.setVisibility(View.GONE);
                        toLogin.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case R.id.tv_change_password:
                startActivity(ForgetActivity.class);
                break;
            case R.id.tv_clear_cache:
                new QMUIDialog.MessageDialogBuilder(getActivity())
                        .setTitle("提示").setMessage("清除缓存后将丢失阅读记录及书籍封面")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确认", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                getApp().clear();
                                showToast("清除完成");
                            }
                        }).show();
                break;
            case R.id.tv_add_book:
                startActivity(AddBookActivity.class);
                break;
        }
    }
}
