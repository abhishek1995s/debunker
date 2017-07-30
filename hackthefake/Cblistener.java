package com.hackthefake.oui.hackthefake;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.ClipboardManager;
import android.util.Log;
import android.widget.Toast;

public class Cblistener extends Service {
    private MonitorTask mTask = new MonitorTask();
    private ClipboardManager mCM;

    public Cblistener() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        mCM = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mTask.start();
    }


    @Override
    public void onDestroy() {
        mTask.cancel();
    }

    @Override
    public void onStart(Intent intent, int startId) {
    }

    /**
     * Monitor task: monitor new text clips in global system clipboard and
     * new image clips in browser download directory
     */
    private class MonitorTask extends Thread {

        private volatile boolean mKeepRunning = false;
        private String mOldClip = null;


        public MonitorTask() {
            super("Cblistener");
        }

        /**
         * Cancel task
         */
        public void cancel() {
            mKeepRunning = false;
            interrupt();
        }

        @Override
        public void run() {
            mKeepRunning = true;

            while (true) {
                doTask();

                if (!mKeepRunning) {
                    break;
                }
            }

        }

        private void doTask() {
            if (mCM.hasText()) {
                String newClip = mCM.getText().toString();
                if (!newClip.equals(mOldClip)) {

                    mOldClip = newClip;
                    Toast.makeText(getApplicationContext(), "" +  newClip.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "new text clip inserted: " + newClip.toString());
                }
            }
        }
    }
}