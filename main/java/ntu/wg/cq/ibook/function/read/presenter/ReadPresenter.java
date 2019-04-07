package ntu.wg.cq.ibook.function.read.presenter;

import android.app.Activity;

import ntu.wg.cq.ibook.app.API;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.model.BaseModel;
import ntu.wg.cq.ibook.mvp.presenter.BasePresenter;
import ntu.wg.cq.ibook.utils.Params;

/**
 * Created by C_Q on 2018/3/10.
 */

public class ReadPresenter extends BasePresenter {
    @Override
    protected BaseModel getModel() {
        return new BaseModel();
    }

    /**
     * 获取内容
     * @param activity
     * @param path
     * @param size
     * @param iResult
     */
    public void getChapters(Activity activity, String path, int size, IResult iResult){
        Params params =new Params();
        params.setActivity(activity);
        params.setMethod(Constants.GET);
        params.setApi(API.GET_CONTENT);
        params.add("path",path);
        params.add("size",size);
        /*params.add("token",token);*/
        getModel().getResponse(params,iResult);
    }

    /**
     * 发弹幕
     * @param activity
     * @param token
     * @param content
     * @param bid 书id
     * @param cid 章节id
     * @param iResult
     */
    public void sendBarrage(Activity activity,String token,String content,int bid,int cid,IResult iResult){
        Params params = new Params();
        params.setActivity(activity);
        params.setMethod(Constants.POST);
        params.setApi(API.SEND_BARRAGE);
        params.add("token",token);
        params.add("content",content);
        params.add("bid",bid);
        params.add("cid",cid);
        getModel().getResponse(params,iResult);
    }

    /**
     * 获取弹幕
     * @param activity
     * @param bid
     * @param cid
     * @param iResult
     */
    public void getBarrage(Activity activity,int bid,int cid,IResult iResult){
        Params params = new Params();
        params.setActivity(activity);
        params.setMethod(Constants.GET);
        params.setApi(API.GET_BARRAGE);
        params.add("bid",bid);
        params.add("cid",cid);
        getModel().getResponse(params,iResult);
    }
}
