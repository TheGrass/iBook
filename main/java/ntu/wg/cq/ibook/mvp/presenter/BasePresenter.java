package ntu.wg.cq.ibook.mvp.presenter;

import android.app.Activity;

import java.lang.ref.WeakReference;

import ntu.wg.cq.ibook.app.API;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.model.BaseModel;
import ntu.wg.cq.ibook.mvp.view.BaseView;
import ntu.wg.cq.ibook.utils.Params;

/**
 * Created by C_Q on 2018/1/28.
 */
public abstract class BasePresenter<M extends BaseModel> {
    protected abstract M getModel();

    /**
     * 用户行为
     * @param activity
     * @param token
     * @param action
     * @param iResult
     */
    public void userBehavior(Activity activity, String token, int action, IResult iResult){
        Params params = new Params();
        params.setActivity(activity);
        params.setApi(API.BEHAVIOR);
        params.setMethod(Constants.POST);
        params.add("token",token);
        params.add("action",action);
        new BaseModel().getResponse(params,iResult);
    }
}