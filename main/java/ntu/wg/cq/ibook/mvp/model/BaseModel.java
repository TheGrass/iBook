package ntu.wg.cq.ibook.mvp.model;

import ntu.wg.cq.ibook.utils.AppLoadTask;
import ntu.wg.cq.ibook.utils.Params;
import ntu.wg.cq.ibook.interfaces.IResult;

/**
 * Created by C_Q on 2018/1/28.
 */
public class BaseModel{

    public void getResponse(Params params, IResult callBack){
        AppLoadTask task = new AppLoadTask();
        task.setCallBack(callBack);
        task.doInBackground(params);
    }
}