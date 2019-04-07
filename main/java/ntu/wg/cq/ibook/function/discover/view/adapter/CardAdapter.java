package ntu.wg.cq.ibook.function.discover.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.pojo.Card;
import ntu.wg.cq.ibook.utils.StringUtils;

/**
 * Created by C_Q on 2018/4/1.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Card> data;
    private Context context;
    public CardAdapter(List<Card> data,Context context){
        this.data = data;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position%2 == 0){
            holder.linearLayout.setBackgroundResource(R.color.pop_color);
        }else {
            holder.linearLayout.setBackgroundResource(R.color.bg_input);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(onClickListener);
        holder.title.setText(StringUtils.urlDecode(data.get(position).getTitle()));
        holder.content.setText(StringUtils.urlDecode(data.get(position).getContent()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView content;
        private LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            content = (TextView) itemView.findViewById(R.id.content);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ll);
        }
    }
    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
