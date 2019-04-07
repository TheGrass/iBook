package ntu.wg.cq.ibook.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.interfaces.IResult;

/**
 * Created by C_Q on 2018/2/3.
 */

public class AppLoadTask {
    private IResult callBack;

    public void setCallBack(IResult callBack){
        this.callBack=callBack;
    }

    public void doInBackground(final Params params) {
        final String method = params.getMethod();
        final Activity activity = params.getActivity();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(Constants.GET.equals(method)){  //GET
                     final String result = NetWorkUtils.get(params.getApi(), params.getParam());
                     uiTask(activity,result);
                }else if(Constants.POST.equals(method)){  //POST
                    final String result = NetWorkUtils.post(params.getApi(), params.getParam());
                    uiTask(activity,result);
                }else if(Constants.IMG.equals(method)){  //获取图片
                    final Bitmap result = NetWorkUtils.img(params.getApi());
                    uiTask(activity,result);
                }
            }
        }).start();
    }
    private void uiTask(Activity activity,final Object result){
        if(null !=activity){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBack.doChange(result);
                }
            });
        }
    }
}
