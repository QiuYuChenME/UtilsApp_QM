package com.qyc.bluetoothlibrary;

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
            Log.e(TAG, "onLeScan: " + list.size() + listbyte.size());
            mBlueToothAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        initView();
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
                                Log.e(TAG, "onServicesDiscovered: characteristic:UUID" + characteristic.getUuid() + "Descriptors" + characteristic.getDescriptors().toString());
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
                    Log.e(TAG, "onCharacteristicChanged: " + characteristic.toString());
                }
            };

    public static String bytesToHexString(byte[] src){
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
