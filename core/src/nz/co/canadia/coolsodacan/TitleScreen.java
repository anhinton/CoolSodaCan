package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class TitleScreen implements Screen, InputProcessor {
    private final CoolSodaCan game;
    private final Stage stage;
    private final Table table;
    private final int padding;
    private CurrentMenu currentMenu;

    private enum CurrentMenu { MAIN, STATISTICS, SETTINGS, CREDITS}

    public TitleScreen(CoolSodaCan game) {
        this.game = game;
        padding = game.getMenuUiPadding();

        FitViewport uiViewport = new FitViewport(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        stage = new Stage(uiViewport);
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        showMainMenu();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void showMainMenu() {
        currentMenu = CurrentMenu.MAIN;
        table.clear();
        table.center();
        table.pad(padding);

        Label titleLabel = new Label(Constants.GAME_NAME, game.skin, "titlemenu");

        TextButton startButton = new TextButton(game.bundle.get("titlescreenStartButton"), game.skin, "titlemenu");
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton statsButton = new TextButton(game.bundle.get("titlescreenStatisticsButton"), game.skin, "titlemenu");
        statsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showStatistics();
            }
        });

        TextButton settingsButton = new TextButton(game.bundle.get("titlescreenSettingsButton"), game.skin, "titlemenu");

        table.add(titleLabel).space(padding);
        table.row();
        table.add(startButton).space(padding)
                .prefSize(startButton.getPrefWidth() * Constants.GAMEUI_MENUBUTTON_SCALE,
                        startButton.getPrefHeight() * Constants.GAMEUI_MENUBUTTON_SCALE);
        table.row();
        table.add(statsButton).space(padding)
                .prefSize(startButton.getPrefWidth() * Constants.GAMEUI_MENUBUTTON_SCALE,
                        startButton.getPrefHeight() * Constants.GAMEUI_MENUBUTTON_SCALE);
        table.row();
        table.add(settingsButton).space(padding)
                .prefSize(startButton.getPrefWidth() * Constants.GAMEUI_MENUBUTTON_SCALE,
                        startButton.getPrefHeight() * Constants.GAMEUI_MENUBUTTON_SCALE);

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            TextButton quitButton = new TextButton(game.bundle.get("titlescreenQuitButton"), game.skin, "titlemenu");
            quitButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    quit();
                }
            });
            table.row();
            table.add(quitButton).space(padding)
                    .prefSize(startButton.getPrefWidth() * Constants.GAMEUI_MENUBUTTON_SCALE,
                            startButton.getPrefHeight() * Constants.GAMEUI_MENUBUTTON_SCALE);
        }
    }

    private void showSettingsMenu() {
    }

    private void showStatistics() {
        currentMenu = CurrentMenu.STATISTICS;
        table.clear();
        table.center();
        table.pad(padding);

        Label headingLabel = new Label(game.bundle.get("titlescreenStatisticsButton"), game.skin, "titlemenu");

        String bp = game.bundle.get("bulletPoint") + " ";
        String statisticsString =
                bp + game.bundle.get("statisticsThrown") + ": " + game.statistics.getTotalCansThrown() + "\n" +
                bp + FreeTypeFontGenerator.DEFAULT_CHARS;
        Label statisticsLabel = new Label(statisticsString, game.skin, "titlemenu");
        statisticsLabel.setWrap(true);

        table.add(headingLabel).space(padding).left();
        table.row();
        table.add(statisticsLabel).prefWidth(Gdx.graphics.getBackBufferWidth());
    }

    private void goBack() {
        switch (currentMenu) {
            case MAIN:
                quit();
                break;
            case SETTINGS:
                showMainMenu();
                break;
            case STATISTICS:
                showMainMenu();
                break;
            case CREDITS:
                showSettingsMenu();
                break;
        }
    }

    private void quit() {
        Gdx.app.exit();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Constants.BACKGROUND_COLOUR);

        stage.act(delta);
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

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.BACK:
            case Input.Keys.ESCAPE:
                goBack();
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
