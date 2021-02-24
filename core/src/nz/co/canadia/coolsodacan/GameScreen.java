package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Main game screen
 */

public class GameScreen implements Screen, InputProcessor {

    private final CoolSodaCan game;
    private final Player player;
    private final Viewport viewport;
    private final Stage bannerStage;

    GameScreen(CoolSodaCan game) {
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
        this.game = game;

        Gdx.input.setCursorCatched(true);

        // Load assets
        game.manager.load("graphics/graphics.atlas", TextureAtlas.class);
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        game.manager.load("banner/banner_left.jpg", Texture.class, param);
        game.manager.load("banner/banner_right.jpg", Texture.class, param);
        game.manager.finishLoading();

        TextureAtlas atlas = game.manager.get("graphics/graphics.atlas", TextureAtlas.class);

        // create player object
        player = new Player(game.getGameHeight(), atlas);

        // create the game viewport
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.GAME_WIDTH, game.getGameHeight());
        viewport = new ExtendViewport(Constants.GAME_WIDTH,
                game.getGameHeight(), camera);


        // Create the side banners.
        // These are in a different Viewport to game objects so they can be wholly or partially "off-screen"
        FillViewport bannerViewport = new FillViewport(Constants.BANNER_WIDTH, Constants.BANNER_HEIGHT);
        bannerStage = new Stage(bannerViewport);
        Texture bannerLeftTexture = game.manager.get("banner/banner_left.jpg", Texture.class);
        Texture bannerRightTexture = game.manager.get("banner/banner_right.jpg", Texture.class);
        Image bannerLeftImage = new Image(bannerLeftTexture);
        bannerLeftImage.setPosition(0, 0);
        bannerStage.addActor(bannerLeftImage);
        Image bannerRightImage = new Image(bannerRightTexture);
        bannerRightImage.setPosition(bannerStage.getWidth() - bannerRightTexture.getWidth(), 0);
        bannerStage.addActor(bannerRightImage);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Constants.BACKGROUND_COLOUR);
        viewport.getCamera().update();

        // update objects
        player.update();

        // Draw side banners
        bannerStage.getViewport().apply();
        bannerStage.draw();

        // draw sprites
        viewport.apply();
        game.batch.setProjectionMatrix(viewport.getCamera().combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        bannerStage.getViewport().update(width, height);
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
        Gdx.input.setCursorCatched(false);
        bannerStage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.ESCAPE:
            case Input.Keys.BACK:
                Gdx.app.exit();
                break;
            case Input.Keys.F:
                if (Gdx.graphics.isFullscreen()) {
                    Gdx.graphics.setWindowedMode(Constants.DESKTOP_WIDTH, Constants.DESKTOP_HEIGHT);
                } else {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                }
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        player.setTargetXY(screenX, screenY, viewport);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        player.setTargetXY(screenX, screenY, viewport);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        player.setTargetXY(screenX, screenY, viewport);
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
