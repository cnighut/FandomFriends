package com.app.lenovo.fandomfriends;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private static final Strategy STRATEGY = Strategy.P2P_CLUSTER;
    private ConnectionsClient connectionsClient;
    private String opponentName;
    private String profile="Aditya";
    SharedPreferences sharedPreferences;
    String biotext = "bio";
    String bioedittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(biotext, Context.MODE_PRIVATE);
        EditText bio = (EditText) findViewById(R.id.Bio);
        bioedittext = sharedPreferences.getString("bio", "Enter your bio here");
        bio.setText(bioedittext);
        final ImageButton edit=(ImageButton)findViewById(R.id.imageButton);
        edit.setOnClickListener(new View.OnClickListener() {

            EditText bio = (EditText) findViewById(R.id.Bio);
            //int flag = 1;
            @Override
            public void onClick(View view){
                if(!bio.isEnabled()) {
                    bio.setEnabled(true);
                    bio.setCursorVisible(true);
                    bio.setFocusableInTouchMode(true);
                    edit.setImageResource(R.drawable.nrg);
                }
                else{
                    String bioedit = bio.getText().toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(biotext, bioedit);
                    editor.commit();
                    bio.setEnabled(false);
                    bio.setCursorVisible(false);
                    bio.setFocusableInTouchMode(false);
                    edit.setImageResource(R.drawable.sm);

                }
            }
        });
        connectionsClient = Nearby.getConnectionsClient(this);
        startAdvertising();
        startDiscovery();
    }
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    //Log.i(TAG, "onEndpointFound: endpoint found, connecting");
                    connectionsClient.requestConnection(profile, endpointId, connectionLifecycleCallback);
                }

                @Override
                public void onEndpointLost(String endpointId) {}
            };

    private void startDiscovery() {
        // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startDiscovery(
                getPackageName(), endpointDiscoveryCallback, new DiscoveryOptions(STRATEGY));

    }
    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
                  //opponentChoice = GameChoice.valueOf(new String(payload.asBytes(), UTF_8));
                    EditText bio = (EditText) findViewById(R.id.Bio);
                    bio.setText(new String(payload.asBytes()));
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    if (update.getStatus() == PayloadTransferUpdate.Status.SUCCESS ) {
                        //finishRound();
                    }
                }
            };

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                   // Log.i(TAG, "onConnectionInitiated: accepting connection");
                    connectionsClient.acceptConnection(endpointId, payloadCallback);
                    Toast.makeText(getApplicationContext(), "Found you!", Toast.LENGTH_LONG).show();
                    opponentName = connectionInfo.getEndpointName();
                }
                @Override
                public void onDisconnected(String endpointId) {
                    //Log.i(TAG, "onDisconnected: disconnected from the opponent");
                    //resetGame();
                }
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    if (result.getStatus().isSuccess()) {
                        //Log.i(TAG, "onConnectionResult: connection successful");

                        connectionsClient.sendPayload(endpointId,Payload.fromBytes("Hi There".getBytes()));
                        connectionsClient.stopDiscovery();
                        connectionsClient.stopAdvertising();

            /*opponentEndpointId = endpointId;
            setOpponentName(opponentName);
            setStatusText(getString(R.string.status_connected));
            setButtonState(true);*/
                    } else {
                        //Log.i(TAG, "onConnectionResult: connection failed");
                    }
                }
            };


    private void startAdvertising() {
        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startAdvertising(
                profile, getPackageName(), connectionLifecycleCallback, new AdvertisingOptions(STRATEGY));
    }
       /* private void startAdvertising() {
        connectionsClient.startAdvertising()
          Nearby.getConnections(context).startAdvertising(
                  profile,
                 "com.app.lenovo.fandomfriends",
                  mConnectionLifecycleCallback,
                  new AdvertisingOptions(STRATEGY))
              .addOnSuccessListener(
                new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void unusedResult) {
                    // We're advertising!
                  }
                })
              .addOnFailureListener(
                new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                    // We were unable to start advertising.
                  }
                });
          }*/
}
