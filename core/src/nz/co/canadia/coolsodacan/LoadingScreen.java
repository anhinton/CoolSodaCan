package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen implements Screen {
    private final CoolSodaCan game;
    private final Stage stage;
    private final ProgressBar progressBar;
    private float progress;

    public LoadingScreen(final CoolSodaCan game) {
        this.game = game;

        // Music
        game.manager.load("music/soundtrack.mp3", Music.class);
        // Sounds
        game.manager.load("sounds/animal_superhit.wav", Sound.class);
        game.manager.load("sounds/hit.wav", Sound.class);
        game.manager.load("sounds/plant_superhit.wav", Sound.class);
        game.manager.load("sounds/start.wav", Sound.class);
        game.manager.load("sounds/throw.wav", Sound.class);
        game.manager.load("sounds/unlock.wav", Sound.class);
        // Images
        game.manager.load("graphics/graphics.atlas", TextureAtlas.class);
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        game.manager.load("textures_large/banner_left.jpg", Texture.class, param);
        game.manager.load("textures_large/banner_right.jpg", Texture.class, param);
        game.manager.load("textures_large/title.png", Texture.class, param);
        game.manager.load("textures_large/blue_soda.png", Texture.class, param);
        game.manager.load("textures_large/orange_soda.png", Texture.class, param);
        game.manager.load("textures_large/purple_soda.png", Texture.class, param);
        game.manager.load("textures_large/silver_soda.png", Texture.class, param);
        game.manager.load("textures_large/yellow_soda.png", Texture.class, param);

        int padding = game.getMenuUiPadding();

        OrthographicCamera camera = new OrthographicCamera();
        Viewport viewport = new ExtendViewport(game.getUiWidth(), Gdx.graphics.getBackBufferHeight(),
                camera);
        stage = new Stage(viewport);
        Table table = new Table();
        table.setFillParent(true);
        table.pad(padding);
        stage.addActor(table);

        // create ProgressBarStyle
        ProgressBar.ProgressBarStyle progressBarStyle =
                game.skin.get("loading",
                        ProgressBar.ProgressBarStyle.class);
        // ProgressBar
        progressBar = new ProgressBar(0, 1, .01f,
                false, progressBarStyle);
        progressBar.setValue(progress);

        Label timerLabel = new Label(game.bundle.get("loadingLabel"), game.skin,
                "titlemenu");
        table.add(timerLabel).space(padding);
        table.row();
        table.add(progressBar).prefWidth(game.getUiWidth()).space(padding);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOUR.r, Constants.BACKGROUND_COLOUR.g, Constants.BACKGROUND_COLOUR.b, Constants.BACKGROUND_COLOUR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.manager.update()) {
//		game.setScreen(new GameScreen(game, Player.PlayerType.BLUE));
            game.setScreen(new TitleScreen(game));
        }

        // Update progress
        progress = game.manager.getProgress();
        progressBar.setValue(progress);

        // draw UI
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
