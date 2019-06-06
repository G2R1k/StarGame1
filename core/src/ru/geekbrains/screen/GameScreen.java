package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.EnemyShip;
import ru.geekbrains.sprite.Explosion;
import ru.geekbrains.sprite.StarShip;
import ru.geekbrains.sprite.Stars;
import ru.geekbrains.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Stars[] stars;
    private StarShip starShip;

    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private EnemyPool enemyPool;

    private Sound laserSound;
    private Music music;
    private Sound explosionSound;
    private Sound bulletSound;

    private EnemyGenerator enemyGenerator;

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        bg = new Texture("textures/Background2.jpg");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas("textures/mainAtlas.atlas");
        stars = new Stars[STAR_COUNT];
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, bulletSound, worldBounds);
        starShip = new StarShip(atlas, bulletPool, explosionPool, laserSound);
        enemyGenerator = new EnemyGenerator(worldBounds, enemyPool, atlas);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Stars(atlas);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        checkCollision();
        freeAllDestroyedActiveObjects();
        draw();
    }

    private void update(float delta){
        for(Stars star : stars) {
            star.update(delta);
        }
        if(!starShip.isDestroyed()){
            starShip.update(delta);
        }
        bulletPool.updateActiveSprites(delta);
        explosionPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        enemyGenerator.generate(delta);
    }

    private void checkCollision(){
        if(starShip.isDestroyed()){
            return;
        }
        List<EnemyShip> enemyShipList = enemyPool.getActiveObjects();
        for(EnemyShip enemyShip : enemyShipList){
            if(enemyShip.isDestroyed()){
                continue;
            }
            float miniDist = enemyShip.getHalfWidth() + starShip.getHalfWidth();
            if(enemyShip.pos.dst(starShip.pos) < miniDist){
                enemyShip.destroy();
                starShip.destroy();
            }
        }
    }

    private void freeAllDestroyedActiveObjects(){
        bulletPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    private void draw(){
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for(Stars star : stars) {
            star.draw(batch);
        }
        if(!starShip.isDestroyed()){
            starShip.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        explosionPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for(Stars star : stars) {
            star.resize(worldBounds);
        }
        starShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        bg.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        explosionSound.dispose();
        bulletSound.dispose();
        laserSound.dispose();
        music.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        starShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        starShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touchl, int pointer) {
        Explosion explosion = explosionPool.obtain();
        explosion.set(0.15f, touchl);
        starShip.touchDown(touchl, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touchl, int pointer) {
        starShip.touchUp(touchl, pointer);
        return false;
    }
}
