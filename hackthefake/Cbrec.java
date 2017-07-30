package com.hackthefake.oui.hackthefake;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by surajr on 7/29/17.
 */

public class Cbrec extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            ComponentName service = context.startService(
                    new Intent(context, Cblistener.class));
            if (service == null) {
                Toast.makeText(context, "Can't Start service", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "Recieved unexpected intent ", Toast.LENGTH_LONG).show();
            Log.e("TAG", "Recieved unexpected intent " + intent.toString());
        }
        Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
    }
}