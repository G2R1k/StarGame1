package ru.geekbrains.sprite;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class StarShip extends Sprite {

    private static final float LEN = 0.005f;
    private Rect worldBounds;
    private Vector2 touch1;
    private Vector2 v2;
    private Vector2 buf;

    public StarShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship1111111111"));
        setHeightProportion(0.15f);
        v2 = new Vector2();
        touch1 = new Vector2();
        buf = new Vector2();
    }

    @Override
    public void update(float delta) {
        buf.set(touch1);
        if(buf.sub(pos).len() <= LEN){
            pos.set(touch1);
        } else {
            pos.add(v2);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setBottom(worldBounds.getBottom() + 0.02f);
    }

    @Override
    public boolean touchDown(Vector2 touchl, int pointer) {
        this.touch1 = touchl;
        v2.set(touchl.cpy().sub(pos)).setLength(LEN);
        return false;
    }
}
