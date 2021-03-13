package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface FontLoader {
    void loadGameUiFont(AssetManager manager, String characters);

    BitmapFont getGameUiFont(AssetManager manager);

    void loadTitleMenuFont(AssetManager manager, String characters);

    BitmapFont getTitleMenuFont(AssetManager manager);
}
