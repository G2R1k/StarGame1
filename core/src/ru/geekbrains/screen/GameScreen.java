package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.StarShip;
import ru.geekbrains.sprite.Stars;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Stars[] stars;
    private StarShip starShip;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/Background2.jpg");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas("textures/mainAtlas.atlas");
        stars = new Stars[STAR_COUNT];
        starShip = new StarShip(atlas);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Stars(atlas);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float delta){
        for(Stars star : stars) {
            star.update(delta);
        }
        starShip.update(delta);
    }

    private void draw(){
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for(Stars star : stars) {
            star.draw(batch);
        }
        starShip.draw(batch);
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
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touchl, int pointer) {
        starShip.touchDown(touchl, pointer);
        return super.touchDown(touchl, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touchl, int pointer) {
        return super.touchUp(touchl, pointer);
    }
}
