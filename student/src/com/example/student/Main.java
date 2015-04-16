package com.example.student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

public class Main extends Activity {
    private boolean PressedOnce = false;
    private AlertDialog alertDialog;
    private double normalizedX;
    private double normalizedY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instantiateDialogBox();
        //set to full screen
        setContentView(R.layout.main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        GameSurface gameSurface = new GameSurface(this);
        setContentView(gameSurface);
    }

    @Override
    public void onPause() {
        // Kill the process when user leave the app
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onBackPressed() {
        if (PressedOnce) {
            alertDialog.show();
        }

        this.PressedOnce = true;
        Toast.makeText(this, "Please click BACK again to pause.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PressedOnce = false;
            }
        }, 2000);
    }

    private void instantiateDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game paused");
        builder.setMessage("Are you want to stop?");
        builder.setCancelable(true);
        builder.setNegativeButton("Resume", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog = builder.create();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        normalizedX = normalizedX / GameControls.screen_width;
        normalizedY = normalizedY / GameControls.screen_height;
        sendCommand(normalizedX, normalizedY);
        return super.onTouchEvent(motionEvent);
    }

    public void sendCommand(double rad, double distance) {
        //TODO use messenger
        Log.w("send", Math.toDegrees(rad) + ", " + distance);
    }
}
