package edu.hkcc.pacmanrobot.controller.androidcontroller;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class GameControls extends Activity implements OnTouchListener {
    public static int screen_width;
    public static int screen_height;
    public static int cx, cy;
    public Point initialXY;
    public Point touchingPoint;
    private Main master;
    //public Point pointerPosition;
    private Boolean dragging = false;
    private MotionEvent lastEvent;


    public GameControls(Main context) {
        this.master = context;
        initialXY = checkMonitorSize();
        screen_width = initialXY.x;
        screen_height = initialXY.y;
        cx = screen_width / 2;
        cy = screen_height / 2;
        touchingPoint = new Point(screen_width / 2, screen_height / 2);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event == null) return false;
        update(event);
        return true;
    }

    public void update(MotionEvent event) {
        if (event == null && lastEvent == null) {
            return;
        } else if (event == null) {
            event = lastEvent;
        } else {
            lastEvent = event;
        }
        //drag drop
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dragging = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            dragging = false;
        }

        if (dragging) {
            // get the position
            touchingPoint.x = (int) event.getX();
            touchingPoint.y = (int) event.getY();
            float x = event.getX();
            float y = event.getY();
            x = (x - cx) / cx;
            y = (y - cy) / cx;
            double rad = Math.atan2(x, -y);
            double distance = Math.sqrt(x * x + y * y);

            master.sendCommand(rad, distance);
        } else {
            // Snap back to center when the joystick is released
            touchingPoint.x = cx;
            touchingPoint.y = cy;
            master.sendCommand(0, 0);
        }


    }

    private Point checkMonitorSize() {
        WindowManager windowManager =
                master.getWindowManager();
        Log.w("debug", windowManager.toString());
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
}