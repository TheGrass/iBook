package ntu.wg.cq.ibook.function.discover.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ntu.wg.cq.ibook.R;
import ntu.wg.cq.ibook.pojo.Answer;

/**
 * Created by C_Q on 2018/3/28.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    private List<Answer> data;
    private boolean[] click;
    private Context context;
    public AnswerAdapter(List<Answer> data,Context context,boolean[] click){
        this.data = data;
        this.context = context;
        this.click = click;
    }
    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_answer,parent,false);
        AnswerViewHolder viewHolder = new AnswerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AnswerViewHolder holder, int position) {
        holder.tvAnswer.setText(data.get(position).getAnswer());
        holder.tvAnswer.setTag(position);
        holder.tvAnswer.setOnClickListener(onClickListener);
        if (click[position]){
            holder.tvAnswer.setBackgroundResource(R.color.red);
        }else {
            holder.tvAnswer.setBackgroundResource(R.color.answer);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class AnswerViewHolder extends RecyclerView.ViewHolder{
        private TextView tvAnswer;
        public AnswerViewHolder(View itemView) {
            super(itemView);
            tvAnswer = (TextView)itemView.findViewById(R.id.tv_answer);
        }
    }
    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
