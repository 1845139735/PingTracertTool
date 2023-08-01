package com.example.pingtracerttool;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<PingResultInfo> pingResultList;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(PingResultInfo pingResult);
    }

    public HistoryAdapter(List<PingResultInfo> pingResultList, OnItemClickListener itemClickListener) {
        this.pingResultList = pingResultList;
        this.itemClickListener = itemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // 在此ViewHolder中定义需要显示的视图，例如TextView
        // 可以根据需要展示Ping结果的部分数据，例如IP地址和平均时间
        // 点击时调用itemClickListener.onItemClick()来处理点击事件

        TextView textIp;
        TextView textTotalTime;
        TextView textLossRate;
        TextView textStartTime;
        public ViewHolder(View itemView) {
            super(itemView);
            // 初始化ViewHolder中的视图
            textIp = itemView.findViewById(R.id.text_ip);
            textTotalTime = itemView.findViewById(R.id.text_total_time);
            textLossRate = itemView.findViewById(R.id.text_loss_rate);
            textStartTime = itemView.findViewById(R.id.text_start_time);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(itemView);
    }

    @NonNull// 避免空指针异常
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PingResultInfo pingResult = pingResultList.get(position);
        // 绑定Ping结果的数据到ViewHolder中的视图
        holder.textIp.setText(pingResult.getIp());

        // 设置总时间
        long totalTime = pingResult.getTotalTime();
        holder.textTotalTime.setText("Total Time: " + totalTime + "ms");

        // 设置丢包率
        double lossRate = pingResult.getLossRate() * 100;
        holder.textLossRate.setText("Loss Rate: " + String.format("%.2f", lossRate) + "%");

        // 设置开始时间（转换为年月日时分秒）
        long startTime = pingResult.getStartTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedStartTime = sdf.format(new Date(startTime));
        holder.textStartTime.setText("Start Time: " + formattedStartTime);





        // 设置点击监听事件
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(pingResult);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pingResultList.size();
    }



}
