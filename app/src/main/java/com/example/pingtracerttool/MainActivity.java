package com.example.pingtracerttool;

import static com.example.pingtracerttool.ToolHandler.getDns;
import static com.example.pingtracerttool.ToolHandler.getGateWay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private EditText inputText;
    private Button searchButton;
    private Button gateWayButton;
    private Button dnsButton;
    private Button baiduButton;
    private ImageView delButton;
    private ImageView switchButton;
    private RecyclerView recyclerView;
    private final List<String> resultList = new ArrayList<>();
    private final PingResultAdapter adapter = new PingResultAdapter(resultList);

    private SettingData settingData = new SettingData("32", "100", "100");
    private final List<PingData> pingDataList = new ArrayList<>();

    private boolean isInterrupted = false; // 中断标志变量

    private GridView gridViewPingResult;
    private GridViewAdapter gridViewAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//设置toolbar
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);//设置toolbar标题样式
        inputText = findViewById(R.id.editText_search);
        searchButton = findViewById(R.id.button_search);
        switchButton = findViewById(R.id.button_switch);
        recyclerView = findViewById(R.id.recyclerView_info);
        gateWayButton = findViewById(R.id.button_gateWay);
        dnsButton = findViewById(R.id.button_dns);
        baiduButton = findViewById(R.id.button_baidu);
        delButton = findViewById(R.id.button_delEdit);

        switchButton.setClickable(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        gridViewPingResult = findViewById(R.id.gridView_ping_result);
        gridViewAdapter = new GridViewAdapter(this);
        gridViewPingResult.setAdapter(gridViewAdapter);

        switchButton.setOnClickListener(v -> {
            if (searchButton.getText().equals("Ping")) {
                searchButton.setText("Tracert");
            } else {
                searchButton.setText("Ping");
            }
        });


        searchButton.setOnClickListener(v -> {

            if (searchButton.getText().equals("Ping")) {//ping
                gridViewPingResult.setVisibility(View.GONE);
                resultList.clear();
                flashUI();
                String text = inputText.getText().toString();
                if (ToolHandler.isIP(text)) {
                    Log.d("MainActivity", "isIP");
                    resultList.clear();
                    searchButton.setText("Stop");
                    switchButton.setClickable(false);
                    flashUI();
                    executePing(text);
                } else {
                    Toast.makeText(this, "请输入正确的Ip地址或域名", Toast.LENGTH_SHORT).show();
                }
            } else if (searchButton.getText().equals("Tracert")) {//tracert
                gridViewPingResult.setVisibility(View.GONE);
                resultList.clear();
                flashUI();
                String text = inputText.getText().toString();
                if (ToolHandler.isIP(text)) {
                    Log.d("MainActivity", "isIP");
                    resultList.clear();
                    searchButton.setText("Stop");
                    switchButton.setClickable(false);
                    flashUI();
                    executeTracert(text);
                } else {
                    Toast.makeText(this, "请输入正确的Ip地址或域名", Toast.LENGTH_SHORT).show();
                }
            } else if (searchButton.getText().equals("Stop")) {//中断
                isInterrupted = true;
            }
        });

        gateWayButton.setOnClickListener(v -> {
            inputText.setText(getGateWay(this));
        });
        dnsButton.setOnClickListener(v -> {
            inputText.setText(getDns(this));
        });
        baiduButton.setOnClickListener(v -> {
            inputText.setText("www.baidu.com");
        });
        delButton.setOnClickListener(v -> {
            inputText.setText("");
        });

    }

    //设置toolbar菜单
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    //toolbar菜单点击事件
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                showSettingsDialog();
                break;
            case R.id.menu_history:
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            default:
        }
        return true;
    }

    //执行ping命令
    private void executePing(final String ipAddress) {
//        new Thread(() -> {
//            try {
//                Process process = Runtime.getRuntime().exec("ping -c 1 -t 1 ".concat(ipAddress));
//                BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String line;
//                Log.d("Thread", "1");
//                String result = "";
//                while ((line = inputStream.readLine()) != null) {
//                    Log.d("Stream", line);
//
//                    result += line + "\n";
//
//                }
//                PingData pingData = new PingData(result);
//                Log.d("Thread", pingData.toString());
//                Log.d("Thread", "over");
//                resultList.add(pingData.toString());
//                flashUI();
//                process.waitFor();
//                inputStream.close();
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
        new Thread(() -> {
            try {
                pingDataList.clear();
                resultList.clear();
                resultList.add("Ping " + ipAddress + ":");
                flashUI();
                StringBuilder command = new StringBuilder();
                command.append("ping -c 1 -W 2").append(" -s ").append(settingData.getBytes()).append(" ").append(ipAddress);
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < Integer.parseInt(settingData.getCount()); i++) {
                    Thread.sleep(Integer.parseInt(settingData.getInterval()));
                    Process process = Runtime.getRuntime().exec(command.toString());
                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    String result = "";
                    while ((line = inputStream.readLine()) != null) {
                        result += line + "\n";
                    }
                    if (isInterrupted) { // 检测中断标志变量的值
                        Log.d("MainActivity", "Ping执行已中断");
                        break;
                    }
                    process.waitFor();
                    inputStream.close();
                    if (isInterrupted) { // 检测中断标志变量的值
                        Log.d("MainActivity", "Ping执行已中断");
                        break;
                    }
                    Log.d("PingData", result);
                    PingData pingData = new PingData(result);
                    pingDataList.add(pingData);
//                    resultList.add(result);
                    resultList.add(i + 1 + "." + pingData.toString());
                    flashUI();
                }
                long endTime = System.currentTimeMillis();
                PingResultInfo pingResultInfo = new PingResultInfo(ipAddress, startTime, endTime, pingDataList);
                PingResultManager.savePingResult(MainActivity.this, pingResultInfo);
                resultList.add(pingResultInfo.toString());
                isInterrupted = false;
//                searchButton.setText("Ping");
//                switchButton.setClickable(true);



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> gridValues = new ArrayList<>();
                        gridValues.add(pingResultInfo.getTotalTime() + "ms");
                        long startTime = pingResultInfo.getStartTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\n HH:mm:ss", Locale.getDefault());
                        String formattedStartTime = sdf.format(new Date(startTime));
                        gridValues.add(formattedStartTime);
                        gridValues.add(String.valueOf(pingResultInfo.getScore()));
                        gridValues.add(String.valueOf(pingResultInfo.getSendCount()));
                        gridValues.add(String.valueOf(pingResultInfo.getReceiveCount()));
                        gridValues.add(String.format("%.2f%%", pingResultInfo.getLossRate() * 100));
                        gridValues.add(String.format("%.2fms", pingResultInfo.getShortestTime()));
                        gridValues.add(String.format("%.2fms", pingResultInfo.getLongestTime()));
                        gridValues.add(String.format("%.2fms", pingResultInfo.getAverageTime()));

                        gridViewAdapter.setValues(gridValues);
                        GridView gridView = findViewById(R.id.gridView_ping_result);
                        gridView.setVisibility(View.VISIBLE);
                        TextView textView = findViewById(R.id.button_search);
                        textView.setText("Ping");
                        ImageView button = findViewById(R.id.button_switch);
                        button.setClickable(true);
                    }
                });
                flashUI();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    //执行tracert命令
    private void executeTracert(final String ipAddress) {
        new Thread(() -> {
            try {
                resultList.add("Tracert " + ipAddress + " 通过最多30个跃点跟踪");
                flashUI();
                for (int i = 1; i <= 30; i++) {
                    String resultLineTracert = "";
                    TracertData tracertData = new TracertData();
                    for (int j = 1; j <= 3; j++) {

                        StringBuilder command = new StringBuilder();
                        command.append("ping -c 1").append(" -s ").append(settingData.getBytes()).append(" -W 2 ").append("-t ").append(i).append(" ").append(ipAddress);
                        Process process = Runtime.getRuntime().exec(command.toString());
                        BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        String result = "";
                        while ((line = inputStream.readLine()) != null) {
                            result += line + "\n";
                        }
                        if (isInterrupted) { // 检测中断标志变量的值
                            Log.d("MainActivity", "Tracert执行已中断");
                            resultList.add("Tracert执行已中断");
                            flashUI();
                            break;
                        }
                        process.waitFor();
                        inputStream.close();
                        if (isInterrupted) { // 检测中断标志变量的值
                            Log.d("MainActivity", "Tracert执行已中断");
                            resultList.add("Tracert执行已中断");
                            flashUI();
                            break;
                        }
                        PingData pingData = new PingData(result);
                        Log.d("PingData", result);
                        tracertData.addPingData(pingData);

                    }
                    if (isInterrupted) {
                        break;
                    }
                    resultList.add(i + "  " + tracertData.toString());
                    flashUI();
                    if (tracertData.isFinished()) {
                        resultList.add("找到服务器，tracert结束");
                        break;
                    } else if (i == 30) {
                        resultList.add("通过30个跃点跟踪，未找到服务器，tracert结束");
                        break;
                    }
                }
                isInterrupted = false;
//                searchButton.setText("Tracert");
//                switchButton.setClickable(true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = findViewById(R.id.button_search);
                        textView.setText("Tracert");
                        ImageView button = findViewById(R.id.button_switch);
                        button.setClickable(true);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();

    }


    //更新UI
    private void flashUI() {
        new Handler(Looper.getMainLooper()).post(() -> {
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(resultList.size() - 1);
        });
    }

    //显示设置对话框
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_settings, null);
        builder.setView(dialogView);

        EditText editBytes = dialogView.findViewById(R.id.editText_bytes);
        EditText editCount = dialogView.findViewById(R.id.editText_count);
        EditText editInterval = dialogView.findViewById(R.id.editText_interval);

        // 设置默认值为当前的settingData的值
        editBytes.setText(settingData.getBytes());
        editCount.setText(settingData.getCount());
        editInterval.setText(settingData.getInterval());

        builder.setPositiveButton("确定", (dialog, which) -> {
            String bytes = editBytes.getText().toString();
            String count = editCount.getText().toString();
            String interval = editInterval.getText().toString();

            // 更新settingData对象的值
            settingData = new SettingData(bytes, count, interval);
        });

        builder.setNegativeButton("取消", (dialog, which) -> {
            // 取消操作，不进行任何更改
        });

        builder.create().show();
    }

    // 添加网络变化监听器
    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {// 网络变化时执行
            if(searchButton.getText().equals("Stop")&&isInterrupted==false) {
                isInterrupted = true; // 设置中断标志
                Log.d("MainActivity", "网络变化，中断执行");
                resultList.add("网络变化，中断执行");
                flashUI();
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        // 注册网络变化监听器
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);// 网络变化时执行
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 解除注册网络变化监听器
        unregisterReceiver(networkChangeReceiver);
    }
}