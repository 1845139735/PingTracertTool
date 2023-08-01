package com.example.pingtracerttool;

import android.text.TextUtils;

import java.util.List;

public class PingResultInfo {
    private String ip;
    private long totalTime;
    private long startTime;
    private int score;
    private int sendCount;
    private int receiveCount;
    private double lossRate;
    private double shortestTime;
    private double longestTime;
    private double averageTime;

    public PingResultInfo(String ip, long totalTime, long startTime, int score, int sendCount, int receiveCount, double lossRate, double shortestTime, double longestTime, double averageTime) {
        this.ip = ip;
        this.totalTime = totalTime;
        this.startTime = startTime;
        this.score = score;
        this.sendCount = sendCount;
        this.receiveCount = receiveCount;
        this.lossRate = lossRate;
        this.shortestTime = shortestTime;
        this.longestTime = longestTime;
        this.averageTime = averageTime;
    }

    public PingResultInfo(String ipAddress, long startTime, long endTime, List<PingData> pingDataList) {
        this.ip = ipAddress;
        this.startTime = startTime;
        this.totalTime = endTime - startTime;
        this.score = 0;
        this.sendCount = pingDataList.size();
        this.receiveCount = 0;
        this.lossRate = 0;
        this.shortestTime = 0;
        this.longestTime = 0;
        this.averageTime = 0;

        if(pingDataList.size() == 0){
            return;
        }

        int lossCount = 0;
        double totalTime = 0;
        double shortestTime = 0;
        double longestTime = 0;
        for (PingData pingData : pingDataList) {
            if(pingData.getState() == 1){
                this.receiveCount++;
                totalTime += pingData.getTime();
                if(shortestTime == 0 || shortestTime > pingData.getTime()){
                    shortestTime = pingData.getTime();
                }
                if(longestTime == 0 || longestTime < pingData.getTime()){
                    longestTime = pingData.getTime();
                }
            }else {
                lossCount++;
            }
        }
        this.lossRate = lossCount * 1.0 / pingDataList.size();
        this.shortestTime = shortestTime;
        this.longestTime = longestTime;
        this.averageTime = totalTime / this.receiveCount;
        if(averageTime<=20)score=100;
        else if(averageTime<=50)score=80;
        else if(averageTime<=100)score=60;
        else if(averageTime<=200)score=40;
        else if(averageTime<=500)score=20;
        else score=0;
        score= (int) (score*(1-lossRate));
    }

    @Override
    public String toString() {
        return this.ip+" 的Ping统计信息\n"+
                "\t数据包：已发送 = "+this.sendCount+"，已接收 = "+this.receiveCount+"，丢失 = "+(this.sendCount - this.receiveCount)+" ("+String.format("%.0f",this.lossRate*100)+"% 丢失)，\n"+
                "往返行程的估计时间(以ms为单位)：\n"+
                "\t最短 = "+String.format("%.2f",this.shortestTime)+"ms，最长 = "+String.format("%.2f",this.longestTime)+"ms，平均 = "+String.format("%.2f",this.averageTime)+"ms";
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public double getLossRate() {
        return lossRate;
    }

    public void setLossRate(double lossRate) {
        this.lossRate = lossRate;
    }

    public double getShortestTime() {
        return shortestTime;
    }

    public void setShortestTime(double shortestTime) {
        this.shortestTime = shortestTime;
    }

    public double getLongestTime() {
        return longestTime;
    }

    public void setLongestTime(double longestTime) {
        this.longestTime = longestTime;
    }

    public double getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(double averageTime) {
        this.averageTime = averageTime;
    }

    public String toSerializedString() {
        // Use TextUtils.join() to join the fields with a delimiter (semicolon)
        // Format: ip;totalTime;startTime;score;sendCount;receiveCount;lossRate;shortestTime;longestTime;averageTime
        String[] data = {
                ip,
                String.valueOf(totalTime),
                String.valueOf(startTime),
                String.valueOf(score),
                String.valueOf(sendCount),
                String.valueOf(receiveCount),
                String.valueOf(lossRate),
                String.valueOf(shortestTime),
                String.valueOf(longestTime),
                String.valueOf(averageTime)
        };
        return TextUtils.join(",", data);
    }

    public static PingResultInfo fromSerializedString(String serializedData) {
        if (TextUtils.isEmpty(serializedData)) {
            return null;
        }

        String[] data = serializedData.split(",");
        if (data.length != 10) {
            return null;
        }

        String ip = data[0];
        long totalTime = Long.parseLong(data[1]);
        long startTime = Long.parseLong(data[2]);
        int score = Integer.parseInt(data[3]);
        int sendCount = Integer.parseInt(data[4]);
        int receiveCount = Integer.parseInt(data[5]);
        double lossRate = Double.parseDouble(data[6]);
        double shortestTime = Double.parseDouble(data[7]);
        double longestTime = Double.parseDouble(data[8]);
        double averageTime = Double.parseDouble(data[9]);

        return new PingResultInfo(ip, totalTime, startTime, score, sendCount, receiveCount, lossRate, shortestTime, longestTime, averageTime);
    }

}
