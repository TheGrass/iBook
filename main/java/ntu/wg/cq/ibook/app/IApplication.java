package ntu.wg.cq.ibook.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import ntu.wg.cq.ibook.pojo.Book;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/2/7.
 */

public class IApplication extends Application {
    public Context context = this;
    private Book item;

    public static IApplication getApp(){
        return new IApplication();
    }
    public Book getItem() {
        return item;
    }

    public void setItem(Book item) {
        this.item = item;
    }

    //////////////////////////配置

    /**
     *保存配置
     * @param key
     * @param value
     */
    public void addSetting(String key,String value){
        SharedPreferences setting = getSharedPreferences("setting",MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * 读取配置
     * @param key
     * @return
     */
    public String getSetting(String key){
        SharedPreferences setting = getSharedPreferences("setting",MODE_PRIVATE);
        return  setting.getString(key,"");
    }
    ///////////////////////////////////end

    /////////////////////////////  缓存
    public  void putCache(String key,String value){
        SharedPreferences cache = getSharedPreferences("cache",MODE_PRIVATE);
        SharedPreferences.Editor editor = cache.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public  void putCache(String key, Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String temp=new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT));
        putCache(key,new String(temp));
    }
    public  String getStringCache(String key){
        SharedPreferences cache = getSharedPreferences("cache",MODE_PRIVATE);
        return  cache.getString(key,"");
    }
    public  Bitmap getBitmapCache(String key){
        String temp =  getStringCache(key);
        if(StringUtils.haveEmpty(temp)){
            return null;
        }else {
            byte[] bytes = Base64.decode(temp.getBytes(),1);
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }
    }

    public  void clear(){
        SharedPreferences cache = getSharedPreferences("cache",MODE_PRIVATE);
        SharedPreferences.Editor editor = cache.edit();
        editor.clear();
        editor.commit();
    }
    ////////////////////////////end
}
