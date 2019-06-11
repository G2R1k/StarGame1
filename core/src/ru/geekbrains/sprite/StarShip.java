package ru.geekbrains.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;

public class StarShip extends Ship {

    private static final int INVALID_POINTER = -1;

    private boolean pressLeft;
    private boolean pressRight;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    public StarShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, Sound bulletSound) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        v1 = new Vector2();
        v0 = new Vector2(0.5f, 0);
        bulletV = new Vector2(0, 0.5f);
        this.reloadInterval = 0.5f;
        this.bulletHeight = 0.01f;
        this.damage = 1;
        this.bulletSound = bulletSound;
        this.hp = 50;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        reloadTimer += delta;
        if(reloadTimer >= reloadInterval){
            reloadTimer = 0f;
            shoot();
        }
        if(getRight() > worldBounds.getRight()){
            setRight(worldBounds.getRight());
            stop();
        } else if(getLeft() < worldBounds.getLeft()){
            setLeft(worldBounds.getLeft());
            stop();
        }
    }

    public void startNewGame(){
        this.hp = 50;
        this.pos.x = worldBounds.pos.x;
        flushDestroyed();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.15f);
        setBottom(worldBounds.getBottom() + 0.02f);
    }

    @Override
    public boolean touchDown(Vector2 touchl, int pointer) {
        if(touchl.x < worldBounds.pos.x){
            if(leftPointer != INVALID_POINTER){
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else {
            if(rightPointer != INVALID_POINTER){
                return false;
            }
            rightPointer = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touchl, int pointer) {
        if(pointer == leftPointer){
            leftPointer = INVALID_POINTER;
            if(rightPointer != INVALID_POINTER){
                moveRight();
            } else {
                stop();
            }
        } else if(pointer == rightPointer){
            rightPointer = INVALID_POINTER;
            if(leftPointer != INVALID_POINTER){
                moveLeft();
            } else {
                stop();
            }
        }
        return false;
    }

    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressRight = true;
                moveRight();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressLeft = false;
                if(pressRight){
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressRight = false;
                if(pressLeft){
                    moveLeft();
                } else {
                    stop();
                }
                break;
        }
        return false;
    }

    public boolean isBulletCollision(Rect bullet){
        return !(
            bullet.getRight() < getLeft()
                    || bullet.getLeft() > getRight()
            || bullet.getBottom() > pos.y
                || bullet.getTop() < getBottom()
        );
    }

    public void destroy(){
        super.destroy();
        pressLeft = false;
        pressRight = false;
        leftPointer = INVALID_POINTER;
        rightPointer = INVALID_POINTER;
        stop();
    }

    private void moveRight(){
        v1.set(v0);
    }
    private void moveLeft(){
        v1.set(v0).rotate(180);
    }
    private void stop(){
        v1.setZero();
    }

}
