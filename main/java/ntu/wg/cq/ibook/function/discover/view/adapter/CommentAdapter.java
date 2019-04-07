package ntu.wg.cq.ibook.function.discover.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.pojo.Comment;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/4/1.
 */

public class CommentAdapter extends RecyclerView.Adapter <CommentAdapter.ViewHolder>{
    private List<Comment> data;
    private Context context;
    public CommentAdapter(List<Comment> data,Context context){
        this.data = data;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.uid.setText("用户：u1xxx45"+data.get(position).getUid());
        holder.content.setText(StringUtils.urlDecode(data.get(position).getContent()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView uid;
        private TextView content;
        public ViewHolder(View itemView) {
            super(itemView);
            uid = (TextView) itemView.findViewById(R.id.tv_user);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
