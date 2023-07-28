package com.example.pingtracerttool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PingResultAdapter extends RecyclerView.Adapter<PingResultAdapter.PingResultViewHolder> {
    private List<String> resultList;

    public PingResultAdapter(List<String> resultList) {
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public PingResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new PingResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PingResultViewHolder holder, int position) {
        String result = resultList.get(position);
        holder.resultTextView.setText(result);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public static class PingResultViewHolder extends RecyclerView.ViewHolder {
        TextView resultTextView;

        public PingResultViewHolder(@NonNull View itemView) {
            super(itemView);
            resultTextView = itemView.findViewById(R.id.textView_item);
        }
    }
}