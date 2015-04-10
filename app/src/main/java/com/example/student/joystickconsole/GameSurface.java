package com.example.student.joystickconsole;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    private Main master;
    private GameThread gameThread;
    private GameControls gameControls;
    private GameJoystick gameJoystick;
    //private Bitmap pointer;

    public GameSurface(Main master) {
        super(master);
        this.master = master;
        initialize();
        getScreenSize();
    }

    private void initialize() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        gameThread = new GameThread(holder, master, new Handler(), this);
        setFocusable(true);

        gameJoystick = new GameJoystick(getContext().getResources());
        //pointer = BitmapFactory.decodeResource(getResources(), R.drawable.ball);

        //controls
        gameControls = new GameControls(master);
        setOnTouchListener(gameControls);
    }

    public void doDraw(Canvas canvas) {
        gameControls.update(null);
        // canvas.drawBitmap(pointer, gameControls.pointerPosition.x, gameControls.pointerPosition.y, null);
        canvas.drawBitmap(gameJoystick.get_joystickBg(), (GameControls.cx - joystick_bg_half_width), (gameControls.cy - joystick_bg_half_width), null);
        canvas.drawBitmap(gameJoystick.get_joystick(), gameControls.touchingPoint.x -joystick_half_width , gameControls.touchingPoint.y -  joystick_half_width , null);
    }

    public  int joystick_half_width=26;
    public  int joystick_bg_half_width=45;

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        boolean retry = true;
        gameThread.state = GameThread.PAUSED;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        if (gameThread.state == GameThread.PAUSED) {
            gameThread = new GameThread(getHolder(), master, new Handler(), this);
            gameThread.start();
        } else {
            gameThread.start();
        }
    }

    public void Update() {
    }

    public void getScreenSize() {
    }
}
