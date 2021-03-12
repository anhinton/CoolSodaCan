package nz.co.canadia.coolsodacan.client;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import nz.co.canadia.coolsodacan.FontLoader;

public class HtmlFontLoader implements FontLoader {
    @Override
    public void loadGameUiFont(AssetManager manager) {
        manager.load("fonts/Podkova18.fnt", BitmapFont.class);
    }

    @Override
    public BitmapFont getGameUiFont(AssetManager manager) {
        return manager.get("fonts/Podkova18.fnt", BitmapFont.class);
    }

    @Override
    public void loadTitleMenuFont(AssetManager manager) {
        manager.load("fonts/Podkova36.fnt", BitmapFont.class);
    }

    @Override
    public BitmapFont getTitleMenuFont(AssetManager manager) {
        return manager.get("fonts/Podkova36.fnt", BitmapFont.class);
    }
}
