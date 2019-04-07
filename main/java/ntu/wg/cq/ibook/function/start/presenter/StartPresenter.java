package ntu.wg.cq.ibook.function.start.presenter;

import ntu.wg.cq.ibook.app.API;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.function.start.model.StartModel;
import ntu.wg.cq.ibook.mvp.presenter.BasePresenter;
import ntu.wg.cq.ibook.utils.Params;
import ntu.wg.cq.ibook.interfaces.IResult;

/**
 * Created by C_Q on 2018/2/5.
 */

public class StartPresenter extends BasePresenter {
    @Override
    protected StartModel getModel() {
        return new StartModel();
    }

    //网络加载启动页图片
   /* public void doGetStartImg(IResult callBack){
        Params params=new Params();
        params.setMethod(Constants.IMG);
        params.setApi(API.START_IMG);
        getModel().doGetStartImg(params,callBack);
    }*/
}
