package ntu.wg.cq.ibook.utils;

import android.app.Activity;
import android.util.Base64;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by C_Q on 2018/2/3.
 *  用于构建网络请求
 *  method  请求方式
 *  api 接口
 *  param 参数
 */

public class Params {
    private String method;
    private String api;
    private Activity activity;
    private Map param = new HashMap();

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map getParam() {
        return param;
    }
    public Object get(String key){
        return this.param.get(key);
    }
    public void add(String key,String value) {
        this.param.put(key,value);
    }
    public void add(String key,int value){
        add(key,value+"");
    }
    public String getApi() {
        return api;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setApi(String api) {
        this.api = api;
    }
}
