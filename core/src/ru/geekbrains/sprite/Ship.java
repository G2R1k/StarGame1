package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;

public abstract class Ship extends Sprite {

    protected BulletPool bulletPool;
    protected ExplosionPool explosionPool;
    protected TextureRegion bulletRegion;

    protected Rect worldBounds;

    protected Vector2 v1;
    protected Vector2 v0;
    protected Vector2 bulletV;
    protected float bulletHeight;

    protected float reloadInterval;
    protected float reloadTimer;

    protected int damage;
    protected int hp;

    protected Sound bulletSound;

    private float damageAnimateInterval = 0.1f;
    private float damageAnimateTimer = damageAnimateInterval;


    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }

    public Ship(){

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v1, delta);
        damageAnimateTimer += delta;
        if(damageAnimateTimer >= damageAnimateInterval){
            frame = 0;
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    public void damage(int damage){
        hp -= damage;
        if (hp <= 0){
            hp = 0;
            destroy();
        }
        frame = 1;
        damageAnimateTimer = 0f;
    }

    public int getHp() {
        return hp;
    }

    protected void shoot(){
        bulletSound.play(0.3f);
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, bulletHeight, worldBounds, damage);
    }

    private void boom(){
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }
}
