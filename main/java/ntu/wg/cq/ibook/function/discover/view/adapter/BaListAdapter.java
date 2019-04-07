package ntu.wg.cq.ibook.function.discover.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.pojo.Ba;

/**
 * Created by C_Q on 2018/4/1.
 */

public class BaListAdapter extends RecyclerView.Adapter<BaListAdapter.ViewHolder> {
    private List<Ba> data;
    private Context context;
    public BaListAdapter(List<Ba> data,Context context){
        this.data =data;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ba_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setTag(position);
        if (position%2 == 0){
            holder.textView.setBackgroundResource(R.drawable.image_no_checked);
        }else {
            holder.textView.setBackgroundResource(R.drawable.image_checked_border);
        }
        holder.textView.setText(data.get(position).getBaName());
        holder.textView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.ba_name);
        }
    }

    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
