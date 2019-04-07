package ntu.wg.cq.ibook.function.user.view.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.function.user.presenter.UserPresenter;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.view.BaseNetActivity;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.pojo.Token;
import ntu.wg.cq.ibook.utils.JsonUtil;
import ntu.wg.cq.ibook.utils.NetWorkUtils;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/2/6.
 */

public class LoginActivity extends BaseNetActivity {
    private ImageView ivExit;
    private EditText edtId;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvForget;

    private String mail;
    private String pwd;

    private UserPresenter presenter;
    @Override
    protected UserPresenter getPresenter() {
        return new UserPresenter();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        ivExit=findView(R.id.iv_exit);
        edtId=findView(R.id.edt_id);
        edtPassword=findView(R.id.edt_pwd);
        btnLogin=findView(R.id.btn_login);
        tvRegister=findView(R.id.tv_register);
        tvForget=findView(R.id.tv_forget);
        ivExit.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
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
            case R.id.iv_exit:
                finish();
                break;
            case R.id.btn_login:
                mail = edtId.getText().toString();
                pwd = edtPassword.getText().toString();
                if(StringUtils.haveEmpty(mail,pwd)){
                    showToast(getStrById(R.string.null_mail_pwd));
                }else if(pwd.length()<6||pwd.length()>10){
                    showToast(getStrById(R.string.pwd_length_err));
                }else{
                    if(NetWorkUtils.isNetWorkAvailable(this)){
                        loadingDialog(true);
                        presenter.doLogin(this,mail,StringUtils.str2MD5(pwd),new IResult(){
                            @Override
                            public void doChange(Object result) {
                                ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                                loadingDialog(false);
                                if(myResponse.getState()== Constants.FAIL){
                                    showAppDialog(DIALOG_MODE_1,myResponse.getMessage());
                                }else if(myResponse.getState() == Constants.SUCCESS){
                                    getApp().addSetting(Constants.TOKEN,JSON.parseObject(myResponse.getMessage(), Token.class).getToken());
                                    getApp().addSetting(Constants.MAIL,mail);
                                    finish();
                                }
                            }
                        });
                    }else {
                        showAppDialog(DIALOG_MODE_NO_NET);
                    }
                }
                break;
            case R.id.tv_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget:
                startActivity(ForgetActivity.class);
                break;
        }
    }
}
