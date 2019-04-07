package ntu.wg.cq.ibook.function.discover.presenter;

import android.app.Activity;

import ntu.wg.cq.ibook.app.API;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.model.BaseModel;
import ntu.wg.cq.ibook.mvp.presenter.BasePresenter;
import ntu.wg.cq.ibook.utils.Params;

/**
 * Created by C_Q on 2018/3/20.
 */

public class DiscoverPresenter extends BasePresenter {
    @Override
    protected BaseModel getModel() {
        return new BaseModel();
    }

    /**
     * 排行
     * @param activity
     * @param clazz 类别
     * @param iResult
     */
    private int page = 0;
    public void getRank(Activity activity, int clazz,boolean isRefresh, IResult iResult){
        if (isRefresh){
            page = 0;
        }else{
            page++;
        }
        Params params = new Params();
        params.setActivity(activity);
        params.setApi(API.RANK);
        params.setMethod(Constants.GET);
        params.add("clazz",clazz);
        params.add("page",page);
        getModel().getResponse(params,iResult);
    }

    /**
     * 推荐
     * @param activity
     * @param token
     * @param iResult
     */
    public void getCommend(Activity activity,String token,IResult iResult){
        Params params = new Params();
        params.setApi(API.COMMEND);
        params.setActivity(activity);
        params.setMethod(Constants.GET);
        params.add("token",token);
        getModel().getResponse(params,iResult);
    }

    /**
     * 分享
     * @param activity
     * @param token
     * @param isPublic 0私有 1 公有
     * @param iResult
     */
    public void doShare(Activity activity,String token,int isPublic,IResult iResult){
        Params params = new Params();
        params.setActivity(activity);
        params.setApi(API.DO_SHARE);
        params.setMethod(Constants.POST);
        params.add("token",token);
        params.add("pub",isPublic);
        getModel().getResponse(params,iResult);
    }

    /**
     * 获取分享
     * @param activity
     * @param token
     * @param sid   0公共
     * @param iResult
     */
    public void getShare(Activity activity,String token,String sid,IResult iResult){
        Params params = new Params();
        params.setApi(API.GET_SHARE);
        params.setMethod(Constants.GET);
        params.setActivity(activity);
        params.add("token",token);
        params.add("sid",sid);
        getModel().getResponse(params,iResult);
    }
}
