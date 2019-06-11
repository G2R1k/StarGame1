package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import javax.swing.plaf.nimbus.State;

import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;

public class EnemyShip extends Ship {

    private enum State{ DESCENT, FIGHT }

    private State state;

    private Vector2 descnetV = new Vector2(0, -0.15f);

    private StarShip starShip;

    public EnemyShip(BulletPool bulletPool, ExplosionPool explosionPool, Sound bulletSound, Rect worldBounds, StarShip starShip){
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bulletSound = bulletSound;
        this.worldBounds = worldBounds;
        this.v1 = new Vector2();
        this.v0 = new Vector2();
        this.bulletV = new Vector2();
        this.starShip = starShip;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        switch (state) {
            case DESCENT:
                if(getTop() <= worldBounds.getTop()){
                    v1.set(v0);
                    state = State.FIGHT;
                }
                break;
            case FIGHT:
                if(reloadTimer >= reloadInterval){
                    reloadTimer = 0f;
                    shoot();
                }
                if(getBottom() < worldBounds.getBottom()){
                    destroy();
                    starShip.damage(damage);
                }
                break;
        }

    }

    public void set(TextureRegion[] regions,
                    Vector2 v0,
                    TextureRegion bulletRegion,
                    float bulletHeight,
                    float bulletVY,
                    int damage,
                    float reloadInterval,
                    float height,
                    int hp
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        setHeightProportion(height);
        this.hp = hp;
        this.v1.set(descnetV);
        this.state = State.DESCENT;
    }

    public boolean isBulletCollision(Rect bullet){
        return !(
                bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < pos.y
                );
    }
}
