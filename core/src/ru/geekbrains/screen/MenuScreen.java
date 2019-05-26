package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.sprite.Background;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.BadLogic;

public class MenuScreen extends BaseScreen {

    private Vector2 pos;
    private Texture img;
    private Texture bg;
    private Background background;
    private BadLogic badLogic;


    @Override
    public void show() {
        super.show();
        img = new Texture("ball.png");
        bg = new Texture("Background2.jpg");
        background = new Background(new TextureRegion(bg));
        badLogic = new BadLogic(new TextureRegion(img));
        pos = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    private void update(float delta){
        badLogic.update(delta);
    }

    private void draw(){
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        badLogic.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        img.dispose();
        super.dispose();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        badLogic.resize(worldBounds);
    }

    public boolean touchDown(Vector2 touchl, int pointer){
        badLogic.touchDown(touchl, pointer);
        return false;
    }
}
