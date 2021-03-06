package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.MathUtils;

public class IOSFontLoader implements FontLoader {
    private final FileHandleResolver resolver;

    public IOSFontLoader() {
        resolver = new InternalFileHandleResolver();
    }

    private void setLoader(AssetManager manager) {
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
    }

    @Override
    public void loadGameUiFont(AssetManager manager) {
        setLoader(manager);

        FreetypeFontLoader.FreeTypeFontLoaderParameter gameUiFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        gameUiFont.fontFileName = "fonts/Podkova-VariableFont_wght.ttf";
        gameUiFont.fontParameters.characters = Constants.FONT_CHARACTERS;
        gameUiFont.fontParameters.size = MathUtils.round((float) Constants.GAMEUI_FONT_SIZE / Constants.GAME_HEIGHT * Gdx.graphics.getBackBufferHeight());
        gameUiFont.fontParameters.color = Constants.FONT_COLOR;
        gameUiFont.fontParameters.shadowColor = Constants.FONT_SHADOW_COLOR;
        gameUiFont.fontParameters.shadowOffsetX = MathUtils.round((float) Constants.GAMEUI_FONT_SHADOW_OFFSET / Constants.GAME_HEIGHT * Gdx.graphics.getBackBufferHeight());
        gameUiFont.fontParameters.shadowOffsetY = MathUtils.round((float) Constants.GAMEUI_FONT_SHADOW_OFFSET / Constants.GAME_HEIGHT * Gdx.graphics.getBackBufferHeight());
        manager.load("fonts/Podkova-VariableFont_wghtGameUi.ttf", BitmapFont.class, gameUiFont);
    }

    @Override
    public BitmapFont getGameUiFont(AssetManager manager) {
        return manager.get("fonts/Podkova-VariableFont_wghtGameUi.ttf", BitmapFont.class);
    }

    @Override
    public void loadTitleMenuFont(AssetManager manager) {
        setLoader(manager);

        FreetypeFontLoader.FreeTypeFontLoaderParameter titleMenuFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        titleMenuFont.fontFileName = "fonts/Podkova-VariableFont_wght.ttf";
        titleMenuFont.fontParameters.characters = Constants.FONT_CHARACTERS;
        titleMenuFont.fontParameters.size = MathUtils.round((float) Constants.TITLEMENU_FONT_SIZE / Constants.GAME_HEIGHT * Gdx.graphics.getBackBufferHeight());
        titleMenuFont.fontParameters.color = Constants.FONT_COLOR;
        titleMenuFont.fontParameters.shadowColor = Constants.FONT_SHADOW_COLOR;
        titleMenuFont.fontParameters.shadowOffsetX = MathUtils.round((float) Constants.TITLEMENU_FONT_SHADOW_OFFSET / Constants.GAME_HEIGHT * Gdx.graphics.getBackBufferHeight());
        titleMenuFont.fontParameters.shadowOffsetY = MathUtils.round((float) Constants.TITLEMENU_FONT_SHADOW_OFFSET / Constants.GAME_HEIGHT * Gdx.graphics.getBackBufferHeight());
        manager.load("fonts/Podkova-VariableFont_wghtTitleMenu.ttf", BitmapFont.class, titleMenuFont);
    }

    @Override
    public BitmapFont getTitleMenuFont(AssetManager manager) {
        return manager.get("fonts/Podkova-VariableFont_wghtTitleMenu.ttf", BitmapFont.class);
    }

    @Override
    public void loadStatisticsFont(AssetManager manager) {
        setLoader(manager);

        FreetypeFontLoader.FreeTypeFontLoaderParameter statisticsFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        statisticsFont.fontFileName = "fonts/Podkova-VariableFont_wght.ttf";
        statisticsFont.fontParameters.characters = Constants.FONT_CHARACTERS;
        statisticsFont.fontParameters.size = MathUtils.round((float) Constants.STATISTICS_FONT_SIZE / Constants.GAME_HEIGHT * Gdx.graphics.getBackBufferHeight());
        statisticsFont.fontParameters.color = Constants.FONT_COLOR;
        statisticsFont.fontParameters.shadowColor = Constants.FONT_SHADOW_COLOR;
        statisticsFont.fontParameters.shadowOffsetX = MathUtils.round((float) Constants.STATISTICS_FONT_SHADOW_OFFSET / Constants.GAME_HEIGHT * Gdx.graphics.getBackBufferHeight());
        statisticsFont.fontParameters.shadowOffsetY = MathUtils.round((float) Constants.STATISTICS_FONT_SHADOW_OFFSET / Constants.GAME_HEIGHT * Gdx.graphics.getBackBufferHeight());
        manager.load("fonts/Podkova-VariableFont_wghtStatistics.ttf", BitmapFont.class, statisticsFont);
    }

    @Override
    public BitmapFont getStatisticsFont(AssetManager manager) {
        return manager.get("fonts/Podkova-VariableFont_wghtStatistics.ttf", BitmapFont.class);
    }
}
