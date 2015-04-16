package com.example.student;

import java.util.logging.Level;
import java.util.logging.Logger;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private final SurfaceHolder mSurfaceHolder;
    private Paint mLinePaint;
    private Paint blackPaint;

    //state of game (Running or Paused).
    int state = 1;
    public final static int RUNNING = 1;
    public final static int PAUSED = 2;
    public final static int STOPPED = 3;

    GameSurface gEngine;

    public GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler, GameSurface gEngineS) {
        //data about the screen
        mSurfaceHolder = surfaceHolder;
        gEngine = gEngineS;
    }

    @Override
    public void run() {
        while (state == RUNNING) {
            //Log.d("State", "Thread is running");
            long beforeTime;
            beforeTime = System.nanoTime();
            gEngine.Update();

            Canvas c = null;
            try {
                c = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    c.drawColor(Color.WHITE);
                    gEngine.doDraw(c);
                }
            } finally {
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
            long delay = 70;
            long sleepTime = delay - ((System.nanoTime() - beforeTime) / 1000000L);

            try {
                if (sleepTime > 0) {
                    sleep(sleepTime);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
            }

            while (state == PAUSED) {
                Log.d("State", "Thread is paused");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
