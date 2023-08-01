package com.example.pingtracerttool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private Context context;

    private List<String> labels = new ArrayList<>();
    private List<String> values = new ArrayList<>();

    public GridViewAdapter(Context context) {
        this.context = context;
        labels.add("持续时间");
        labels.add("开始时间");
        labels.add("分数");
        labels.add("已发送包数量");
        labels.add("已接收包数量");
        labels.add("丢包率");
        labels.add("往返最短时间");
        labels.add("往返最长时间");
        labels.add("平均时长");

        // 初始化values为"-"，表示还没有结果
        for (int i = 0; i < labels.size(); i++) {
            values.add("-");
        }
    }

    public void setValues(List<String> newValues) {
        values.clear();
        values.addAll(newValues);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview, parent, false);

        TextView labelTextView = convertView.findViewById(R.id.textView_label);
        TextView valueTextView = convertView.findViewById(R.id.textView_value);

        labelTextView.setText(labels.get(position));
        valueTextView.setText(values.get(position));

        return convertView;
    }
}
