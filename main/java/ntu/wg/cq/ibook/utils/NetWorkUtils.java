package ntu.wg.cq.ibook.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import ntu.wg.cq.ibook.app.Constants;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络模块
 * Created by C_Q on 2018/1/28.
 */
public class NetWorkUtils {

    /**
     * 判断网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetWorkAvailable(Context context){
        return QMUIDisplayHelper.hasInternet(context);
    }

    /**
     * POST请求
     * @param api:请求接口
     * @param params:请求参数
     * @return
     */
    public static String post(String api, Map<String, String> params){
        //Log.d("method","POST");
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBody.add(entry.getKey(),entry.getValue());
            Log.d(entry.getKey(),entry.getValue());
        }
        Request.Builder builder = new Request.Builder().url(api);
        builder.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        builder.addHeader("Accept-Encoding","gzip, deflate");
        builder.addHeader("User-Agent:","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:58.0) Gecko/20100101 Firefox/58.0");
        builder.addHeader("Host","api.uwere.top:8080");
        Request request = builder.post(formBody.build()).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String result = response.body().string();
                Log.d("result",result);
                return result;
            }else {
                return Constants.REQUEST_ERROR;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Constants.REQUEST_ERROR;
        }
    }

    /**
     * GET请求
     * @param api:请求接口
     * @param params:请求参数
     * @return
     */
    public static String get(String api, Map<String, String> params){
       // Log.d("method","GET");
        StringBuffer stringBuffer =new StringBuffer(api);
        stringBuffer.append("?").append(StringUtils.getRequestData(params));
        OkHttpClient httpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(stringBuffer.toString());
        builder.addHeader("Accept","Accept: application/json, text/javascript, */*; q=0.01");
        builder.addHeader("Accept-Encoding","gzip, deflate");
        builder.addHeader("User-Agent:","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:58.0) Gecko/20100101 Firefox/58.0");
        builder.addHeader("Host","api.uwere.top:8080");
        builder.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        builder.addHeader("Referer","http://api.uwere.top:8080/server/");
        Request request = builder.build();
        Log.d("get_url",stringBuffer.toString());
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()){
                String result = response.body().string();
                Log.d("result",result);
                return result;
            }else {
                return Constants.REQUEST_ERROR;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Constants.REQUEST_ERROR;
        }
    }

    public static Bitmap img(String api) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(api);
        Request request = builder.build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()){
                return ImgUtils.inputStream2Bitmap(response.body().byteStream());
            }else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}