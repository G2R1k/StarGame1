package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaleButton;
import ru.geekbrains.math.Rect;

public class ButtonExit extends ScaleButton {

    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("exit2"));
        setHeightProportion(0.12f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom() + 0.03f);
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }
}
