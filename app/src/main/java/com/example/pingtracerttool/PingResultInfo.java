package com.example.pingtracerttool;

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

    public PingResultInfo(String ipAddress, long startTime,long endTime,List<PingData> pingDataList) {
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
        this.score = (int) (this.averageTime * 100);
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
}
