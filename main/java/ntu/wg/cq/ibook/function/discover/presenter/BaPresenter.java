package ntu.wg.cq.ibook.function.discover.presenter;

import android.app.Activity;

import ntu.wg.cq.ibook.app.API;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.model.BaseModel;
import ntu.wg.cq.ibook.mvp.presenter.BasePresenter;
import ntu.wg.cq.ibook.utils.Params;

/**
 * Created by C_Q on 2018/4/1.
 */

public class BaPresenter extends BasePresenter {
    @Override
    protected BaseModel getModel() {
        return new BaseModel();
    }

    /**
     * 关注
     * @param activity
     * @param token
     * @param bid
     * @param iResult
     */
    public void doFollow(Activity activity, String token, int bid, IResult iResult){
        Params params = new Params();
        params.setMethod(Constants.POST);
        params.setActivity(activity);
        params.setApi(API.BA_DO_FOLLOW);
        params.add("token",token);
        params.add("bid",bid);
        getModel().getResponse(params,iResult);
    }

    /**
     * 获取贴列表
     * @param activity
     * @param bid
     * @param isRefresh
     * @param iResult
     */
    int cardPage = 0;
    public void getCards(Activity activity,int bid, boolean isRefresh, IResult iResult){
        if (isRefresh){
            cardPage = 0;
        }else {
            cardPage += 1;
        }
        Params params = new Params();
        params.setMethod(Constants.GET);
        params.setActivity(activity);
        params.setApi(API.BA_CARDS);
        params.add("page",cardPage);
        params.add("bid",bid);
        getModel().getResponse(params,iResult);
    }

    /**
     * 获取关注列表
     * @param activity
     * @param token
     * @param iResult
     */
    public void getBas(Activity activity, String token,IResult iResult){
        Params params = new Params();
        params.setMethod(Constants.GET);
        params.setActivity(activity);
        params.setApi(API.BA_FOLLOWS);
        params.add("token",token);
        getModel().getResponse(params,iResult);
    }
    /**
     *获取评论
     * @param activity
     * @param cid
     * @param isRefresh
     * @param iResult
     */
    int comPage = 0;
    public void getComments(Activity activity,int cid, boolean isRefresh,IResult iResult){
        if (isRefresh){
            comPage = 0;
        }else {
            comPage += 1;
        }
        Params params = new Params();
        params.setMethod(Constants.GET);
        params.setActivity(activity);
        params.setApi(API.BA_COMMENTS);
        params.add("page",comPage);
        params.add("cid",cid);
        getModel().getResponse(params,iResult);
    }

    /**
     * 发帖
     * @param activity
     * @param bid
     * @param token
     * @param title
     * @param content
     */
    public void postCrd(Activity activity,int bid,String token,String title,String content,IResult iResult){
        Params params = new Params();
        params.setMethod(Constants.POST);
        params.setActivity(activity);
        params.setApi(API.BA_POST_CARD);
        params.add("bid",bid);
        params.add("token" ,token);
        params.add("title",title);
        params.add("content",content);
        getModel().getResponse(params,iResult);
    }

    /**
     * 评论
     * @param activity
     * @param cid
     * @param token
     * @param content
     */
    public void postCom(Activity activity,int cid,String token,String content,IResult iResult){
        Params params = new Params();
        params.setMethod(Constants.POST);
        params.setActivity(activity);
        params.setApi(API.BA_POST_COMMENT);
        params.add("cid",cid);
        params.add("token" ,token);
        params.add("content",content);
        getModel().getResponse(params,iResult);
    }
}
