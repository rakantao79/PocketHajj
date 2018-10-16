package zamboanga.antao.pockethajj.DataSchedule;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import zamboanga.antao.pockethajj.DataUsers.DataScheduleOfActivities;
import zamboanga.antao.pockethajj.R;

/**
 * Created by abdulrahmanantao on 02/09/2017.
 */

public class AdapterSched extends RecyclerView.Adapter<ViewHolderSched>{

    List<DataScheduleOfActivities> list = Collections.emptyList();
    Context context;

    public AdapterSched(List<DataScheduleOfActivities> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolderSched onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_sched_layout, parent, false);
        ViewHolderSched holder = new ViewHolderSched(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolderSched holder, int position) {

        holder.title.setText(list.get(position).title);
        holder.content.setText(list.get(position).content);
        holder.date.setText(list.get(position).date);
        holder.time.setText(list.get(position).time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void insert(DataScheduleOfActivities data) {
        list.add(data);
        notifyItemInserted(list.size());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
