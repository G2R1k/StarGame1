package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class BadLogic extends Sprite {

    private static float LEN = 0.005f;
    private Vector2 v1;
    private Vector2 touch1;
    private Vector2 buf;

    public BadLogic(TextureRegion region){
        super(region);
        regions[frame].getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        v1 = new Vector2();
        buf = new Vector2();
        touch1 = new Vector2();
    }

    public void draw(SpriteBatch batch){
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),
                halfWidth, halfHeight,
                getWidth(),getHeight(),
                scale, scale,
                angle
        );
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        buf.set(touch1);
        if(buf.sub(pos).len() <= LEN) {
            pos.set(touch1);
        } else {
            pos.add(v1);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.2f);
    }

    @Override
    public boolean touchDown(Vector2 touchl, int pointer) {
        this.touch1 = touchl;
        v1.set(touchl.cpy().sub(pos)).setLength(LEN);
        return false;
    }
}
