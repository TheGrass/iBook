package ntu.wg.cq.ibook.function.bookshelf.presenter;

import android.app.Activity;

import ntu.wg.cq.ibook.app.API;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.model.BaseModel;
import ntu.wg.cq.ibook.mvp.presenter.BasePresenter;
import ntu.wg.cq.ibook.utils.Params;

/**
 * Created by C_Q on 2018/3/5.
 */

public class BookShelfPresenter extends BasePresenter{

    @Override
    protected BaseModel getModel() {
        return new BaseModel();
    }

    /**
     * 获取书架
     * @param activity
     * @param token
     * @param iResult
     */
    public void getBookShelf(Activity activity, String token, IResult iResult){
        Params params =new Params();
        params.setMethod(Constants.GET);
        params.setActivity(activity);
        params.setApi(API.GET_SHELF);
        params.add("token",token);
        getModel().getResponse(params,iResult);
    }

    /**
     * 获取目录
     * @param activity
     * @param bookId
     * @param iResult
     */
    public void getCatalogById(Activity activity, int bookId, IResult iResult){
        Params params =new Params();
        params.setMethod(Constants.GET);
        params.setActivity(activity);
        params.setApi(API.GET_CATALOG);
        params.add("id",bookId);
        getModel().getResponse(params,iResult);
    }

    /**
     * 获取内容
     * @param activity
     * @param path 根路径
     * @param size 大小
     * @param iResult
     */
    public void getChapters(Activity activity,String path,int size,IResult iResult){
        Params params =new Params();
        params.setMethod(Constants.GET);
        params.setActivity(activity);
        params.setApi(API.GET_CONTENT);
        params.add("path",path);
        params.add("size",size);
        //params.add("token",token);
        getModel().getResponse(params,iResult);
    }

    public void delete(Activity activity,String token,int bid,IResult iResult){
        Params params =new Params();
        params.setMethod(Constants.POST);
        params.setActivity(activity);
        params.setApi(API.DELETE);
        params.add("token",token);
        params.add("bid",bid);
        getModel().getResponse(params,iResult);
    }
}
