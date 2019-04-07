package ntu.wg.cq.ibook.function.search.view.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.app.Constants;
import ntu.wg.cq.ibook.app.IApplication;
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
    private BaseActivity activity;
    public BookListAdapter(List<Book> data, BaseActivity activity){
        this.data=data;
        this.activity = activity;
        presenter = new SearchPresenter();
    }
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_book_1,parent,false);
        SearchViewHolder viewHolder =new SearchViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.tvAuthor.setText("作者: "+data.get(position).getAuthor());
        holder.tvName.setText(data.get(position).getName());
        if(data.get(position).getFavorite()>0){
            holder.tvFavorite.setVisibility(View.VISIBLE);
            holder.tvFavorite.setText("热度: "+data.get(position).getFavorite());
            holder.tvFavorite.setTextColor(0xC7FD5D2D);
        }
       // final String imgKey = Constants.IMG+data.get(position).getId();
       // final Bitmap bitmap = activity.getApp().getBitmapCache(imgKey);
       /* if (null == bitmap){
            presenter.getImg(activity, data.get(position).getImage(), new IResult() {
                @Override
                public void doChange(Object result) {
                    ImgUtils.setBitmapAndCache(holder.imageView,result,imgKey,activity);
                }
            });
        }else {
            holder.imageView.setImageBitmap(bitmap);
        }*/
       presenter.getImg(activity, data.get(position).getImage(), new IResult() {
           @Override
           public void doChange(Object result) {
               if (null != result){
                   holder.imageView.setImageBitmap((Bitmap)result);
               }else {
                   holder.imageView.setImageResource(R.mipmap.default_pic);
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
        TextView tvAuthor;
        TextView tvFavorite;
        public SearchViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.iv_book_pic);
            tvName = (TextView)itemView.findViewById(R.id.tv_book_name);
            tvAuthor = (TextView)itemView.findViewById(R.id.tv_new_chapter);
            tvFavorite  =(TextView)itemView.findViewById(R.id.tv_fav);
        }
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private View.OnClickListener onItemClickListener;

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    private int position;
}
