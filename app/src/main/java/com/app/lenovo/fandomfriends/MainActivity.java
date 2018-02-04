package com.app.lenovo.fandomfriends;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final Strategy STRATEGY = Strategy.P2P_CLUSTER;
    private ConnectionsClient connectionsClient;
    private String opponentName;
    private String profile = "Aditya";
    SharedPreferences sharedPreferences;
    String biotext = "bio";
    String bioedittext;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static   int ACCESS_FINE_LOCATION = 0;
    private  final static int ACCESS_COARSE_LOCATION = 1;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    int flag=0;
    private double currentLongitude;
    String name,lati,longi,fandom1,fandom2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        sharedPreferences = getSharedPreferences(biotext, Context.MODE_PRIVATE);
        EditText bio = (EditText) findViewById(R.id.Bio);
        bioedittext = sharedPreferences.getString("bio", "Enter your bio here");
        bio.setText(bioedittext);
        final ImageButton edit = (ImageButton) findViewById(R.id.imageButton);
        edit.setOnClickListener(new View.OnClickListener() {

            EditText bio = (EditText) findViewById(R.id.Bio);

            //int flag = 1;
            @Override
            public void onClick(View view) {
                if (!bio.isEnabled()) {
                    bio.setEnabled(true);
                    bio.setCursorVisible(true);
                    bio.setFocusableInTouchMode(true);
                    edit.setImageResource(R.drawable.nrg);
                } else {
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
    public void btRegister(View view)
    {
        /*first_name =  ET_FIRST_NAME.getText().toString();
        last_name = ET_LAST_NAME.getText().toString();
        address = ET_ADDRESS.getText().toString();
        email = ET_EMAIL.getText().toString();
        password = ET_PASSWORD.getText().toString();*/
        name="HighlyUniqueName";
        fandom1="HarryPotter";
        fandom2="Ben10";
        String method = "register";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, name,lati,longi,fandom1,fandom2);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
        //Toast.makeText(this, "In onResume()",  Toast.LENGTH_LONG).show();
    }

    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this, "Tried to connect", Toast.LENGTH_LONG).show();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(this, "Requesting...", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_COARSE_LOCATION);

                //return;
            }
            /*if(flag==0) {
                Toast.makeText(this, "Flag is 0...aborting", Toast.LENGTH_LONG).show();
                return;
            }*/
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            } else {
                //If everything went fine lets get latitude and longitude
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                lati=currentLatitude+"";
                longi=currentLongitude+"";
                //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
                TextView tv=findViewById(R.id.FandomsFollowed);
                tv.setText(currentLatitude+" , "+currentLongitude);
            }
        }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    flag++;

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                //return;
            }

            case ACCESS_FINE_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    flag++;
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                //return;
            }
        }
    }
    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
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
