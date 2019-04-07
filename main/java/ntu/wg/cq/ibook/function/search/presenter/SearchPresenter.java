package ntu.wg.cq.ibook.function.search.presenter;

import android.app.Activity;

import ntu.wg.cq.ibook.app.API;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.model.BaseModel;
import ntu.wg.cq.ibook.mvp.presenter.BasePresenter;
import ntu.wg.cq.ibook.utils.Params;

/**
 * Created by C_Q on 2018/2/28.
 */

public class SearchPresenter extends BasePresenter {
    @Override
    protected BaseModel getModel() {
        return new BaseModel();
    }

    private int page=0;

    /**
     * 搜索书籍
     * @param activity
     * @param key
     * @param isRefresh
     * @param iResult
     */
    public void doSearch(Activity activity,String key,boolean isRefresh, IResult iResult){
        if(isRefresh){
            page=0;
        }else {
            page++;
        }
        Params params=new Params();
        params.setActivity(activity);
        params.setApi(API.SEARCH_BOOK);
        params.setMethod(Constants.GET);
        params.add("key",key);
        //params.add("token",token);
        params.add("page",page);
        getModel().getResponse(params,iResult);
    }

    /**
     * 获取封面
     * @param activity
     * @param url
     * @param iResult
     */
    public void getImg(Activity activity,String url,IResult iResult){
        Params params =new Params();
        params.setApi(url);
        params.setMethod(Constants.IMG);
        params.setActivity(activity);
        getModel().getResponse(params,iResult);
    }

    /**
     * 添加到书架
     * @param activity
     * @param token
     * @param bid
     * @param iResult
     */
    public void add2Shelf(Activity activity,String token,int bid,IResult iResult){
        Params params = new Params();
        params.setApi(API.ADD);
        params.setMethod(Constants.POST);
        params.setActivity(activity);
        params.add("token",token);
        params.add("bid",bid+"");
        params.add("pos",0);
        getModel().getResponse(params,iResult);
    }

    public void getCatalog(Activity activity,int bid,IResult iResult){
        Params params = new Params();
        params.setApi(API.GET_CATALOG);
        params.setMethod(Constants.GET);
        params.setActivity(activity);
        params.add("bid",bid);
        getModel().getResponse(params,iResult);
    }
}
