package com.example.naytonwilkerson.wifiandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Um BroadcastReceiver que notifica eventos P2P wifi importantes.
 */

class MyBroadcastReceiver extends BroadcastReceiver {

    private MainActivity activity;
    WifiP2pManager.Channel mChannel;
    WifiP2pManager mManager;
    TextView texto;
    private int intNumber;
    WifiP2pManager.PeerListListener myPeerListListener;
    private WifiP2pDeviceList disponiveis;



    public MyBroadcastReceiver(MainActivity activity, WifiP2pManager.Channel mChannel,WifiP2pManager mManager, int intNumber) {
        this.activity = activity;
        this.mChannel = mChannel;
        this.mManager = mManager;
        this.intNumber = intNumber;
    }



    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        //percebeu a mudanca no estado do Wi-Fi Direct
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            //Determine se o modo Wi-Fi P2P está ativado ou não, alerta, A activity.

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                    
                // Wi-Fi Direct esta habilitado
            }else{
                // Wi-Fi Direct nao esta habilitado
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            if(mManager != null){
                mManager.requestPeers(mChannel,myPeerListListener);
            }

            // Solicite peers disponíveis do gerenciador p2p do wifi. Isto é um
            // chamada assíncrona e a atividade de chamada é notificada com um
            // retorno de chamada em PeerListListener.onPeersAvailable ()
            // Chamar WifiP2pManager.requestPeers() para obter uma lista dos dispositivos disponiveis
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Responder a novas conexoes ou desconexoes
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Responder a mudancas de estado deste dispositivo
        }
    }


    public void requestPeers(WifiP2pManager.Channel mChannel, WifiP2pManager.PeerListListener listener) {

        mManager.requestPeers(mChannel, listener);

    }


    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peers) {
            disponiveis=peers;
           // activity.preencherSpinner();
        }
    };

    public void connectDevicePos(int pos) {
        // variavel 'pos' vem do evento de clique no spinner dentro da atividade principal
        List<WifiP2pDevice> deviceList = new ArrayList<WifiP2pDevice>();
        deviceList.addAll(disponiveis.getDeviceList());

        WifiP2pConfig config = new WifiP2pConfig();
        config.groupOwnerIntent = this.intNumber;
        config.deviceAddress = deviceList.get(pos).deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "List Initiated",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(activity, "List Failed : " + reason,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}



