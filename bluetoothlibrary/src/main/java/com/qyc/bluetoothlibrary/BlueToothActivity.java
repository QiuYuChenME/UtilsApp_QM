package com.qyc.bluetoothlibrary;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BlueToothActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private String TAG = getClass().getName();
    private Handler mHandler = new Handler();
    private boolean mScanning;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private ArrayList<BluetoothDevice> list = new ArrayList<>();
    private HashSet<BluetoothDevice> hashSet = new HashSet<>();
    private ArrayList<byte[]> listbyte = new ArrayList<>();
    private ListView list_tooth;
    private ArrayAdapter sAdpter;
    private BlueToothAdapter mBlueToothAdapter;
    private int mConnectionState = STATE_DISCONNECTED;
    private HashMap<BluetoothDevice, byte[]> hashMap = new HashMap<>();
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            hashMap.put(device, scanRecord);
            hashSet.add(device);
            list.clear();
            listbyte.clear();
            list.addAll(hashMap.keySet());
            listbyte.addAll(hashMap.values());
//            parseScanRecord(scanRecord, device);

            Log.e(TAG, "onLeScan: " + list.size() + listbyte.size());
            mBlueToothAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
        scanLeDevice(true);
    }

    private void initView() {
        list_tooth = (ListView) findViewById(R.id.lv_bluetooth);
        sAdpter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        mBlueToothAdapter = new BlueToothAdapter(this, list, listbyte);
        list_tooth.setAdapter(mBlueToothAdapter);
        list_tooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BlueToothActivity.this, list.get(position).toString(), Toast.LENGTH_SHORT).show();
                list.get(position).connectGatt(BlueToothActivity.this, false, mGattCallback);
            }
        });
    }

    /**
     * 扫描附近蓝牙设备
     *
     * @param enable
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback() {
                /**
                 * 连接时回调
                 * @param gatt
                 * @param status
                 * @param newState
                 */
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {
                    Log.e(TAG, "onConnectionStateChange: " + status + " " + newState);
                    if (newState == BluetoothProfile.STATE_CONNECTED) {

                        boolean b = gatt.discoverServices();
                        Log.e(TAG, "onConnectionStateChange: " + b);
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

                    }
                }

                @Override
                // New services discovered
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    Log.e(TAG, "onServicesDiscovered: " + status);
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        List<BluetoothGattService> services = gatt.getServices();
                        for (BluetoothGattService service : services) {
                            Log.e(TAG, "onServicesDiscovered: service: UUID" + service.getUuid() + "Type" + service.getType());
                            List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                            for (BluetoothGattCharacteristic characteristic : characteristics
                                    ) {
                                boolean b = gatt.setCharacteristicNotification(characteristic, true);
                                Log.e(TAG, "onServicesDiscovered: characteristic:UUID" + characteristic.getUuid() + "Descriptors" + characteristic.getDescriptors().toString() + "boolean" + b);
                            }
                        }
                    } else {
                        Log.w(TAG, "onServicesDiscovered received: " + status);
                    }
                }

                @Override
                // Result of a characteristic read operation
                public void onCharacteristicRead(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic,
                                                 int status) {

                    Log.e(TAG, "onCharacteristicRead: " + status);

                    if (status == BluetoothGatt.GATT_SUCCESS) {
//                        broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                    }
                }

                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                    super.onCharacteristicChanged(gatt, characteristic);
                    Log.e(TAG, "onCharacteristicChanged: " + characteristic.getValue());
                }
            };

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
     * @param device
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private ParsedAd parseScanRecord(byte[] scanRecord, BluetoothDevice device) {
        ParsedAd parsedAd = null;
        try {
            parsedAd = parseScaleData(scanRecord);
            Log.d("mLeScanCallback", "parsedAd.flags:" + parsedAd.flags
                    + " parsedAd.manufacturer:" + parsedAd.manufacturer
                    + " parsedAd.localName:" + parsedAd.localName
                    + " parsedAd.mac:" + parsedAd.mac
                    + " parsedAd.productCode:" + parsedAd.productCode);
            if (parsedAd.productCode != null) {
                Log.d(TAG, "getAddress:" + device.getAddress()
                        + " getBondState:" + device.getBondState()
                        + " getName:" + device.getName()
                        + " getType:" + device.getType()
                        + " getUuids:" + device.getUuids());
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
        return parsedAd;
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
