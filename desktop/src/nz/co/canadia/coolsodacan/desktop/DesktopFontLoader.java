package nz.co.canadia.coolsodacan.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.MathUtils;

import nz.co.canadia.coolsodacan.Constants;
import nz.co.canadia.coolsodacan.FontLoader;

public class DesktopFontLoader implements FontLoader {

    @Override
    public void loadGameUiFont(AssetManager manager, String characters) {
        manager.load("fonts/Podkova18.fnt", BitmapFont.class);
    }

    @Override
    public BitmapFont getGameUiFont(AssetManager manager) {
        return manager.get("fonts/Podkova18.fnt", BitmapFont.class);
    }

    @Override
    public void loadTitleMenuFont(AssetManager manager, String characters) {
        manager.load("fonts/Podkova36.fnt", BitmapFont.class);
    }

    @Override
    public BitmapFont getTitleMenuFont(AssetManager manager) {
        return manager.get("fonts/Podkova36.fnt", BitmapFont.class);
    }
}
