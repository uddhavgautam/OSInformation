package com.example.uddhav.memoryuse;

import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static ProgressBar progressBar;
    private static Handler mainHandler;
    private Thread thread;
    private TextView percentTextView;

    public void setUsedMemory(final double usedPart) {
        final double usedPercentage = 100 * usedPart;
        Log.i("percentUsed", usedPercentage + "");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress((int) usedPercentage);
                percentTextView.setText("" + (double) Math.round(usedPercentage * 100.0000) / 100.0000);


                try {
                    thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }

                startProcessing();
            }
        });
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        percentTextView = (TextView) findViewById(R.id.textView2);

        mainHandler = new Handler(getMainLooper());
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startProcessing();
    }

    private void startProcessing() {
        //get the usedMem of usedMemory/totalMemory and paint the ProgressBar based on this usedMem
        progressBar.setMax(100); //min is always 0
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("used", getUsedMemoryPercent() + "");
                setUsedMemory(getUsedMemoryPercent());
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private double getUsedMemoryPercent() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(mi);

        double percentAvail = (double) mi.availMem / (double) mi.totalMem;
        double percentUsed = 1 - percentAvail;
        return percentUsed;
    }

}
