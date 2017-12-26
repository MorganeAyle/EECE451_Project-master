package com.example.morgane.eece451_project;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Connections extends AppCompatActivity {

    protected static Context context;
    BluetoothAdapter bluetoothadapter;

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);


        bluetoothadapter= BluetoothAdapter.getDefaultAdapter();


        TextView bt = (TextView) findViewById(R.id.btField);
        bt.setText(checkBluetoothConnection());


        TextView wifi = (TextView) findViewById(R.id.wifiField);
        wifi.setText(checkWifiConnection());

        ////////////////////////setText

        TextView cellular = (TextView) findViewById(R.id.cellularField);
        cellular.setText(checkCellularConnection());

        TextView network = (TextView) findViewById(R.id.networkField);
        network.setText(getNetworkType());

        TextView signal = (TextView) findViewById(R.id.signalField);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            signal.setText(getSignalStrength());




    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    private String checkBluetoothConnection() {
        if(bluetoothadapter.isEnabled())
            return "Bluetooth is enabled";
        else
            return "Bluetooth is disabled";

    }

    private String checkWifiConnection() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return "Connected";
        } else {
            WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (!manager.isWifiEnabled()) {
                return "Disabled";
            }
        }
        return "Not connected";
    }





    private String checkCellularConnection() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetwork = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mobileNetwork != null && mobileNetwork.isConnected())
            return "Connected";
        return "Disabled";
    }

    private String getNetworkType() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetwork = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        int networkType = mobileNetwork.getSubtype();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "GSM";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            default:
                return "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private String getSignalStrength() {
        String level = "omg";
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return "Permission not granted";
            }
            CellInfoGsm cellInfo = (CellInfoGsm)telephonyManager.getAllCellInfo().get(0);
            CellSignalStrengthGsm signalStrength = cellInfo.getCellSignalStrength();
            int asu = signalStrength.getAsuLevel();
            if (asu <= 2 || asu == 99) level = "None or Unknown";
            else if (asu >= 12) level = "Excellent";
            else if (asu >= 8) level = "Good";
            else if (asu >= 5) level = "Moderate";
            else level = "Poor";
            return level;

    }
}