package com.example.pingtracerttool;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingData {
    private int state;//0:超时 1:正常 2：Time to live exceeded
    private String ip;
    private String sendIp;



    private int bytes;
    private double time;
    private int ttl;

    public PingData() {
        this.state = 0;
        this.ip = "";
        this.bytes = 0;
        this.time = 0;
        this.ttl = 0;
        this.sendIp = "";
    }
    public PingData(String info) {
        this.state = 0;
        this.ip = "";
        this.bytes = 0;
        this.time = 0;
        this.ttl = 0;

        if(info.equals("")){
            return;
        }

        this.state = 1;
        Pattern ipAddressPattern = Pattern.compile("\\((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\)");
        Matcher ipAddressMatcher = ipAddressPattern.matcher(info);
        if (ipAddressMatcher.find()) {
            this.ip = ipAddressMatcher.group(1);
//            if(ipAddressMatcher.find()){
//                this.sendIp = ipAddressMatcher.group(1);
//            }else {
//                this.state = 0;
//                return;
//            }
        }else {
            this.state = 0;
            return;
        }
//        bytes\sfrom\s(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})

        Pattern sendIpPattern = Pattern.compile("rom\\s(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
        Matcher sendIpMatcher = sendIpPattern.matcher(info);
        if (sendIpMatcher.find()) {
            this.sendIp = sendIpMatcher.group(1);
        }else {
            this.state = 0;
            return;
        }




        Pattern bytesPattern = Pattern.compile("(\\d+)\\(\\d+\\)\\s+bytes");
        Matcher bytesMatcher = bytesPattern.matcher(info);
        if (bytesMatcher.find()) {
            this.bytes = Integer.parseInt(bytesMatcher.group(1));
        }

        Pattern ttlPattern = Pattern.compile("ttl=(\\d+)");
        Matcher ttlMatcher = ttlPattern.matcher(info);
        if (ttlMatcher.find()) {
            this.ttl = Integer.parseInt(ttlMatcher.group(1));
        }

        Pattern timePattern = Pattern.compile("time=(\\d+(\\.\\d+)?)\\sms");
        Matcher timeMatcher = timePattern.matcher(info);
        if (timeMatcher.find()) {
            this.time = Double.parseDouble(timeMatcher.group(1));
        }

        Pattern timeOutPattern = Pattern.compile("Time to live exceeded");
        Matcher timeOutMatcher = timeOutPattern.matcher(info);
        if(timeOutMatcher.find()){
            this.state = 2;
        }




    }

    @Override
    public String toString() {
        if(this.state==0)
            return "请求超时";
        else if(this.state==2){
            return "来自 "+this.sendIp+" 的回复:\nTTL超时";
        }
        return "来自 "+this.sendIp+" 的回复:\n字节="+this.bytes+" 时间="+this.time+"ms TTL="+this.ttl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSendIp() {
        return sendIp;
    }

    public void setSendIp(String sendIp) {
        this.sendIp = sendIp;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
}
