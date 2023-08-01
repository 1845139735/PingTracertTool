package com.example.pingtracerttool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHistory;
    private HistoryAdapter historyAdapter;

    private List<PingResultInfo> pingResultList; // 假设这里是历史记录的数据列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerViewHistory = findViewById(R.id.recyclerView_history);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar_history);
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
        toolbar.setTitle("历史记录");
        setSupportActionBar(toolbar);
        // 添加返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 假设这里有一个方法来获取历史记录的数据列表
        pingResultList = getHistoryData();



        // 创建并设置适配器
        historyAdapter = new HistoryAdapter(pingResultList, new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PingResultInfo pingResult) {
                // 处理点击事件，打开详细信息活动
                openDetailedInfoActivity(pingResult);
            }
        });
        recyclerViewHistory.setAdapter(historyAdapter);
    }

    // 假设这里有一个方法来获取历史记录的数据列表
    private List<PingResultInfo> getHistoryData() {
        // 返回历史记录的数据列表
        // 这里可以从SharedPreferences、数据库或其他来源获取历史记录数据
        List<PingResultInfo> pingResults = PingResultManager.getPingResults(this);
        //倒转列表
        List<PingResultInfo> pingResultsReverse = new ArrayList<>();
        for (int i = pingResults.size() - 1; i >= 0; i--) {
            pingResultsReverse.add(pingResults.get(i));
        }
        pingResults = pingResultsReverse;
        return pingResults; // 示例：返回空的列表
    }

    private void openDetailedInfoActivity(PingResultInfo pingResult) {
        // 处理打开详细信息活动的逻辑
        // 在此方法中启动一个新的活动（详细信息活动），并传递选中的Ping结果数据
        // 可以使用Intent来传递数据给详细信息活动
        Intent intent = new Intent(HistoryActivity.this, DetailedInfoActivity.class);
        intent.putExtra("pingResult", pingResult.toSerializedString());
        startActivity(intent);

    }


}