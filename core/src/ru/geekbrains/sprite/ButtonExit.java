package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaleButton;
import ru.geekbrains.math.Rect;

public class ButtonExit extends ScaleButton {

    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("exit2"));
        setHeightProportion(0.15f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setRight(worldBounds.getRight() - 0.03f);
        setBottom(worldBounds.getBottom() + 0.03f);
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }
}
