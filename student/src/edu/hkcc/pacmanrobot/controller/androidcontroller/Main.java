package edu.hkcc.pacmanrobot.controller.androidcontroller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;
import edu.hkcc.pacmanrobot.utils.Config;
import edu.hkcc.pacmanrobot.utils.message.udpmessage.Encoder;
import edu.hkcc.pacmanrobot.utils.message.udpmessage.UDPMessengerSingleton;

import java.io.IOException;
import java.net.SocketException;

//import edu.hkcc.pacmanrobot.utils.message.MovementCommand;
//import edu.hkcc.pacmanrobot.utils.message.udpmessage.Decoder;

public class Main extends Activity {
    private boolean PressedOnce = false;
    private AlertDialog alertDialog;
    private double normalizedX;
    private double normalizedY;
    DeviceInfoManager deviceInfoManager;

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
        startManager();
    }

    private void startManager() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ok = false;
                do {
                    try {
                        deviceInfoManager = new DeviceInfoManager();
                        ok = true;
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }
                while (!ok);
                deviceInfoManager.start();
            }
        }).start();
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
        byte[] t = Encoder.getInstance().getMovementCommand((byte) 1, rad, distance);
        try {
            UDPMessengerSingleton.getInstance(new UDPMessengerSingleton.ReceiveActor() {
                @Override
                public void apply(String ip) {
                    //do nothing
                }
            }).send(t, 0, t.length, Config.PORT_MOVEMENT_COMMAND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
