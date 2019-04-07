package ntu.wg.cq.ibook.utils;

import com.alibaba.fastjson.JSON;

import ntu.wg.cq.ibook.pojo.ResponseMsg;

/**
 * Created by C_Q on 2018/3/20.
 */

public class JsonUtil {
    public static ResponseMsg.MyResponse parseJson(Object result){
        return JSON.parseObject((String) result, ResponseMsg.class).getMyResponse();
    }
}
