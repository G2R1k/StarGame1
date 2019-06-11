package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class MsgGameOver extends Sprite {

    public MsgGameOver(TextureAtlas atlas){
        super(atlas.findRegion("message_game_over"));
        setHeightProportion(0.05f);
        setBottom(-0.1f);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.07f);
        setBottom(0.009f);
    }
}

