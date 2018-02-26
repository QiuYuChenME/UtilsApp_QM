package com.qyc.bluetoothlibrary;

public class ParsedAd {
    public byte flags;
    public String localName;
    public String manufacturer;
    public String mac;
    public String productCode;//产品号
    public String deviceNum;//设备号
    public String bleFirmware;//蓝牙固件版本
    public String marker;//标志符
    public byte bleMode;//蓝牙模式
    // public List<String> uuids;

    @Override
    public String toString() {
        return "ParsedAd{" +
                "flags=" + flags +
                ", localName='" + localName + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", mac='" + mac + '\'' +
                ", productCode='" + productCode + '\'' +
                ", deviceNum='" + deviceNum + '\'' +
                ", bleFirmware='" + bleFirmware + '\'' +
                ", marker='" + marker + '\'' +
                ", bleMode=" + bleMode +
                '}';
    }
}
