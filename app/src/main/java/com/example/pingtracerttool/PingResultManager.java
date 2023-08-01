package com.example.pingtracerttool;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class PingResultManager {

    private static final String PING_RESULT_PREFS = "ping_result_prefs";
    private static final String KEY_PING_RESULTS = "ping_results";

    public static void savePingResult(Context context, PingResultInfo pingResult) {
        List<PingResultInfo> pingResults = getPingResults(context);
        if (pingResults == null) {
            pingResults = new ArrayList<>();
        }
        pingResults.add(pingResult);

        SharedPreferences prefs = context.getSharedPreferences(PING_RESULT_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_PING_RESULTS, serializePingResults(pingResults));
        editor.apply();
    }

    public static List<PingResultInfo> getPingResults(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PING_RESULT_PREFS, Context.MODE_PRIVATE);// MODE_PRIVATE: 只有当前的应用程序才可以对这个SharedPreferences文件进行读写
        String serializedData = prefs.getString(KEY_PING_RESULTS, null);// KEY_PING_RESULTS: 用于存储ping结果的键

        if (serializedData != null) {
            return deserializePingResults(serializedData);
        }

        return new ArrayList<>();
    }

    private static String serializePingResults(List<PingResultInfo> pingResults) {
        StringBuilder stringBuilder = new StringBuilder();
        for (PingResultInfo pingResult : pingResults) {
            stringBuilder.append(pingResult.toSerializedString()).append(";");
        }
        return stringBuilder.toString();
    }

    private static List<PingResultInfo> deserializePingResults(String serializedData) {
        List<PingResultInfo> pingResults = new ArrayList<>();
        String[] dataArray = serializedData.split(";");
        for (String data : dataArray) {
            pingResults.add(PingResultInfo.fromSerializedString(data));
        }
        return pingResults;
    }
}