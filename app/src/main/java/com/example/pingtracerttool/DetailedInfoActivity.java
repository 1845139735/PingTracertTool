package com.example.pingtracerttool;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailedInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private GridView gridViewPingResult;
    private GridViewAdapter gridViewAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);

        toolbar = findViewById(R.id.toolbar_history);
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
        toolbar.setTitle("详细信息");
        setSupportActionBar(toolbar);
        // 添加返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        gridViewPingResult = findViewById(R.id.gridView_ping_result);
        gridViewAdapter = new GridViewAdapter(this);
        gridViewPingResult.setAdapter(gridViewAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            String serializedData = intent.getStringExtra("pingResult");
            PingResultInfo pingResult = PingResultInfo.fromSerializedString(serializedData);
            if (pingResult != null) {
                TextView domainTextView = findViewById(R.id.textView_title);
                domainTextView.setText(pingResult.getIp());

                long startTime = pingResult.getStartTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedStartTime = sdf.format(new Date(startTime));

                List<String> gridValues = Arrays.asList(
                        pingResult.getTotalTime() + "ms",
                        formattedStartTime,
                        String.valueOf(pingResult.getScore()),
                        String.valueOf(pingResult.getSendCount()),
                        String.valueOf(pingResult.getReceiveCount()),
                        String.format("%.2f%%", pingResult.getLossRate() * 100),
                        String.format("%.2fms", pingResult.getShortestTime()),
                        String.format("%.2fms", pingResult.getLongestTime()),
                        String.format("%.2fms", pingResult.getAverageTime())
                );

                gridViewAdapter.setValues(gridValues);
            }
        }
    }
}
