package ntu.wg.cq.ibook.function.user.view.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.function.user.presenter.UserPresenter;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.view.BaseNetActivity;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.utils.JsonUtil;
import ntu.wg.cq.ibook.utils.StringUtils;
import ntu.wg.cq.ibook.utils.ViewUtils;

/**
 * Created by C_Q on 2018/2/7.
 */

public class ForgetActivity extends BaseNetActivity {
    private String mail;
    private String password;
    private String surePassword;
    private String captcha;

    private EditText edtMail;
    private EditText edtPassword;
    private EditText edtSurePassword;
    private EditText edtCaptcha;
    private QMUITopBar topBar;

    private QMUIRoundButton btnSendCaptcha;
    private QMUIRoundButton btnChange;

    private Button btnBack;
    private UserPresenter presenter;
    @Override
    protected UserPresenter getPresenter() {
        return new UserPresenter();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_password;
    }

    @Override
    protected void initView() {
        topBar=findView(R.id.top_bar);
        topBar.setTitle("修改密码");
        btnBack = topBar.addLeftTextButton("返回",R.layout.empty_button);
        btnBack.setTextColor(getColorById(R.color.text_color_3));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtMail = findView(R.id.edt_mail);
        edtPassword = findView(R.id.edt_pwd);
        edtSurePassword = findView(R.id.edt_sure);
        edtCaptcha = findView(R.id.edt_captcha);
        btnSendCaptcha =findView(R.id.btn_send_captcha);
        btnChange = findView(R.id.btn_change);
        btnChange.setOnClickListener(this);
        btnSendCaptcha.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        presenter = getPresenter();
    }

    @Override
    protected boolean allowFullScreen() {
        return false;
    }

    @Override
    protected boolean allowScreenRoate() {
        return false;
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void click(View v) {
        switch (v.getId()){
            case R.id.btn_send_captcha:
                mail = edtMail.getText().toString();
                if(StringUtils.haveEmpty(mail)){
                    showToast("请输入邮箱");
                }else {
                    presenter.getCaptcha(this,mail, new IResult() {
                        @Override
                        public void doChange(Object result) {
                            ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                            if(myResponse.getState()== Constants.FAIL){
                                showAppDialog(DIALOG_MODE_1,myResponse.getMessage());
                            }else{
                                showToast(myResponse.getMessage());
                                ViewUtils.buttonCountDown(btnSendCaptcha,60);
                            }
                        }
                    });
                }
                break;
            case R.id.btn_change:
                mail = edtMail.getText().toString();
                password = edtPassword.getText().toString();
                surePassword = edtSurePassword.getText().toString();
                captcha = edtCaptcha.getText().toString();
                if(StringUtils.haveEmpty(mail,password,surePassword,captcha)){
                    showToast("请输入完整信息");
                }else if(!password.equals(surePassword)){
                    showToast("请确认密码是否一致");
                }else if(password.length()<6||password.length()>10){
                    showToast(getStrById(R.string.pwd_length_err));
                }else {
                    loadingDialog(true);
                    presenter.doChangePwd(this, mail, StringUtils.str2MD5(password), captcha, new IResult() {
                        @Override
                        public void doChange(Object result) {
                            ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                            loadingDialog(false);
                            showAppDialog(DIALOG_MODE_1,myResponse.getMessage());
                        }
                    });
                }
                break;
        }
    }
}
