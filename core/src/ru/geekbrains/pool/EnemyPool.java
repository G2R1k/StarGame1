package ru.geekbrains.pool;

import com.badlogic.gdx.audio.Sound;

import ru.geekbrains.base.SpritesPool;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.EnemyShip;

public class EnemyPool extends SpritesPool<EnemyShip> {

    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private Sound bulletSound;
    private Rect worldBounds;

    public EnemyPool(BulletPool bulletPool, ExplosionPool explosionPool, Sound bulletSound, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bulletSound = bulletSound;
        this.worldBounds = worldBounds;
    }

    protected EnemyShip newObject() {
        return new EnemyShip(bulletPool, explosionPool, bulletSound, worldBounds);
    }
}
