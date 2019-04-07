package ntu.wg.cq.ibook.function.search.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.function.discover.view.activity.BaCardActivity;
import ntu.wg.cq.ibook.function.discover.view.activity.RankActivity;
import ntu.wg.cq.ibook.function.read.view.activity.ReadActivity;
import ntu.wg.cq.ibook.function.search.presenter.SearchPresenter;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.view.BaseActivity;
import ntu.wg.cq.ibook.mvp.view.BaseNetActivity;
import ntu.wg.cq.ibook.pojo.Book;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.utils.ImgUtils;
import ntu.wg.cq.ibook.utils.JsonUtil;
import ntu.wg.cq.ibook.utils.NetWorkUtils;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/3/3.
 */

public class BookInfoActivity extends BaseNetActivity {
    private BaseActivity activity=this;
    private QMUITopBar topBar;
    private Button btnBack;
    private ImageView ivPic;
    private TextView tvName;
    private TextView tvAuthor;
    private TextView tvClazz;
    private Button btnAdd;
    private Button btnRead;
    private TextView tvDescription;
    private Button btnIntoBa;
    private Book book;
    private SearchPresenter presenter;
    private String token;
    @Override
    protected SearchPresenter getPresenter() {
        return new SearchPresenter();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_bookinfo;
    }

    @Override
    protected void initView() {
        topBar = findView(R.id.top_bar);
        topBar.setTitle("书籍详情");
        btnBack = topBar.addLeftTextButton("返回",R.layout.empty_button);
        btnBack.setTextColor(getColorById(R.color.text_color_3));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivPic = findView(R.id.iv_book_pic);
        tvName = findView(R.id.tv_book_name);
        tvAuthor = findView(R.id.tv_author);
        tvClazz = findView(R.id.tv_clazz);
        tvClazz.setOnClickListener(this);
        tvDescription = findView(R.id.tv_description);
        btnAdd =findView(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnRead = findView(R.id.btn_read);
        btnRead.setOnClickListener(this);
        btnIntoBa =findView(R.id.btn_into);
        btnIntoBa.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        token = getApp().getSetting(Constants.TOKEN);
        book = getApp().getItem();
        presenter = getPresenter();
    }

    @Override
    protected boolean allowFullScreen() {
        return false;
    }

    @Override
    protected boolean allowScreenRoate() {
        return false;
    }

    @Override
    public void doBusiness() {
        tvName.setText(book.getName());
        tvAuthor.setText("作者:"+book.getAuthor());
        tvClazz.setText("类别:"+Constants.BOOK_CLAZZ[book.getClazz()-1]);
        tvDescription.setText(book.getDescription());
        tvDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (!StringUtils.haveEmpty(token)){
            presenter.userBehavior(this, token, book.getClazz(), new IResult() {
                @Override
                public void doChange(Object result) {

                }
            });
        }
        final String imgKey = Constants.IMG+book.getId();
        Bitmap temp = getApp().getBitmapCache(Constants.IMG+book.getId());
        if(null != temp){
            ivPic.setImageBitmap(temp);
        }else {
            presenter.getImg(this, book.getImage(), new IResult() {
                @Override
                public void doChange(Object result) {
                    ImgUtils.setBitmapAndCache(ivPic,result,imgKey,activity);
                }
            });
        }
    }

    @Override
    public void click(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                if(NetWorkUtils.isNetWorkAvailable(this)){
                    if(StringUtils.haveEmpty(token)){
                        showAppDialog(DIALOG_MODE_TO_LOGIN);
                    }else {
                        loadingDialog(true);
                        presenter.add2Shelf(this, token, book.getId(), new IResult() {
                            @Override
                            public void doChange(Object result) {
                                ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                                loadingDialog(false);
                                showAppDialog(DIALOG_MODE_1,myResponse.getMessage());
                            }
                        });
                    }
                }else {
                    showAppDialog(DIALOG_MODE_NO_NET);
                }
                break;
            case R.id.btn_read:
                if(NetWorkUtils.isNetWorkAvailable(this)){
                    loadingDialog(true);
                    presenter.getCatalog(this, book.getId(), new IResult() {
                        @Override
                        public void doChange(Object result) {
                            ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                            if (Constants.FAIL == myResponse.getState()){
                                loadingDialog(false);
                                showAppDialog(DIALOG_MODE_1,myResponse.getMessage());
                            }else {
                               // List<Catalog> list = JSON.parseArray(msg.getMessage(),Catalog.class);
                                if(StringUtils.haveEmpty(myResponse.getMessage())||"[]".equals(myResponse.getMessage())){
                                    loadingDialog(false);
                                    showAppDialog(DIALOG_MODE_1,"获取目录失败");
                                }else {
                                    getApp().putCache(Constants.CATALOG_KEY+book.getId(),myResponse.getMessage());
                                    Bundle bundle = new Bundle();
                                    bundle.putString("title",book.getName());
                                    bundle.putInt("bookId",book.getId());
                                    bundle.putInt("position",book.getPosition());
                                    bundle.putString("catalog",myResponse.getMessage());
                                    loadingDialog(false);
                                    startActivity(ReadActivity.class,bundle);
                                }
                            }
                        }
                    });
                }else {
                    showAppDialog(DIALOG_MODE_NO_NET);
                }
                break;
            case R.id.tv_clazz:
                Bundle bundle = new Bundle();
                bundle.putInt("tag",book.getClazz());
                startActivity(RankActivity.class,bundle);
                finish();
                break;
            case R.id.btn_into:
                Bundle bundle1 = new Bundle();
                bundle1.putString("name",book.getName());
                bundle1.putInt("id",book.getId());
                startActivity(BaCardActivity.class,bundle1);
        }
    }
}
