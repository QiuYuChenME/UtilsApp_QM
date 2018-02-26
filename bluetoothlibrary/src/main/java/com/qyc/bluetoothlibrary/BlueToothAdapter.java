package com.qyc.bluetoothlibrary;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.CharArrayWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

/**
 * Created by QYC on 2018/1/12.
 */

public class BlueToothAdapter extends BaseAdapter {
    private static final String TAG = BlueToothAdapter.class.getName();
    private Context mContext;
    private List<BluetoothDevice> list;
    private List<byte[]> listbyte;
    private LayoutInflater layoutInflater;

    public BlueToothAdapter(Context mContext, List<BluetoothDevice> list, List<byte[]> listbyte) {
        this.list = list;
        this.mContext = mContext;
        this.listbyte = listbyte;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.lv_bluetooth_item, parent, false);
            viewHolder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            viewHolder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            viewHolder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
            viewHolder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
            viewHolder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
            viewHolder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv1.setText("Name : " + list.get(position).getName());
        viewHolder.tv2.setText("UUID : " + list.get(position).getUuids());
        viewHolder.tv3.setText("Type : " + list.get(position).getType());
        viewHolder.tv4.setText("Address : " + list.get(position).getAddress());
        viewHolder.tv5.setText("BondState : " + list.get(position).getBondState());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(String.format("%02X", listbyte.get(position)));
        ParsedAd parsedAd = parseScaleData(listbyte.get(position));
        viewHolder.tv6.setText("scanRecord : " + parsedAd.toString());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv1, tv2, tv3, tv4, tv5, tv6;

    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 解析广播数据
     *
     * @param scanRecord
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void parseScanRecord(byte[] scanRecord) {
        try {
            ParsedAd parsedAd = parseScaleData(scanRecord);
            Log.d("mLeScanCallback", "parsedAd.flags:" + parsedAd.flags
                    + " parsedAd.manufacturer:" + parsedAd.manufacturer
                    + " parsedAd.localName:" + parsedAd.localName
                    + " parsedAd.mac:" + parsedAd.mac
                    + " parsedAd.productCode:" + parsedAd.productCode);
            if (parsedAd.productCode != null) {
                if (parsedAd.manufacturer != null
                        && (parsedAd.manufacturer.equals("1102")
                        || parsedAd.manufacturer.equals("0211"))) {// 0211泰凌Telink
                } else if (parsedAd.manufacturer != null
                        && (parsedAd.manufacturer.equals("C000")
                        || parsedAd.manufacturer.equals("00C0"))) {// 00C0笙科AMICCOM
                    // Electronics
                    // Corporation
                } else {
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "mLeScanCallback onLeScan e:" + e);
        }
    }

    /**
     * 解析FH7010广播
     *
     * @param adData
     * @return
     */
    public static ParsedAd parseScaleData(byte[] adData) {
        ParsedAd parsedAd = new ParsedAd();
        ByteBuffer buffer = ByteBuffer.wrap(adData).order(ByteOrder.LITTLE_ENDIAN);
        while (buffer.remaining() > 2) {
            byte length = buffer.get();
            if (length == 0)
                break;
            byte type = buffer.get();
            length -= 1;
            switch (type) {
                case (byte) 0x01: // Flags
                    parsedAd.bleMode = buffer.get();
                    length--;
                    break;
                case 0x09: // Complete local device name
                    byte sb[] = new byte[length];
                    buffer.get(sb, 0, length);
                    length = 0;
                    parsedAd.localName = new String(sb).trim();
                    Log.d("parseScaleData", "localName:" + parsedAd.localName);
                    break;
                case (byte) 0xFF:
                    byte[] bytes = new byte[2];
                    buffer.get(bytes, 0, 2);
                    StringBuilder stringBuilder = new StringBuilder(2);
                    for (byte aByte : bytes) {
                        stringBuilder.append(String.format("%02X", aByte));
                    }
                    String trim = stringBuilder.toString().trim();
                    if ("7010".equals(trim)) {
                        //设备号
//                        byte[] productCode = new byte[2];
//                        buffer.get(productCode, 0, 2);
//                        StringBuilder deviceBuilder = new StringBuilder(2);
//                        for (byte b : productCode) {
//                            deviceBuilder.append(String.format("%02X", b));
//                        }
                        parsedAd.productCode = trim;
                        length -= 2;
                        //蓝牙固件版本
                        byte[] bleFirmVer = new byte[2];
                        buffer.get(bleFirmVer, 0, 2);
                        StringBuilder bleFirmBuilder = new StringBuilder(2);
                        for (byte b : bleFirmVer) {
                            bleFirmBuilder.append(String.format("%02X", b));
                        }
                        parsedAd.bleFirmware = bleFirmBuilder.toString();
                        length -= 2;
                        //标志符
                        byte[] marker = new byte[1];
                        buffer.get(marker, 0, 1);
                        parsedAd.marker = String.format("%02X", marker[0]);
                        length -= 1;
                        //mcu-app

                        Log.d("parseScaleData",
                                "productCode:" + parsedAd.productCode
                                        + " bleFirmware:" + parsedAd.bleFirmware
                                        + " marker:" + parsedAd.marker);
//                    } else if (length == 8) {
                    } else {
                        // Manufacturer Specific Data
//                        byte[] manufacturerByte = new byte[2];
//                        buffer.get(manufacturerByte, 0, 2);
//                        StringBuilder manufacturerStringBuilder = new StringBuilder(2);
//                        for (byte byteChar : manufacturerByte)
//                            manufacturerStringBuilder.append(String.format("%02X", byteChar));
                        parsedAd.manufacturer = trim;
                        length -= 2;
                        // mac
                        byte[] macByte = new byte[6];
                        buffer.get(macByte, 0, 6);
                        StringBuilder macStringBuilder = new StringBuilder(6);
                        for (byte byteChar : macByte)
                            macStringBuilder.append(String.format("%02X", byteChar));
                        parsedAd.mac = macStringBuilder.toString();
                        // buffer.position(buffer.position() + macByte.length);
                        length -= 6;
                        Log.d("parseScaleData", "manufacturer:" + parsedAd.manufacturer + " mac:" + parsedAd.mac);
                    }
                    break;
                default: // skip
                    break;
            }
            if (length > 0) {
                buffer.position(buffer.position() + length);
            }
        }
        return parsedAd;
    }
}
