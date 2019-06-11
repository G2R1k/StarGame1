package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.base.Font;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.ButtonNewGame;
import ru.geekbrains.sprite.EnemyShip;
import ru.geekbrains.sprite.MsgGameOver;
import ru.geekbrains.sprite.StarShip;
import ru.geekbrains.sprite.Stars;
import ru.geekbrains.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";
    private static final int STAR_COUNT = 64;

    private enum State { PLAYING, PAUSE, GAME_OVER }

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

    private State state;

    private EnemyGenerator enemyGenerator;

    private MsgGameOver msgGameOver;

    private ButtonNewGame buttonNewGame;

    private int frags;
    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;


    @Override
    public void show() {
        super.show();
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(0.02f);
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
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Stars(atlas);
        }
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        starShip = new StarShip(atlas, bulletPool, explosionPool, laserSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, bulletSound, worldBounds, starShip);
        enemyGenerator = new EnemyGenerator(worldBounds, enemyPool, atlas);
        msgGameOver = new MsgGameOver(atlas);
        buttonNewGame = new ButtonNewGame(atlas, this);
        frags = 0;
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();
        state = State.PLAYING;
    }

    @Override
    public void pause() {
        super.pause();
        if(state == State.GAME_OVER){
            return;
        }
        state = State.PAUSE;
        music.pause();
    }

    @Override
    public void resume() {
        super.resume();
        if(state == State.GAME_OVER){
            return;
        }
        state = State.PLAYING;
        music.play();
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
        explosionPool.updateActiveSprites(delta);
        if(state == State.PLAYING){
                starShip.update(delta);
                bulletPool.updateActiveSprites(delta);
                enemyPool.updateActiveSprites(delta);
                enemyGenerator.generate(delta, frags);
        }
    }

    private void checkCollision(){
        if(state != State.PLAYING){
            return;
        }
        List<EnemyShip> enemyShipList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for(EnemyShip enemyShip : enemyShipList){
            if(enemyShip.isDestroyed()){
                continue;
            }
            float miniDist = enemyShip.getHalfWidth() + starShip.getHalfWidth();
            if(enemyShip.pos.dst(starShip.pos) < miniDist){
                enemyShip.destroy();
                starShip.damage(starShip.getHp());
                state = State.GAME_OVER;
            }
            for (Bullet bullet : bulletList) {
                if(bullet.getOwner() != starShip || bullet.isDestroyed()){
                    continue;
                }
                if(enemyShip.isBulletCollision(bullet)){
                    enemyShip.damage(bullet.getDamage());
                    if(enemyShip.isDestroyed()){
                        frags++;
                    }
                    bullet.destroy();
                }
            }
        }
        for (Bullet bullet : bulletList){
            if(bullet.getOwner() == starShip || bullet.isDestroyed()){
                continue;
            }
            if(starShip.isBulletCollision(bullet)){
                starShip.damage(bullet.getDamage());
                bullet.destroy();
                if(starShip.isDestroyed()){
                    state = State.GAME_OVER;
                }
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
        explosionPool.drawActiveSprites(batch);
        if(state == State.PLAYING) {
            starShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        }
        else if (state == State.GAME_OVER){
            msgGameOver.draw(batch);
            buttonNewGame.draw(batch);
        }
        printInfo();
        batch.end();
    }

    private void printInfo(){
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft(), worldBounds.getTop() - 0.01f);
        sbHP.setLength(0);
        font.draw(batch, sbHP.append(HP).append(starShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - 0.01f, Align.center);
        sbLevel.setLength(0);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyGenerator.getLevel()), worldBounds.getRight(), worldBounds.getTop() - 0.01f, Align.right);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for(Stars star : stars) {
            star.resize(worldBounds);
        }
        starShip.resize(worldBounds);
        msgGameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
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
        font.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(state == State.PLAYING) {
            starShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(state == State.PLAYING) {
            starShip.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touchl, int pointer) {
        if(state == State.PLAYING) {
            starShip.touchDown(touchl, pointer);
        } else if(state == State.GAME_OVER) {
            buttonNewGame.touchDown(touchl, pointer);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touchl, int pointer) {
        if(state == State.PLAYING) {
            starShip.touchUp(touchl, pointer);
        }else if(state == State.GAME_OVER) {
            buttonNewGame.touchUp(touchl, pointer);
        }
        return false;
    }

    public void startNewGame(){
        state = State.PLAYING;
        starShip.startNewGame();
        frags = 0;
        bulletPool.freeAllActiveObjects();
        explosionPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }
}
