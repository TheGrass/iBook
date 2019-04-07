package ntu.wg.cq.ibook.function.discover.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.function.search.presenter.SearchPresenter;
import ntu.wg.cq.ibook.interfaces.IResult;
import ntu.wg.cq.ibook.mvp.view.BaseActivity;
import ntu.wg.cq.ibook.pojo.Book;
import ntu.wg.cq.ibook.utils.ImgUtils;

/**
 * Created by C_Q on 2018/2/28.
 */

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.SearchViewHolder> {
    private List<Book> data;
    private SearchPresenter presenter;
    private Activity activity;
    public BookListAdapter(List<Book> data, Activity activity){
        this.data=data;
        this.activity = activity;
        presenter = new SearchPresenter();
    }
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_book_3,parent,false);
        SearchViewHolder viewHolder =new SearchViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.tvName.setText(data.get(position).getName());
        presenter.getImg(activity, data.get(position).getImage(), new IResult() {
                @Override
                public void doChange(Object result) {
                    //ImgUtils.setBitmapAndCache(holder.imageView,result,imgKey,activity);\
                    if(result == null){
                        holder.imageView.setImageResource(R.mipmap.default_pic);
                    }else {
                        holder.imageView.setImageBitmap((Bitmap)result);
                    }
                }
            });
        holder.itemView.setOnClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tvName;
        public SearchViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.iv_book_pic);
            tvName = (TextView)itemView.findViewById(R.id.tv_book_name);
        }
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private View.OnClickListener onItemClickListener;

}
