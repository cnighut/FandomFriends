package com.app.lenovo.fandomfriends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;



public class ProximityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub
        // The reciever gets the Context & the Intent that fired the broadcast as arg0 & agr1

        String k=LocationManager.KEY_PROXIMITY_ENTERING;
        // Key for determining whether user is leaving or entering

        boolean state=arg1.getBooleanExtra(k, false);
        //Gives whether the user is entering or leaving in boolean form-

        if(state){
            // Call the Notification Service or anything else that you would like to do here
            Toast.makeText(arg0, "Welcome to my Area", Toast.LENGTH_LONG).show();
        }else{
            //Other custom Notification
            Toast.makeText(arg0, "Thank you for visiting my Area,come back again !!", Toast.LENGTH_LONG).show();

        }

    }


}

