package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;

public class CoolSodaCan extends Game {
	private int gameHeight;
	private int uiWidth;
	private int gameUiPadding;
	private int menuUiPadding;
	SpriteBatch batch;
	I18NBundle bundle;
	FontLoader fontLoader;
	Formatter formatter;
	AssetManager manager;
	Skin skin;
	ShapeRenderer shapeRenderer;
	Statistics statistics;
	boolean debugUnlocks;

	public CoolSodaCan(FontLoader fontLoader, Formatter formatter) {
		this.formatter = formatter;
		this.fontLoader = fontLoader;
	}

	@Override
	public void create () {
		// Set to true to prepare statistic for testing unlocks
		debugUnlocks = false;

		I18NBundle.setSimpleFormatter(true);
		Gdx.input.setCatchKey(Input.Keys.BACK, true);
		statistics = new Statistics();
		statistics.load();
		if (debugUnlocks) {
			statistics.testUnlocks();
		}

		int gameWidth = Constants.GAME_WIDTH;
		gameHeight = Constants.GAME_HEIGHT;
		float gameRatio = (float) gameWidth / gameHeight;
		float screenRatio = (float) Gdx.graphics.getBackBufferWidth() / Gdx.graphics.getBackBufferHeight();
		uiWidth = Gdx.graphics.getBackBufferWidth();
		int gameUiHeight = Gdx.graphics.getBackBufferHeight();
		if (screenRatio < gameRatio) {
			gameHeight = MathUtils.round(gameWidth / screenRatio);
		} else {
			uiWidth = MathUtils.round(gameUiHeight * gameRatio);
		}
		gameUiPadding = MathUtils.round((float) Constants.GAMEUI_PADDING / Constants.GAME_WIDTH * uiWidth);
		menuUiPadding = MathUtils.round((float) Constants.MENUUI_PADDING / Constants.GAME_WIDTH * uiWidth);

		// Load assets
		manager = new AssetManager();
		manager.load("il8n/Bundle", I18NBundle.class);
		manager.load("graphics/graphics.atlas", TextureAtlas.class);
		TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
		param.minFilter = Texture.TextureFilter.Linear;
		param.magFilter = Texture.TextureFilter.Linear;
		manager.load("textures_large/banner_left.jpg", Texture.class, param);
		manager.load("textures_large/banner_right.jpg", Texture.class, param);
		manager.load("textures_large/title.png", Texture.class, param);
		manager.load("textures_large/blue_soda.png", Texture.class, param);
		manager.load("textures_large/orange_soda.png", Texture.class, param);
		manager.load("textures_large/purple_soda.png", Texture.class, param);
		manager.load("textures_large/silver_soda.png", Texture.class, param);
		manager.load("textures_large/yellow_soda.png", Texture.class, param);
		fontLoader.loadGameUiFont(manager);
		fontLoader.loadTitleMenuFont(manager);
		fontLoader.loadStatisticsFont(manager);
		manager.load("skin/uiskin.json", Skin.class);
		manager.finishLoading();

		// Prepare skin
		skin = manager.get("skin/uiskin.json", Skin.class);

		skin.add("game", new Label.LabelStyle(fontLoader.getGameUiFont(manager), Color.WHITE), Label.LabelStyle.class);
		skin.add("titlemenu", new Label.LabelStyle(fontLoader.getTitleMenuFont(manager), Color.WHITE), Label.LabelStyle.class);
		skin.add("statistics", new Label.LabelStyle(fontLoader.getStatisticsFont(manager), Color.WHITE), Label.LabelStyle.class);

		TextButton.TextButtonStyle gameTextButtonStyle = new TextButton.TextButtonStyle(skin.get("default", TextButton.TextButtonStyle.class));
		gameTextButtonStyle.font = fontLoader.getGameUiFont(manager);
		skin.add("game", gameTextButtonStyle);
		TextButton.TextButtonStyle titlemenuTextButtonStyle = new TextButton.TextButtonStyle(skin.get("default", TextButton.TextButtonStyle.class));
		titlemenuTextButtonStyle.font = fontLoader.getTitleMenuFont(manager);
		skin.add("titlemenu", titlemenuTextButtonStyle);

		batch = new SpriteBatch();
		bundle = manager.get("il8n/Bundle", I18NBundle.class);
		// DEBUG hitboxes
		shapeRenderer = new ShapeRenderer();
//		this.setScreen(new GameScreen(this));
		this.setScreen(new TitleScreen(this));
	}

	// Calculate the width of a Game-size Sprite/Texture in uiViewport coordinates
	public float calculateImageWidth(float width) {
		return width / Constants.GAME_WIDTH * getUiWidth();
	}

	// Calculate the height of a Game-size Sprite/Texture in uiViewport coordinates
	public float calculateImageHeight(float width, float height) {
		float adjustedWidth = calculateImageWidth(width);
		float ratio = adjustedWidth / width;
		return height * ratio;
	}

	int getGameHeight() {
		return gameHeight;
	}

	int getUiWidth() {
		return uiWidth;
	}

	public int getGameUiPadding() {
		return gameUiPadding;
	}

	public int getMenuUiPadding() { return menuUiPadding; }

	public String displayTime(float timeElapsed) {
		int minutes = (int) (timeElapsed / 60);
		int seconds = (int) (timeElapsed % 60);
		return formatter.zeroPadTime(minutes, bundle.getLocale()) + ":" + formatter.zeroPadTime(seconds, bundle.getLocale());
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		statistics.save();
		batch.dispose();
		manager.dispose();
		// NB I am not doing skin.dispose() because its TextureAtlas and BitmapFont *should*
		// be disposed in manager.dispose()
	}
}