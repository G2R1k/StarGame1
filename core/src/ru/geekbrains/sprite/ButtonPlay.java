package ru.geekbrains.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaleButton;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;

public class ButtonPlay extends ScaleButton {

    private Game game;

    public ButtonPlay(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("play"));
        this.game = game;
        setHeightProportion(0.2f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setLeft(worldBounds.getLeft() + 0.01f);
        setBottom(worldBounds.getBottom() + 0.00f);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen());
    }
}
