package com.example.naytonwilkerson.wifiandroid;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION;


public class MainActivity extends Activity   {


    private final IntentFilter intentFilter = new IntentFilter();
    WifiP2pManager.Channel mChannel;
    WifiP2pManager mManager;
    MyBroadcastReceiver myBroadcastReceiver;
    Button btnOnOff,btnListOnOff;
    ListView ListOnOff;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOnOff = (Button) findViewById(R.id.onOff);

        // Indicates a mudanca do status do wifi.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates uma mudanca na lista de dispositivos.
        intentFilter.addAction(WIFI_P2P_PEERS_CHANGED_ACTION);


        // Indica que o estado da conectividade Wi-Fi P2P foi alterado
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indica que os detalhes deste dispositivo foram alterados.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(),null);
        myBroadcastReceiver = new MyBroadcastReceiver(this, mChannel,mManager,15);

        SetList();

    }

    @Override
    public void onResume() {
    super.onResume();
          registerReceiver(myBroadcastReceiver, intentFilter);
     }

      @Override
     public void onPause() {
      super.onPause();
      unregisterReceiver(myBroadcastReceiver);
       }

    private void SetList() {

        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION == null) {

                    btnOnOff.setText("Wifi Abilitado = " + WifiP2pManager.EXTRA_WIFI_STATE);
                    // Wi-Fi Direct esta habilitado retorna 2
                } else {
                    btnOnOff.setText("Wifi Desabilitado = " + WifiP2pManager.EXTRA_WIFI_STATE);
                    // Wi-Fi Direct nao esta habilitado retorna 1
                }
            }
        });

        btnListOnOff = (Button) findViewById(R.id.btnListOnOff);
        ListOnOff = (ListView) findViewById(R.id.ListOnOff);


        btnListOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                      
                    }
                    @Override
                    public void onFailure(int reasonCode) {

                    }
                });

        }

        });
}

}