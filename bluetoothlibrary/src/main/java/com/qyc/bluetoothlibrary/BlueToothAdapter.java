package com.qyc.bluetoothlibrary;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by QYC on 2018/1/12.
 */

public class BlueToothAdapter extends BaseAdapter {
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

        viewHolder.tv6.setText("scanRecord : " + bytesToHexString(listbyte.get(position)));
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
}
