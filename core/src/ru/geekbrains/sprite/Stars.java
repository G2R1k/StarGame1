package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Stars extends Sprite {

    private Vector2 v;
    private Rect worldBounds;
    private float height;

    public Stars(TextureAtlas atlas){
        super(atlas.findRegion("star2"));
        float vX = Rnd.nextFloat(-0.005f, 0.005f);
        float vY = Rnd.nextFloat(-0.1f, -0.3f);
        v = new Vector2(vX, vY);
        height = Rnd.nextFloat(0.001f, 0.01f);
        setHeightProportion(height);
    }

    @Override
    public void update(float delta) {
        if(height >= 0.01f){
            height = 0.005f;
        } else {
            height += 0.0001f;
        }
        setHeightProportion(height);
        pos.mulAdd(v, delta);
        if(getRight() < worldBounds.getLeft()){
            setLeft(worldBounds.getRight());
        }
        if(getLeft() > worldBounds.getRight()){
            setRight(worldBounds.getLeft());
        }
        if(getTop() < worldBounds.getBottom()){
            setBottom(worldBounds.getTop());
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(posX, posY);
    }
}
