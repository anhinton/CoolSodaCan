package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface FontLoader {
    void loadGameUiFont(AssetManager manager);

    BitmapFont getGameUiFont(AssetManager manager);

    void loadTitleMenuFont(AssetManager manager);

    BitmapFont getTitleMenuFont(AssetManager manager);

    void loadStatisticsFont(AssetManager manager);

    BitmapFont getStatisticsFont(AssetManager manager);
}
