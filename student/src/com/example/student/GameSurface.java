package com.example.student;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.example.student.GameControls.cx;
import static com.example.student.GameControls.cy;

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
        int bg_offset_x = gameJoystick.get_joystickBg().getWidth() / 2;
        int bg_offset_y = gameJoystick.get_joystickBg().getHeight() / 2;
        int offset_x = gameJoystick.get_joystick().getWidth() / 2;
        int offset_y = gameJoystick.get_joystick().getHeight() / 2;
        canvas.drawBitmap(gameJoystick.get_joystickBg(), (cx - bg_offset_x), (cy - bg_offset_y), null);
        canvas.drawBitmap(gameJoystick.get_joystick(), gameControls.touchingPoint.x - offset_x, gameControls.touchingPoint.y - offset_y, null);
    }

    public int joystick_half_width = 26;
    public int joystick_bg_half_width = 45;

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
