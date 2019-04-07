package ntu.wg.cq.ibook.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.InputStream;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.IApplication;
import ntu.wg.cq.ibook.mvp.view.BaseActivity;

/**
 * Created by C_Q on 2018/2/5.
 */

public class ImgUtils {

    /**
     *  InputStream 转换为Bitmap
     * */
    public static Bitmap inputStream2Bitmap(InputStream inputStream){
        return BitmapFactory.decodeStream(inputStream);
    }

    public static void setBitmapAndCache(ImageView imageView,Object img,String imgKey,Activity activity){
        Bitmap temp;
        if (null == img){
            temp = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.default_pic);
        }else {
            temp = (Bitmap)img;
        }
        imageView.setImageBitmap(temp);
        IApplication application=(IApplication)activity.getApplication();
        application.putCache(imgKey,temp);
    }
}
