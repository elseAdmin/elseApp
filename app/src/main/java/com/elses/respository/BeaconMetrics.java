package com.elses.respository;

public class BeaconMetrics{
    public int rssi;
    public Long timestamp;
    public String name;
    public int txPower;
    public int distance;

    public BeaconMetrics(){

    }

    public BeaconMetrics(String name, int rssi,int txPower,int distance){
        this.rssi=rssi;
        this.txPower=txPower;
        this.distance=distance;
        this.name=name;
        this.timestamp=System.currentTimeMillis();
    }

}
