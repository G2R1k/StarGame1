package ru.geekbrains.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class ScaleButton extends Sprite {

    private static final float PRESS_SCALE = 0.9f;

    private int pointer;
    private boolean pressed;

    public ScaleButton(TextureRegion region){
        super(region);
    }

    @Override
    public boolean touchDown(Vector2 touchl, int pointer) {
        if(pressed || !isMe(touchl)){
            return false;
        }
        this.pointer = pointer;
        this.scale = PRESS_SCALE;
        this.pressed = true;
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touchl, int pointer) {
        if(this.pointer != pointer || !pressed) {
            return false;
        }
        if(isMe(touchl)){
            action();
        }
        pressed = false;
        scale = 1f;
        return false;
    }

    public abstract void action();

}
