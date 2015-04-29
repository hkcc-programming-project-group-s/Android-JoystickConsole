package edu.hkcc.pacmanrobot.controller.androidcontroller;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import com.example.student.R;

public class GameJoystick {

    private Bitmap joystick;
    private Bitmap joystickBg;
    private Bitmap trigger;
    private Bitmap triggerDown;

    public GameJoystick(Resources res) {
        joystick = BitmapFactory.decodeResource(res, R.drawable.joystick);
        joystickBg = BitmapFactory.decodeResource(res, R.drawable.joystick_bg);
    }

    public Bitmap get_triggerDown() {
        return triggerDown;
    }

    public void set_triggerDown(Bitmap triggerDown) {
        this.triggerDown = triggerDown;
    }

    public Bitmap get_joystick() {
        return joystick;
    }

    public void set_joystick(Bitmap joystick) {
        this.joystick = joystick;
    }

    public Bitmap get_joystickBg() {
        return joystickBg;
    }

    public void set_joystickBg(Bitmap joystickBg) {
        this.joystickBg = joystickBg;
    }

    public Bitmap get_trigger() {
        return trigger;
    }

    public void set_trigger(Bitmap trigger) {
        this.trigger = trigger;
    }
}
