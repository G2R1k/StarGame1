package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    SpriteBatch batch;
    Texture img;
    Texture img2;

    private float x, y;
    private Vector2 touchl;
    private Vector2 distance;
    private Vector2 v1;
    private Vector2 pos;
    private float topBorder;
    private float rightBorder;

    @Override
    public void show(){
        super.show();
        batch = new SpriteBatch();
        img = new Texture("ball.png");
        img2 = new Texture("Background.jpg");
        touchl = new Vector2();
        v1 = new Vector2();
        distance = new Vector2();
        pos = new Vector2(0, 0);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 0.5f);
        batch.draw(img2, 0, 0);
        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(img, pos.x, pos.y, 128, 128);

        batch.end();
        v1.x = (touchl.x - pos.x) * 0.1f;
        v1.y = (touchl.y - pos.y) * 0.1f;
        pos.add(v1);




//        topBorder =  pos.y + img.getHeight();
//        rightBorder = pos.x + img.getWidth();
//        if(rightBorder < Gdx.graphics.getWidth() &&  topBorder < Gdx.graphics.getHeight()){
//            pos.add(v1);
//        } else {
//            pos.set(-0.8f, -0.8f);
//        }
    }

    @Override
    public void dispose(){
        batch.dispose();
        img.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);
        touchl.set(screenX, Gdx.graphics.getHeight() - screenY);
        System.out.println("touch1.x = " + touchl.x + "touch1.y = " + touchl.y);
        return false;
    }
}
