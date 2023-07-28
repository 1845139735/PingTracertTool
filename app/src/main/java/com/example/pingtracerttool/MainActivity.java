package com.example.pingtracerttool;

import static com.example.pingtracerttool.ToolHandler.getDns;
import static com.example.pingtracerttool.ToolHandler.getGateWay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

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


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        switchButton.setOnClickListener(v -> {
            if (searchButton.getText().equals("Ping")) {
                searchButton.setText("Tracert");
            } else {
                searchButton.setText("Ping");
            }
        });

        searchButton.setOnClickListener(v -> {
            if (searchButton.getText().equals("Ping")) {
                String text = inputText.getText().toString();
                if (ToolHandler.isIP(text)) {
                    Log.d("MainActivity", "isIP");
                    resultList.clear();
                    flashUI();
                    executePing(text);
                } else {
                    Toast.makeText(this, "请输入正确的Ip地址或域名", Toast.LENGTH_SHORT).show();
                }


            } else {
            }
        });

        gateWayButton.setOnClickListener(v->{
            inputText.setText(getGateWay(this));
        });
        dnsButton.setOnClickListener(v->{
            inputText.setText(getDns(this));
        });
        baiduButton.setOnClickListener(v->{
            inputText.setText("www.baidu.com");
        });
        delButton.setOnClickListener(v->{
            inputText.setText("");
        });

    }

    private void executePing(final String ipAddress) {
        new Thread(() -> {
            try {
                Process process = Runtime.getRuntime().exec("ping -c 4 ".concat(ipAddress));
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                Log.d("Thread", "1");
                while ((line = inputStream.readLine()) != null) {
                    Log.d("Stream", line);
                    resultList.add(line);
                    flashUI();
                }
                process.waitFor();
                inputStream.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
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
}