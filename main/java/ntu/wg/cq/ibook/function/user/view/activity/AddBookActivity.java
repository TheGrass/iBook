package ntu.wg.cq.ibook.function.user.view.activity;

import android.view.View;
import android.widget.TextView;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.custom.InputView;
import ntu.wg.cq.ibook.custom.TopBar;
import ntu.wg.cq.ibook.function.user.presenter.UserPresenter;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.view.BaseNetActivity;
import ntu.wg.cq.ibook.pojo.ResponseMsg;
import ntu.wg.cq.ibook.utils.JsonUtil;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/3/31.
 */

public class AddBookActivity extends BaseNetActivity {
    private InputView inName;
    private InputView inAuthor;
    private TopBar topBar;
    private TextView tvAdd;
    private UserPresenter presenter;
    @Override
    protected UserPresenter getPresenter() {
        return new UserPresenter();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_add;
    }

    @Override
    protected void initView() {
        inName = findView(R.id.input_book);
        inAuthor = findView(R.id.input_author);
        topBar = findView(R.id.top_bar);
        tvAdd = findView(R.id.tv_add_book);
        tvAdd.setOnClickListener(this);

    }

    @Override
    protected void initData() {
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
        topBar.setTitle("添书");
        topBar.setLeftButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        inName.init("书名","请输入书名");
        inAuthor.init("作者","请输入作者名");
    }

    @Override
    public void click(View v) {
        switch (v.getId()){
            case R.id.tv_add_book:
                String inNameInput = StringUtils.urlEncoding(inName.getInput());
                String inAuthorInput = StringUtils.urlEncoding(inAuthor.getInput());
                if (inAuthorInput.length()>0&&inNameInput.length()>0){
                    loadingDialog(true);
                    presenter.doAdd(this, inNameInput, inAuthorInput, new IResult() {
                        @Override
                        public void doChange(Object result) {
                            ResponseMsg.MyResponse myResponse = JsonUtil.parseJson(result);
                            if (myResponse.getState() == Constants.SUCCESS){
                                inName.clear();
                                inAuthor.clear();
                                loadingDialog(false);
                                showToast(myResponse.getMessage());
                            }else {
                                loadingDialog(false);
                                showToast(myResponse.getMessage());
                            }
                        }
                    });
                }else {
                    showToast("请输入信息");
                }
        }
    }
}
