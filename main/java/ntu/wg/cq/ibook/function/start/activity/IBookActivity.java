package ntu.wg.cq.ibook.function.start.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.function.bookshelf.view.fragment.BookshelfFragment;
import ntu.wg.cq.ibook.function.discover.view.fragment.DiscoverFragment;
import ntu.wg.cq.ibook.function.user.view.fragment.MineFragment;
import ntu.wg.cq.ibook.mvp.view.BaseActivity;
import ntu.wg.cq.ibook.utils.NetWorkUtils;

/**
 * Created by C_Q on 2018/2/6.
 */

public class IBookActivity extends BaseActivity {
    private QMUITopBar topBar;
    private RadioButton rbShelf;
    private RadioButton rbDiscover;
    private RadioButton rbMine;
    private MineFragment mineFragment;
    private DiscoverFragment discoverFragment;
    private BookshelfFragment bookshelfFragment;
    @Override
    public int bindLayout() {
        return R.layout.activity_ibook;
    }

    @Override
    protected void initView() {
        topBar=findView(R.id.top_bar);
        topBar.setTitle(R.string.bookshelf);
        rbShelf=findView(R.id.rb_shelf);
        rbDiscover=findView(R.id.rb_discover);
        rbMine=findView(R.id.rb_mine);
        replaceFragment(R.id.fl_show,new BookshelfFragment());
        rbShelf.setOnClickListener(this);
        rbDiscover.setOnClickListener(this);
        rbMine.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mineFragment = new MineFragment();
        bookshelfFragment = new BookshelfFragment();
        discoverFragment = new DiscoverFragment();
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
        if (!NetWorkUtils.isNetWorkAvailable(this)){
            new QMUIDialog.MessageDialogBuilder(this)
                    .setMessage("网络不可用")
                    .addAction("确认", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        }
    }

    @Override
    public void click(View v) {
        switch (v.getId()){
            case R.id.rb_shelf:
                topBar.setTitle(R.string.bookshelf);
                replaceFragment(R.id.fl_show,bookshelfFragment);
                break;
            case R.id.rb_discover:
                topBar.setTitle(R.string.discover);
                replaceFragment(R.id.fl_show,discoverFragment);
                break;
            case R.id.rb_mine:
                topBar.setTitle(R.string.mine);
                replaceFragment(R.id.fl_show,mineFragment);
                break;
        }
    }

    /**
     * 使一组按钮第一个不可点击
     * @param buttons
     */
    /*private void setButtonClickAble(RadioButton... buttons){
        int len=buttons.length;
        if(len>0){
            buttons[0].setClickable(false);
            for(int p=1;p<buttons.length;p++){
                buttons[p].setClickable(true);
            }
        }
    }*/
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("再按一次退出看书吧");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
