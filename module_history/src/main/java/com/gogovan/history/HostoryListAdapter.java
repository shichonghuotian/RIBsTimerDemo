package com.gogovan.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gogovan.data.entities.TimerTaskEntity;
import com.gogovan.utils.WUtils;

import java.util.List;

/**
 * RecyclerView adapter for displaying history
 * Created by Arthur on 2018/11/10.
 */
public class HostoryListAdapter extends RecyclerView.Adapter<HostoryListAdapter.ItemViewHolder> {

    private List<TimerTaskEntity> list;

    public HostoryListAdapter(@NonNull List<TimerTaskEntity> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timer_task,parent,
                false);

        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        TimerTaskEntity en = this.list.get(position);
        holder.setTimerTask(en);

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        //name input
        TextView nameTextView;

        //time input
        TextView timeTextView;

        public ItemViewHolder(View itemView) {

            super(itemView);

            nameTextView = itemView.findViewById(R.id.task_name_text);
            timeTextView = itemView.findViewById(R.id.time_text);
        }

        public void setTimerTask(TimerTaskEntity entity) {

            nameTextView.setText(entity.getTaskName());

            timeTextView.setText(WUtils.stringFromTime(entity.getFinishSeconds()));
        }
    }
}
