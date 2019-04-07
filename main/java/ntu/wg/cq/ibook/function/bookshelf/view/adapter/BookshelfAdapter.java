package ntu.wg.cq.ibook.function.bookshelf.view.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.app.IApplication;
import ntu.wg.cq.ibook.function.search.presenter.SearchPresenter;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.pojo.Book;
import ntu.wg.cq.ibook.utils.ImgUtils;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/2/7.
 */

public class BookshelfAdapter extends RecyclerView.Adapter<BookshelfAdapter.BookshelfViewHolder> {
    private List<Book> data;
    private Activity context;
    private SearchPresenter presenter;
    private IApplication application;
    public BookshelfAdapter(List<Book> data,Activity context){
        this.data=data;
        this.context=context;
        application=(IApplication)context.getApplication();
        presenter = new SearchPresenter();
    }

    @Override
    public BookshelfViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_book_2,parent,false);
        BookshelfViewHolder viewHolder=new BookshelfViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BookshelfViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        int id = data.get(position).getId();
        final String imgKey = Constants.IMG+id;
        Bitmap temp = application.getBitmapCache(imgKey);
        if(null != temp){
            holder.imageView.setImageBitmap(temp);
        }else {
            presenter.getImg(context, data.get(position).getImage(), new IResult() {
                @Override
                public void doChange(Object result) {
                    ImgUtils.setBitmapAndCache(holder.imageView,result,imgKey,context);
                }
            });
        }
        holder.name.setText(data.get(position).getName());
        String lastPos = application.getStringCache(application.getSetting(Constants.TOKEN)+id);
        holder.chapter.setText("上次阅读到第"+ (StringUtils.haveEmpty(lastPos)?1:Integer.valueOf(lastPos)+1)+"章");
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right,holder.swipeLayout.findViewById(R.id.ll));
        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                holder.item.setClickable(false);
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }
            @Override
            public void onClose(SwipeLayout layout) {
                holder.item.setClickable(true);
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
        holder.delete.setTag(position);
        holder.delete.setOnClickListener(onDeleteClickListener);
        holder.ba.setTag(position);
        holder.ba.setOnClickListener(onBaClickListener);
        holder.item.setTag(position);
        holder.item.setOnClickListener(onClickListener);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static class BookshelfViewHolder extends RecyclerView.ViewHolder{
        SwipeLayout swipeLayout;
        RelativeLayout item;
        ImageView imageView;
        TextView name;
        TextView chapter;
        TextView delete;
        TextView ba;
        public BookshelfViewHolder(View v){
            super(v);
            swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe);
            item = (RelativeLayout) v.findViewById(R.id.item);
            delete = (TextView)v.findViewById(R.id.swipe_delete);
            ba = (TextView)v.findViewById(R.id.swipe_ba);
            imageView=(ImageView)v.findViewById(R.id.iv_book_pic);
            name=(TextView)v.findViewById(R.id.tv_book_name);
            chapter=(TextView)v.findViewById(R.id.tv_new_chapter);
        }
    }

    private View.OnClickListener onDeleteClickListener;
    private View.OnClickListener onBaClickListener;

    public void setOnBaClickListener(View.OnClickListener onBaClickListener) {
        this.onBaClickListener = onBaClickListener;
    }
    public void setOnDeleteClickListener(View.OnClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }
}
