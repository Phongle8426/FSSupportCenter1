package com.example.fssupportcenter.Modules;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fssupportcenter.Object.ObjectHistoryTeam;
import com.example.fssupportcenter.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapterRecyclerView extends RecyclerView.Adapter{
    private RecyclerViewClickInterface recyclerViewClickInterface;
    List<ObjectHistoryTeam> historyList;
    String time;
    long hours,minute;
     public HistoryAdapterRecyclerView(List<ObjectHistoryTeam> historyList, RecyclerViewClickInterface recyclerViewClickInterface){
         this.historyList = historyList;
         this.recyclerViewClickInterface = recyclerViewClickInterface;
     }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        ViewAdapterClass viewAdapterClass = new ViewAdapterClass(view);
        return viewAdapterClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewAdapterClass viewAdapterClass = (ViewAdapterClass)holder;
        Date d1 = null;
        Date d2 = null;
        ObjectHistoryTeam history = historyList.get(position);
        viewAdapterClass.name.setText(history.getUser_name());
        DateFormat df = new SimpleDateFormat("HH'h'mm'-'dd/MM/yy");
        String date = df.format(Calendar.getInstance().getTime());
        try {
             d1 = df.parse(date);
             d2 = df.parse(history.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = d1.getTime() - d2.getTime();
        diff = diff/1000;
        check(diff);
        if (hours > 24 || (hours== 24 && minute > 0)){
            viewAdapterClass.time.setText(history.getTime());
        }else{
            time = hours+"h"+minute+"m";
            if (hours == 0)
                time = minute+"m";
            viewAdapterClass.time.setText(time + " ago.");
        }
    }
    public void check(long seconds){
         hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.SECONDS.toHours(TimeUnit.SECONDS.toDays(seconds));
         minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds)*60;
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewAdapterClass extends RecyclerView.ViewHolder {
        TextView name,time;
        public ViewAdapterClass(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
           time = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    recyclerViewClickInterface.onItemClick(position);
                }
            });
        }

    }
}

