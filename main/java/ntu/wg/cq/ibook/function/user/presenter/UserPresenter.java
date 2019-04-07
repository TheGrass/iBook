package ntu.wg.cq.ibook.function.user.presenter;

import android.app.Activity;

import ntu.wg.cq.ibook.app.API;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.mvp.model.BaseModel;
import ntu.wg.cq.ibook.mvp.presenter.BasePresenter;
import ntu.wg.cq.ibook.utils.Params;
import ntu.wg.cq.ibook.interfaces.IResult;

/**
 * Created by C_Q on 2018/2/3.
 */

public class UserPresenter extends BasePresenter {

    @Override
    protected BaseModel getModel() {
        return new BaseModel();
    }

    public void doLogin(Activity activity,String mail, String pwd, IResult callback){
        Params params =new Params();
        params.setActivity(activity);
        params.setMethod(Constants.POST);
        params.setApi(API.LOGIN);
        params.add("u",mail);
        params.add("p",pwd);
        getModel().getResponse(params,callback);
    }

    public void getCaptcha(Activity activity,String mail,IResult callback){
        Params params = new Params();
        params.setActivity(activity);
        params.setMethod(Constants.GET);
        params.setApi(API.CAPTCHA);
        params.add("mail",mail);
        getModel().getResponse(params,callback);
    }

    public void doRegister(Activity activity,String mail,String pwd,String captcha,IResult callback){
        Params params =new Params();
        params.setActivity(activity);
        params.setMethod(Constants.POST);
        params.setApi(API.REGISTER);
        params.add("mail",mail);
        params.add("p",pwd);
        params.add("c",captcha);
        getModel().getResponse(params,callback);
    }

    public void doChangePwd(Activity activity,String mail,String pwd,String captcha,IResult callback){
        Params params =new Params();
        params.setActivity(activity);
        params.setMethod(Constants.POST);
        params.setApi(API.PASSWORD);
        params.add("mail",mail);
        params.add("p",pwd);
        params.add("c",captcha);
        getModel().getResponse(params,callback);
    }

    public void doLogout(Activity activity,String token,IResult callback){
        Params params = new Params();
        params.setActivity(activity);
        params.setMethod(Constants.POST);
        params.setApi(API.LOGOUT);
        params.add("token",token);
        getModel().getResponse(params,callback);
    }
    public void doAdd(Activity activity,String name,String author,IResult callback){
        Params params = new Params();
        params.setActivity(activity);
        params.setMethod(Constants.POST);
        params.setApi(API.ADD_BOOK);
        params.add("name",name);
        params.add("author",author);
        getModel().getResponse(params,callback);
    }
}
