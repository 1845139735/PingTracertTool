package com.example.pingtracerttool;

public class SettingData {
    //包大小（Byte）
    private String bytes;
    //次数
    private String count;
    //间隔（ms）
    private String interval;

    public SettingData(String bytes, String count, String interval) {
        this.bytes = bytes;
        this.count = count;
        this.interval = interval;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
