package com.example.pingtracerttool;

import java.util.ArrayList;
import java.util.List;

public class TracertData {
    private List<PingData> pingDataList;
    private String ip = "";

    public TracertData() {
        this.pingDataList = new ArrayList<>();
        this.ip = "";

    }

    public void addPingData(PingData pingData) {
        this.pingDataList.add(pingData);
    }

    @Override
    public String toString() {
        String result = "";
        String sendIp = "";
//        for (PingData pingData : pingDataList) {
//            result += pingData.toString() + "\n";
//        }
        for (int i = 0; i < pingDataList.size(); i++) {
            PingData pingData = pingDataList.get(i);
            if (pingData.getState() == 1) {
                result += pingData.getTime() + "ms  ";
            } else if (pingData.getState() == 2) {
                result += "2ms  ";
            }else {
                result += "*ms  ";
            }
            if(pingData.getState()!=0&&sendIp.equals(""))
                sendIp = pingData.getSendIp();
        }
        if(!sendIp.equals("")){
            result += sendIp;
        }else {
            result += "请求超时";
        }
        return result;
    }

        public boolean isFinished () {
            boolean flag = false;
            for (PingData pingData : pingDataList) {
                if ((pingData.getState() == 1) && (pingData.getSendIp().equals(pingData.getIp())))
                    flag = true;
                break;
            }
            return flag;
        }

    }
