package ntu.wg.cq.ibook.function.read.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;
import java.util.List;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;
import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.function.read.adapter.CatalogAdapter;
import ntu.wg.cq.ibook.function.read.adapter.ReadFragmentAdapter;
import ntu.wg.cq.ibook.function.read.presenter.ReadPresenter;
import ntu.wg.cq.ibook.function.read.view.fragment.ReadFragment;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.interfaces.ITextSize;
import ntu.wg.cq.ibook.mvp.view.BaseNetActivity;
import ntu.wg.cq.ibook.pojo.Barrage;
import ntu.wg.cq.ibook.pojo.Catalog;
import ntu.wg.cq.ibook.pojo.Content;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.utils.JsonUtil;
import ntu.wg.cq.ibook.utils.NetWorkUtils;
import ntu.wg.cq.ibook.utils.StringUtils;
import ntu.wg.cq.ibook.utils.ViewUtils;

/**
 * Created by C_Q on 2018/2/8.
 */

public class ReadActivity extends BaseNetActivity {
    private Activity activity = this;
    private boolean flag;
    private RelativeLayout rlBackground;//背景
    private QMUITopBar topBar;  //顶部
    private RadioGroup bottomBar; //底部
    private ViewPager viewPager; //阅读
    private String sizeMode;//字体大小模式
    private int lines; //行数
    private int count; //每行字数
    private float textSize;//字体大小
    private ReadFragmentAdapter adapter; //内容adapter
    private View view; //呼出top|bottom bar
   // private View left; //左翻页
  //  private View right; //右翻页
    private LinearLayout linearLayout;
    private RadioButton rbShowCatalog; //目录
    private RadioButton rbNight; //夜间模式
    private RadioButton rbSetting; //设置
    private RadioButton rbBarrage; //弹幕
   // private boolean isClick = false; //是否 点击翻页
    private boolean isNight = false; //是否夜间模式
    private boolean isBarrage = false; //是否开启弹幕
    private int backGround; //背景
    private int light; //亮度
    private CatalogAdapter catalogAdapter;  //目录列表adapter
    private List<Fragment> list = new ArrayList<>();//阅读页面列表
    private List<Content> contentList = new ArrayList<>(); //内容列表
    //存储传递参数
    private String title; //书名
    private int bookId; //书id
    private int position; //阅读位置
    private String catalogStr; //目录json
    private List<Catalog> catalogList; //目录列表
    private ReadPresenter presenter;

    ///////设置内控件
    private LinearLayout linearLayoutSetting; //设置布局
    private SeekBar seekBar; //亮度控制
    private TextView tvSize_1; //字号1
    private TextView tvSize_2; //字号2
    private TextView tvSize_3; //字号3
 //   private TextView tvClick;  //点击翻页
 //   private TextView tvSwipe; // 滑动翻页
    private ImageView ivBackground_1;  //背景1
    private ImageView ivBackground_2; //背景2
    private ImageView ivBackground_3; //背景3
    private ImageView ivBackground_4; //背景4
    private Button buttonClose; //关闭设置界面
    //////end
    //弹幕
    private DanmakuView danmakuView;
   // private LinearLayout llBarrageSendView;
    private TextView barrageSwitch;
  //  private EditText barrageContent;
  //  private TextView barrageSend;
    private List<Barrage> barrageList = new ArrayList<>();
    private DanmakuContext danmakuContext;
    private BaseDanmakuParser parser;
    //end
    @Override
    protected ReadPresenter getPresenter() {
        return new ReadPresenter();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_read;
    }

    @Override
    protected void initView() {
        rlBackground = findView(R.id.rl);
        linearLayout = findView(R.id.ll_click);
        view = findViewById(R.id.view);
        //left = findViewById(R.id.left);
        //right = findViewById(R.id.righ);
        view.setOnClickListener(this);
        //left.setOnClickListener(this);
       // right.setOnClickListener(this);
        topBar=findView(R.id.top_bar);
        topBar.setTitle(title);
        Button button = topBar.addLeftTextButton("返回",R.layout.empty_button);
        button.setTextColor(getColorById(R.color.text_color_3));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bottomBar=findView(R.id.bottom_bar);
        viewPager=findView(R.id.vp_read);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gonTopAndBottom();
                goneSetting();
                return false;
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {

            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
                switch (arg0) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        flag= false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        flag = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !flag) {
                            if (position == catalogList.size()-1){
                                showToast("没有东西了");
                            }else {
                                position++;
                                setPosition();
                                initContent(position,1);
                            }
                        }
                        if (viewPager.getCurrentItem() == 0 && !flag){
                            if (position==0){
                                showToast("已经是第一页啦");
                            }else {
                                initContent(position,0);
                                position --;
                                setPosition();
                            }
                        }
                        flag = true;
                        break;
                }
            }
        });
        rbShowCatalog=findView(R.id.rbt_catalog);
        rbNight = findView(R.id.rbt_night);
        rbSetting = findView(R.id.rbt_setting);
        rbBarrage = findView(R.id.rbt_barrage);
        rbShowCatalog.setOnClickListener(this);
        rbNight.setOnClickListener(this);
        rbSetting.setOnClickListener(this);
        rbBarrage.setOnClickListener(this);
        linearLayoutSetting = findView(R.id.ll_setting);
        buttonClose = findView(R.id.btn_close);
        /*tvClick =findView(R.id.tv_click);
        tvSwipe =findView(R.id.tv_swipe);
        tvClick.setOnClickListener(this);
        tvSwipe.setOnClickListener(this);*/
        buttonClose.setOnClickListener(this);
        seekBar = findView(R.id.seekbar);
        seekBar.setProgress(light);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                getApp().addSetting(Constants.LIGHT,progress+"");
                setLight(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ivBackground_1 = findView(R.id.iv_bg_1);
        ivBackground_2 = findView(R.id.iv_bg_2);
        ivBackground_3 = findView(R.id.iv_bg_3);
        ivBackground_4 = findView(R.id.iv_bg_4);
        ivBackground_1.setOnClickListener(this);
        ivBackground_2.setOnClickListener(this);
        ivBackground_3.setOnClickListener(this);
        ivBackground_4.setOnClickListener(this);
        tvSize_1 = findView(R.id.tv_size_1);
        tvSize_2 = findView(R.id.tv_size_2);
        tvSize_3 = findView(R.id.tv_size_3);
        tvSize_1.setOnClickListener(this);
        tvSize_2.setOnClickListener(this);
        tvSize_3.setOnClickListener(this);
        //弹幕
        initDanmukeView();
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        bookId = bundle.getInt("bookId");
        position = bundle.getInt("position");
        catalogStr = bundle.getString("catalog");
       // isClick = !getApp().getSetting(Constants.IS_CLICK).equals("0");
        isNight = !getApp().getSetting(Constants.IS_NIGHT).equals("0");
        isBarrage = !getApp().getSetting(Constants.IS_BARRAGE).equals("0");
        backGround = Integer.valueOf(getApp().getSetting(Constants.BACKGROUND));
        light = Integer.valueOf(getApp().getSetting(Constants.LIGHT));
        sizeMode = getApp().getSetting(Constants.SIZE_MODE);
        presenter = getPresenter();
        catalogList = JSON.parseArray(catalogStr,Catalog.class);
        //contentList = new ArrayList<>(catalogList.size());
        adapter=new ReadFragmentAdapter(getSupportFragmentManager(),list);
        catalogAdapter=new CatalogAdapter(this,catalogList);
        catalogAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = (Integer)v.getTag();
                setPosition();
                //TextView tvTemp = (TextView)v;
                //tvTemp.setTextColor(getColorById(R.color.text_color_1));
                initContent(position,1);
                //refreshPage(true);
                adapter.notifyDataSetChanged();
                showAppDialog(POPUP_WINDOW_DISMISS);
            }
        });
        getTextSize(sizeMode);
    }

    @Override
    protected boolean allowFullScreen() {
        return true;
    }

    @Override
    protected boolean allowScreenRoate() {
        return false;
    }

    @Override
    public void doBusiness() {
        setLight(light);
        //canClick(isClick);
        setBackground(backGround);
        if(isNight){
            setBackground(5);
        }
        initContent(position,1);
        setBarrageIsOpen(isBarrage);
    }
    @Override
    public void click(View v) {
        switch (v.getId()){
            case R.id.rbt_catalog:
                showAppDialog(DIALOG_MODE_POPUP,catalogAdapter,topBar,position);
                gonTopAndBottom();
                break;
            case R.id.rbt_night:
                gonTopAndBottom();
                if(!isNight){
                    isNight = !isNight;
                    getApp().addSetting(Constants.IS_NIGHT,"1");
                    setBackground(5);
                    showToast("已开启夜间模式");
                }else {
                    isNight = !isNight;
                    getApp().addSetting(Constants.IS_NIGHT,"0");
                    backGround = Integer.valueOf(getApp().getSetting(Constants.BACKGROUND));
                    setBackground(backGround);
                    showToast("已关闭夜间模式");
                }
                break;
            case R.id.rbt_setting:
                gonTopAndBottom();
                if (linearLayoutSetting.getVisibility() == View.GONE){
                    linearLayoutSetting.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rbt_barrage:
                gonTopAndBottom();
                final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
                builder.setTitle("发弹幕")
                        .setPlaceholder("请输入弹幕(10字)")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("发送", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                String s = builder.getEditText().getText().toString();
                                if (StringUtils.haveEmpty(s)){
                                    showToast("请输入弹幕");
                                }else if (s.length()>10){
                                    showToast("超过字数上限");
                                }else{
                                    sendBarrage(bookId,position+1,s);
                                    dialog.dismiss();
                                }
                            }
                        }).show();
                //goneBarrageSendView(false);
                break;
         /*   case R.id.barrage_send:
              //  String bContent = barrageContent.getText().toString();
                if (!StringUtils.haveEmpty(bContent)){

                }
                break;*/
            case R.id.view:
                if(topBar.getVisibility()==View.GONE){
                    topBar.setVisibility(View.VISIBLE);
                    bottomBar.setVisibility(View.VISIBLE);
                }else {
                    topBar.setVisibility(View.GONE);
                    bottomBar.setVisibility(View.GONE);
                }
                goneSetting();
                break;
            /*case R.id.left:
                gonTopAndBottom();
                goneSetting();
                viewPager.arrowScroll(View.FOCUS_LEFT);
                break;
            case R.id.righ:
                gonTopAndBottom();
                goneSetting();
                viewPager.arrowScroll(View.FOCUS_RIGHT);
                break;*/
            case R.id.btn_close:
                goneSetting();
                break;
           /* case R.id.tv_click:
                showToast("暂不支持点击翻页");
               // canClick(true);
                break;
            case R.id.tv_swipe:
                canClick(false);
                break;*/
            case R.id.iv_bg_1:
                setBorder(ivBackground_1,ivBackground_2,ivBackground_3,ivBackground_4);
                setBackground(1);
                break;
            case R.id.iv_bg_2:
                setBorder(ivBackground_2,ivBackground_1,ivBackground_3,ivBackground_4);
                setBackground(2);
                break;
            case R.id.iv_bg_3:
                setBorder(ivBackground_3,ivBackground_2,ivBackground_1,ivBackground_4);
                setBackground(3);
                break;
            case R.id.iv_bg_4:
                setBorder(ivBackground_4,ivBackground_2,ivBackground_3,ivBackground_1);
                setBackground(4);
                break;
            case R.id.tv_size_1:
                setTextSize(tvSize_1);
                break;
            case R.id.tv_size_2:
                setTextSize(tvSize_2);
                break;
            case R.id.tv_size_3:
                setTextSize(tvSize_3);
                break;
            case R.id.barrage_switch:
                isBarrage = !isBarrage;
                setBarrageIsOpen(isBarrage);
                break;
            default:
                break;
        }
    }
    /**
     * 获取内容
     * @param p 开始位置
     * @param s 章节数量
     */
    private void initContent(int p,int s){
        loadingDialog(true);
        if(!NetWorkUtils.isNetWorkAvailable(this)){
            showToast("请检查网络连接");
        }else {
            presenter.getChapters(this, catalogList.get(p).getPath(), s, new IResult() {
                @Override
                public void doChange(Object result) {
                    ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                    if(Constants.SUCCESS==myResponse.getState()){
                        contentList=JSON.parseArray(myResponse.getMessage(), Content.class);
                        refreshPage(true);
                        loadingDialog(false);
                        viewPager.setAdapter(adapter);
                    }else {
                        loadingDialog(false);
                        showToast(myResponse.getMessage());
                    }
                }
            });
        }
    }

    /**
     * 阅读位置
     */
    private void setPosition(){

        if (!StringUtils.haveEmpty(getApp().getSetting(Constants.TOKEN))){
            getApp().putCache(getApp().getSetting(Constants.TOKEN)+bookId,position+"");
        }
    }
    /**
     * 刷新页面
     * @param isRefresh  true 重新载入 false 增加
     */
    private void refreshPage(boolean isRefresh){
        if (isRefresh){
            list.clear();
        }
        for (int p = 0;p<contentList.size();p++){
            Content con = contentList.get(p);
            if (null == con.getContent()){
                continue;
            }else {
                List<String> content = StringUtils.paging(new StringBuffer(con.getContent()),lines,count);
                int size  =content.size();
                for(int i = 0;i<size;i++){
                    ReadFragment rf=new ReadFragment();
                    rf.setTextSize(new ITextSize() {
                            @Override
                            public float size() {
                                return textSize;
                            }
                        });
                    Bundle bundle=new Bundle();
                    bundle.putString(Constants.CONTENT,content.get(i));
                    bundle.putInt("this",i+1);
                    bundle.putInt("total",size);
                    rf.setArguments(bundle);
                    list.add(rf);
                }
            }
        }
        if (isBarrage){
            getBarrage();
        }
    }



    /**
     * 弹幕开关
     * @param isOpen
     */
    private void setBarrageIsOpen(boolean isOpen){
        if (isOpen){
            danmakuView.show();
        }else {
            danmakuView.hide();
        }
        getApp().addSetting(Constants.IS_BARRAGE,isOpen ? "1":"0");
        showToast("已"+(isOpen ? "开启":"关闭")+"弹幕");
        barrageSwitch.setText(isOpen ? "开启":"关闭");
       // danmakuView.setVisibility(isOpen ? View.VISIBLE : View.GONE);
    }
    /**
     * 控制弹幕发送控件
     * @param isGone
     */
    /*private void goneBarrageSendView(boolean isGone){
        if (isGone){
            llBarrageSendView.setVisibility(View.GONE);
        }else {
            llBarrageSendView.setVisibility(View.VISIBLE);
        }
    }*/
    /**
     * 隐藏顶部和底部
     */
    private void gonTopAndBottom(){
        if(topBar.getVisibility()==View.VISIBLE){
            topBar.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏设置界面
     */
    private void goneSetting(){
        if(linearLayoutSetting.getVisibility()==View.VISIBLE){
            linearLayoutSetting.setVisibility(View.GONE);
        }
        //goneBarrageSendView(true);
    }

    /**
     * 设置点击or滑动翻页
     * @param isClick
     */
    /*private void canClick(boolean isClick){
        if(isClick){
            getApp().addSetting(Constants.IS_CLICK,"1");
            linearLayout.setVisibility(View.VISIBLE);
            tvClick.setTextColor(getColorById(R.color.bg_read_1));
            tvSwipe.setTextColor(getColorById(R.color.white));
        }else {
            getApp().addSetting(Constants.IS_CLICK,"0");
            linearLayout.setVisibility(View.GONE);
            tvSwipe.setTextColor(getColorById(R.color.bg_read_1));
            tvClick.setTextColor(getColorById(R.color.white));
        }
    }*/

    /**
     * 设置屏幕亮度
     * @param brightness
     */
    private void setLight(int brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 设置背景
     * @param tag
     */
    private void setBackground(int tag){
        if(tag<5){
            getApp().addSetting(Constants.BACKGROUND,tag+"");
        }else {
            isNight = true;
        }
        switch (tag){
            case 1:
                rlBackground.setBackgroundResource(R.mipmap.read_bg_1);
                break;
            case 2:
                rlBackground.setBackgroundResource(R.mipmap.read_bg_2);
                break;
            case 3:
                rlBackground.setBackgroundResource(R.mipmap.read_bg_3);
                break;
            case 4:
                rlBackground.setBackgroundResource(R.mipmap.read_bg_5);
                break;
            case 5:
                rlBackground.setBackgroundColor(getColorById(R.color.night));
            default:
                break;
        }
    }

    /**
     * 设置边框   第一个绿  后三个灰
     * @param iv1
     * @param iv2
     * @param iv3
     * @param iv4
     */
    private void setBorder(ImageView iv1,ImageView iv2,ImageView iv3,ImageView iv4){
        iv1.setBackgroundResource(R.drawable.image_checked_border);
        iv2.setBackgroundResource(R.drawable.image_no_checked);
        iv3.setBackgroundResource(R.drawable.image_no_checked);
        iv4.setBackgroundResource(R.drawable.image_no_checked);
    }

    /**
     * 设置字体
     * @param textView
     */
    private void setTextSize(TextView textView){
        String mode = (String)textView.getTag();
        if(!sizeMode.equals(mode)){
            goneSetting();
            sizeMode = mode;
            showToast("切换为"+sizeMode+"号字体");
            getTextSize(sizeMode);
            getApp().addSetting(Constants.SIZE_MODE,sizeMode);
            refreshPage(true);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获得字体设置
     * @param mode
     */
    private void getTextSize(String mode){
        textSize = Float.valueOf(getApp().getSetting(Constants.SIZE+mode));
        Log.d(getClass().getSimpleName()+"get",getApp().getSetting(Constants.SIZE+mode));
        Log.d(getClass().getSimpleName()+"set",textSize+"");
        count=Integer.valueOf(getApp().getSetting(Constants.CHAR_COUNT+mode));
        lines=Integer.valueOf(getApp().getSetting(Constants.LINES+mode));
    }



    /**
     * 发送弹幕
     * @param bid
     * @param cid
     * @param content
     */
    private void sendBarrage(int bid,int cid,String content){
        if (StringUtils.haveEmpty(getApp().getSetting(Constants.TOKEN))){
            showAppDialog(DIALOG_MODE_TO_LOGIN);
        }else {
            addDanmaku(content,true);
            presenter.sendBarrage(this, getApp().getSetting(Constants.TOKEN), StringUtils.urlEncoding(content), bid, cid, new IResult() {
                @Override
                public void doChange(Object result) {
                    ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                    showToast(myResponse.getMessage());
                    //barrageContent.setText("");
                }
            });
        }
    }

    /**
     * 初始化弹幕
     */
    private void initDanmukeView(){
        parser =  new BaseDanmakuParser() {
            @Override
            protected IDanmakus parse() {
                return new Danmakus();
            }
        };
        danmakuView = findView(R.id.danmaku);
        //llBarrageSendView = findView(R.id.barrage_send_view);
        barrageSwitch = findView(R.id.barrage_switch);
        //barrageContent = findView(R.id.barrage_content);
        //barrageSend = findView(R.id.barrage_send);
        barrageSwitch.setOnClickListener(this);
       // barrageSend.setOnClickListener(this);
        danmakuView.enableDanmakuDrawingCache(true);
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                isBarrage = true;
                danmakuView.start();
               // generateSomeDanmaku();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        danmakuContext = DanmakuContext.create();
        danmakuView.prepare(parser, danmakuContext);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
    }
    /**
     * 添加一条弹幕
     * @param content    弹幕的具体内容
     * @param withBorder 弹幕是否有边框
     */
    private void addDanmaku(String content, boolean withBorder) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.textSize = ViewUtils.sp2px(this,20);
        danmaku.textColor = Color.WHITE;
        danmaku.setTime(danmakuView.getCurrentTime());
        if (withBorder) {
            danmaku.borderColor = Color.GREEN;
        }
        danmakuView.addDanmaku(danmaku);
    }

    /**
     * 从服务器获取弹幕
     */
    private void getBarrage(){
        presenter.getBarrage(activity, bookId, position + 1, new IResult() {
            @Override
            public void doChange(Object result) {
                ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                if (myResponse.getState() == Constants.SUCCESS){
                    barrageList.clear();
                    barrageList.addAll(JSON.parseArray(myResponse.getMessage(), Barrage.class));
                    if (barrageList!=null && barrageList.size()!=0){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (Barrage barrage:barrageList){
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    addDanmaku(StringUtils.urlDecode(barrage.getContent()),false);
                                }
                            }
                        }).start();
                    }
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isBarrage = false;
        if (danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
        }
    }
}
