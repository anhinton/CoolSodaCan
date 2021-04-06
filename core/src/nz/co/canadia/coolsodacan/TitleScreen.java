package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public class TitleScreen implements Screen, InputProcessor {
    private final CoolSodaCan game;
    private final Stage stage;
    private final Table table;
    private final int padding;
    private final float buttonHeight;
    private final float buttonWidth;
    private final TextButton backButton;
    private final TextureAtlas atlas;
    private Sprite currentSprite;
    private final Viewport viewport;
    private CurrentMenu currentMenu;
    private Label musicVolumeLabel;
    private Slider musicVolumeSlider;
    private Label soundVolumeLabel;
    private Slider soundVolumeSlider;

    private enum CurrentMenu { MAIN, SELECT_SODA, START_GAME, UNLOCK_DIALOG, STATISTICS, RESET_STATISTICS, SETTINGS, CREDITS}

    public TitleScreen(CoolSodaCan game) {
        this.game = game;
        padding = game.getMenuUiPadding();
        buttonWidth = game.getUiWidth() * Constants.TITLEMENU_BUTTON_WIDTH;
        buttonHeight = buttonWidth * Constants.TITLEMENU_BUTTON_RELATIVE_HEIGHT;
        atlas = game.manager.get("graphics/graphics.atlas", TextureAtlas.class);

        currentSprite = new Sprite();

        backButton = new TextButton(game.bundle.get("backButton"), game.skin, "titlemenu");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goBack();
            }
        });

        // create the game viewport
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.GAME_WIDTH, game.getGameHeight());
        viewport = new FitViewport(Constants.GAME_WIDTH,
                game.getGameHeight(), camera);

        OrthographicCamera uiCamera = new OrthographicCamera();
        Viewport uiViewport = new ExtendViewport(game.getUiWidth(), Gdx.graphics.getBackBufferHeight(),
                uiCamera);
        stage = new Stage(uiViewport);
        table = new Table();
        table.setFillParent(true);
        table.pad(padding);
        table.padTop(Math.max(0, Gdx.graphics.getSafeInsetTop()));
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

        Image titleImage = new Image(game.manager.get("textures_large/title.png", Texture.class));

        TextButton startButton = new TextButton(game.bundle.get("titlescreenChooseButton"), game.skin, "titlemenu");
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSodaSelection();
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
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSettingsMenu();
            }
        });

        table.add(titleImage)
                .expandY()
                .prefWidth(game.calculateImageWidth(titleImage.getWidth()))
                .prefHeight(game.calculateImageHeight(titleImage.getWidth(), titleImage.getHeight()))
                .space(padding);
        table.row();
        Table buttonTable = new Table();
        buttonTable.add(startButton)
                .prefSize(buttonWidth, buttonHeight)
                .space(padding);
        buttonTable.row();
        buttonTable.add(statsButton)
                .prefSize(buttonWidth, buttonHeight)
                .space(padding);
        buttonTable.row();
        buttonTable.add(settingsButton)
                .prefSize(buttonWidth, buttonHeight)
                .space(padding);

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            TextButton quitButton = new TextButton(game.bundle.get("titlescreenQuitButton"), game.skin, "titlemenu");
            quitButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    quit();
                }
            });
            buttonTable.row();
            buttonTable.add(quitButton)
                    .prefSize(buttonWidth, buttonHeight)
                    .space(padding);
        }
        table.add(buttonTable).expandY();
    }

    private void showSodaSelection() {
        currentMenu = CurrentMenu.SELECT_SODA;
        table.clear();

        Label sodaSelectLabel = new Label(game.bundle.get("sodaSelectLabel"), game.skin, "titlemenu");

        Image blueImage = new Image(new SpriteDrawable(atlas.createSprite(Player.PlayerType.BLUE.getSelectTextureName())));
        blueImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showStartGame(Player.PlayerType.BLUE);
            }
        });

        Image orangeImage = new Image(new SpriteDrawable(atlas.createSprite(Player.PlayerType.ORANGE.getSelectTextureName())));
        if (!game.statistics.isSodaUnlocked(Player.PlayerType.ORANGE)) {
            orangeImage.setColor(1, 1, 1, Constants.TITLEMENU_SODA_ALPHA_LOCKED);
        }
        orangeImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.statistics.isSodaUnlocked(Player.PlayerType.ORANGE)) {
                    showStartGame(Player.PlayerType.ORANGE);
                } else {
                    showUnlockDialog(Player.PlayerType.ORANGE);
                }
            }
        });

        Image purpleImage = new Image(new SpriteDrawable(atlas.createSprite(Player.PlayerType.PURPLE.getSelectTextureName())));
        if (!game.statistics.isSodaUnlocked(Player.PlayerType.PURPLE)) {
            purpleImage.setColor(1, 1, 1, Constants.TITLEMENU_SODA_ALPHA_LOCKED);
        }
        purpleImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.statistics.isSodaUnlocked(Player.PlayerType.PURPLE)) {
                    showStartGame(Player.PlayerType.PURPLE);
                } else {
                    showUnlockDialog(Player.PlayerType.PURPLE);
                }
            }
        });

        Image silverImage = new Image(new SpriteDrawable(atlas.createSprite(Player.PlayerType.SILVER.getSelectTextureName())));
        if (!game.statistics.isSodaUnlocked(Player.PlayerType.SILVER)) {
            silverImage.setColor(1, 1, 1, Constants.TITLEMENU_SODA_ALPHA_LOCKED);
        }
        silverImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.statistics.isSodaUnlocked(Player.PlayerType.SILVER)) {
                    showStartGame(Player.PlayerType.SILVER);
                } else {
                    showUnlockDialog(Player.PlayerType.SILVER);
                }
            }
        });

        Image yellowImage = new Image(new SpriteDrawable(atlas.createSprite(Player.PlayerType.YELLOW.getSelectTextureName())));
        if (!game.statistics.isSodaUnlocked(Player.PlayerType.YELLOW)) {
            yellowImage.setColor(1, 1, 1, Constants.TITLEMENU_SODA_ALPHA_LOCKED);
        }
        yellowImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.statistics.isSodaUnlocked(Player.PlayerType.YELLOW)) {
                    showStartGame(Player.PlayerType.YELLOW);
                } else {
                    showUnlockDialog(Player.PlayerType.YELLOW);
                }
            }
        });

        table.add(sodaSelectLabel)
                .colspan(2)
                .expandY()
                .space(padding);
        table.row();
        table.add(blueImage)
                .colspan(2)
                .prefWidth(game.calculateImageWidth(blueImage.getWidth()))
                .prefHeight(game.calculateImageHeight(blueImage.getWidth(), blueImage.getHeight()))
                .space(padding);
        table.row();
        table.add(orangeImage)
                .prefWidth(game.calculateImageWidth(orangeImage.getWidth()))
                .prefHeight(game.calculateImageHeight(orangeImage.getWidth(), orangeImage.getHeight()))
                .space(padding);
        table.add(purpleImage)
                .prefWidth(game.calculateImageWidth(purpleImage.getWidth()))
                .prefHeight(game.calculateImageHeight(purpleImage.getWidth(), purpleImage.getHeight()))
                .space(padding);
        table.row();
        table.add(silverImage)
                .prefWidth(game.calculateImageWidth(silverImage.getWidth()))
                .prefHeight(game.calculateImageHeight(silverImage.getWidth(), silverImage.getHeight()))
                .space(padding);
        table.add(yellowImage)
                .prefWidth(game.calculateImageWidth(yellowImage.getWidth()))
                .prefHeight(game.calculateImageHeight(yellowImage.getWidth(), yellowImage.getHeight()))
                .space(padding);
        table.row();
        table.add(backButton)
                .colspan(2)
                .expandY()
                .prefSize(buttonWidth, buttonHeight)
                .space(padding);
    }

    private void showStartGame(final Player.PlayerType playerType) {
        currentMenu = CurrentMenu.START_GAME;
        table.clear();

        // Get large soda can Texture and calculate dimensions in UI scale
        Texture sodaCanTexture = game.manager.get(playerType.getLargeTextureName(), Texture.class);
        float imageWidth = game.calculateImageWidth(sodaCanTexture.getWidth());
        float imageHeight = game.calculateImageHeight(sodaCanTexture.getWidth(), sodaCanTexture.getHeight());

        Image sodaCanImage = new Image(new TextureRegionDrawable(sodaCanTexture));
        // Set origin to centre for spin effect
        sodaCanImage.setOrigin(imageWidth / 2,imageHeight / 2);

        // Set up spin and shrink actions, start game when finished
        ParallelAction parallelAction = new ParallelAction();
        RotateByAction rotateByAction = Actions.rotateBy(Constants.UNLOCK_SODA_ROTATION, 1);
        rotateByAction.setInterpolation(Interpolation.swingOut);
        parallelAction.addAction(rotateByAction);
        parallelAction.addAction(Actions.scaleTo(0, 0, 1));
        SequenceAction sequenceAction = new SequenceAction(
                parallelAction,
                run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new GameScreen(game, playerType));
                    }
                })
        );
        sodaCanImage.addAction(sequenceAction);

        table.add(sodaCanImage).prefSize(imageWidth, imageHeight);
    }

    private void showUnlockDialog(Player.PlayerType playerType) {
        currentMenu = CurrentMenu.UNLOCK_DIALOG;
        table.clear();

        currentSprite = new Sprite(game.manager.get(playerType.getLargeTextureName(), Texture.class));
        currentSprite.setCenter(Constants.GAME_WIDTH / 2f, game.getGameHeight() / 2f);
        currentSprite.setAlpha(Constants.TITLEMENU_SODA_ALPHA_LOCKED);

        String text = game.bundle.get("unlockDialogPrefix") + "\n\n";
        switch (playerType) {
            case ORANGE:
                text += game.bundle.get("unlockDialogOrange");
                break;
            case PURPLE:
                text += game.bundle.get("unlockDialogPurple");
                break;
            case SILVER:
                text += game.bundle.get("unlockDialogSilver");
                break;
            case YELLOW:
                text += game.bundle.get("unlockDialogYellow");
                break;
            case BLUE:
            default:
                text = "This case should be impossible to reach";
                break;
        }
        text += "\n";

        Label unlockDialogLabel = new Label(text, game.skin, "statistics");
        unlockDialogLabel.setWrap(true);

        table.add(unlockDialogLabel)
                .prefWidth(game.getUiWidth())
                .space(padding);
        table.row();
        table.add(backButton)
                .prefSize(buttonWidth, buttonHeight)
                .space(padding);
    }

    private void showSettingsMenu() {
        currentMenu = CurrentMenu.SETTINGS;
        table.clear();

        Label headingLabel = new Label(game.bundle.get("titlescreenSettingsButton"), game.skin, "titlemenu");

        musicVolumeLabel = new Label("", game.skin, "titlemenu");
        updateMusicVolumeLabel();

        TextButton decreaseMusicVolumeButton = new TextButton("-", game.skin, "titlemenu");
        decreaseMusicVolumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.decreaseMusicVolume();
                updateMusicVolumeLabel();
                musicVolumeSlider.setValue(game.getMusicVolume());
            }
        });

        musicVolumeSlider = new Slider(0, 1, Constants.VOLUME_STEP_SIZE, false, game.skin, "volume-horizontal");
        musicVolumeSlider.setValue(game.getMusicVolume());
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setMusicVolume(musicVolumeSlider.getValue());
                updateMusicVolumeLabel();
            }
        });

        TextButton increaseMusicVolumeButton = new TextButton("+", game.skin, "titlemenu");
        increaseMusicVolumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.increaseMusicVolume();
                updateMusicVolumeLabel();
                musicVolumeSlider.setValue(game.getMusicVolume());
            }
        });

        soundVolumeLabel = new Label("", game.skin, "titlemenu");
        updateSoundVolumeLabel();

        TextButton decreaseSoundVolumeButton = new TextButton("-", game.skin, "titlemenu");
        decreaseSoundVolumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.decreaseSoundVolume();
                updateSoundVolumeLabel();
                soundVolumeSlider.setValue(game.getSoundVolume());
            }
        });

        soundVolumeSlider = new Slider(0, 1, Constants.VOLUME_STEP_SIZE, false, game.skin, "volume-horizontal");
        soundVolumeSlider.setValue(game.getSoundVolume());
        soundVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setSoundVolume(soundVolumeSlider.getValue());
                updateSoundVolumeLabel();
            }
        });

        TextButton increaseSoundVolumeButton = new TextButton("+", game.skin, "titlemenu");
        increaseSoundVolumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.increaseSoundVolume();
                updateSoundVolumeLabel();
                soundVolumeSlider.setValue(game.getSoundVolume());
            }
        });

        TextButton creditsButton = new TextButton(game.bundle.get("settingsCreditsButton"), game.skin, "titlemenu");
        creditsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showCredits();
            }
        });

        table.add(headingLabel)
                .expandY()
                .space(padding);
        table.row();
        table.add(soundVolumeLabel)
                .space(padding);
        table.row();

        Table soundVolumeTable = new Table();
        soundVolumeTable.add(decreaseSoundVolumeButton)
                .prefSize(buttonHeight)
                .space(padding);
        soundVolumeTable.add(soundVolumeSlider)
                .prefWidth(game.getUiWidth())
                .space(padding);
        soundVolumeTable.add(increaseSoundVolumeButton)
                .prefSize(buttonHeight)
                .space(padding);
        table.add(soundVolumeTable)
                .prefWidth(game.getUiWidth())
                .space(padding);
        table.row();

        table.add(musicVolumeLabel)
                .space(padding);
        table.row();

        Table musicVolumeTable = new Table();
        musicVolumeTable.add(decreaseMusicVolumeButton)
                .prefSize(buttonHeight)
                .space(padding);
        musicVolumeTable.add(musicVolumeSlider)
                .prefWidth(game.getUiWidth())
                .space(padding);
        musicVolumeTable.add(increaseMusicVolumeButton)
                .prefSize(buttonHeight)
                .space(padding);
        table.add(musicVolumeTable)
                .prefWidth(game.getUiWidth())
                .space(padding);
        table.row();

        Table buttonTable = new Table();
        buttonTable.add(creditsButton)
                .prefSize(buttonWidth, buttonHeight)
                .space(padding);
        buttonTable.row();
        buttonTable.add(backButton)
                .prefSize(buttonWidth, buttonHeight)
                .space(padding);
        table.add(buttonTable)
                .expandY()
                .space(padding);
    }

    private void showCredits() {
        currentMenu = CurrentMenu.CREDITS;
        table.clear();

        Label headingLabel = new Label(game.bundle.get("settingsCreditsButton"), game.skin, "titlemenu");

        FileHandle file = Gdx.files.internal("credits.txt");
        String creditsText = file.readString("UTF-8");
        Label creditsLabel = new Label(creditsText, game.skin, "game");
        creditsLabel.setWrap(true);
        ScrollPane creditsPane = new ScrollPane(creditsLabel, game.skin, "credits");
        creditsPane.setFadeScrollBars(false);

        table.add(headingLabel)
                .expandY()
                .space(padding);
        table.row();

        table.add(creditsPane)
                .prefWidth(Gdx.graphics.getBackBufferWidth())
                .space(padding);
        table.row();

        table.add(backButton)
                .expandY()
                .prefSize(buttonWidth, buttonHeight)
                .space(padding);
    }

    private void showStatistics() {
        currentMenu = CurrentMenu.STATISTICS;
        table.clear();

        Label headingLabel = new Label(game.bundle.get("titlescreenStatisticsButton"), game.skin, "titlemenu");

        String bp = game.bundle.get("bulletPoint") + " ";
        String nl = "\n";
        String statisticsString =
                bp + game.bundle.get("statisticsThrown") + ": " + game.formatter.commaPrint(game.statistics.getTotalCansThrown()) + nl
                + bp + game.bundle.get("statisticsDrunk") + ": " + game.formatter.commaPrint(game.statistics.getTotalCansDelivered()) + nl
                + bp + game.bundle.get("statisticsHighScore") + ": " + game.formatter.commaPrint(game.statistics.getHighScore()) + nl
                + bp + game.bundle.get("statisticsPoints") + ": " + game.formatter.commaPrint(game.statistics.getTotalPointsScored()) + nl
                + bp + game.bundle.get("statisticsAnimalsQuenched") + ": " + game.statistics.getAnimalsSuperhit() + nl
                + bp + game.bundle.get("statisticsPlantsDestroyed") + ": " + game.statistics.getPlantsSuperHit() + nl
                + bp + game.bundle.get("statisticsLongestSession") + ": " + game.displayTime(game.statistics.getLongestSession()) + nl
                + bp + game.bundle.get("statisticsTime") + ": " + game.displayTime(game.statistics.getTotalTimePlayed()) + nl
                + bp + game.bundle.get("statisticsUnlocked") + ": " + game.statistics.getSodasUnlocked();
        Label statisticsLabel = new Label(statisticsString, game.skin, "statistics");
        statisticsLabel.setWrap(true);
        statisticsLabel.setAlignment(Align.center);

        TextButton backButton = new TextButton(game.bundle.get("backButton"), game.skin, "titlemenu");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goBack();
            }
        });

        TextButton resetStatisticsButton = new TextButton(game.bundle.get("statisticsResetButton"), game.skin, "titlemenu");
        resetStatisticsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showResetStatistics();
            }
        });

        table.add(headingLabel)
                .expandY()
                .space(padding);
        table.row();
        table.add(statisticsLabel).prefWidth(game.getUiWidth());
        table.row();
        Table buttonTable = new Table();
        buttonTable.add(backButton)
                .prefSize(buttonWidth, buttonHeight)
                .space(padding);
        buttonTable.row();
        buttonTable.add(resetStatisticsButton)
                .prefSize(buttonWidth, buttonHeight)
                .space(padding);
        table.add(buttonTable).expandY();
    }

    private void showResetStatistics() {
        currentMenu = CurrentMenu.RESET_STATISTICS;
        table.clear();

        Label resetHeadingLabel = new Label(game.bundle.get("statisticsResetLabel"), game.skin, "titlemenu");

        Label resetQuestionLabel = new Label(game.bundle.get("statisticsResetQuestion"), game.skin, "statistics");
        resetQuestionLabel.setWrap(true);
        resetQuestionLabel.setAlignment(Align.center);

        TextButton resetNoButton = new TextButton(game.bundle.get("statisticsResetNo"), game.skin, "titlemenu");
        resetNoButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goBack();
            }
        });

        TextButton resetYesButton = new TextButton(game.bundle.get("statisticsResetYes"), game.skin, "titlemenu");
        resetYesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.statistics.clear();
                game.statistics.save();
                game.statistics.load();
                showStatistics();
            }
        });

        table.add(resetHeadingLabel).space(padding);
        table.row();
        table.add(resetQuestionLabel)
                .prefWidth(Gdx.graphics.getBackBufferWidth())
                .space(padding);
        table.row();
        table.add(resetNoButton)
                .prefSize(buttonWidth, buttonHeight)
                .space(padding);
        table.row();
        table.add(resetYesButton)
                .prefSize(buttonWidth, buttonHeight)
                .space(padding);
    }

    private void updateMusicVolumeLabel() {
        musicVolumeLabel.setText(game.bundle.get("settingsMusicVolumeLabel") + ": " + (int) (game.getMusicVolume() * 10));
    }

    private void updateSoundVolumeLabel() {
        soundVolumeLabel.setText(game.bundle.get("settingsSoundVolumeLabel") + ": " + (int) (game.getSoundVolume() * 10));
    }

    private void goBack() {
        switch (currentMenu) {
            case MAIN:
                quit();
                break;
            case SETTINGS:
                game.flushSettings();
                showMainMenu();
                break;
            case SELECT_SODA:
            case STATISTICS:
                showMainMenu();
                break;
            case UNLOCK_DIALOG:
                showSodaSelection();
                break;
            case RESET_STATISTICS:
                showStatistics();
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
//        ScreenUtils.clear(Constants.BACKGROUND_COLOUR);
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOUR.r, Constants.BACKGROUND_COLOUR.g, Constants.BACKGROUND_COLOUR.b, Constants.BACKGROUND_COLOUR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (currentMenu == CurrentMenu.UNLOCK_DIALOG) {
            // draw sprites
            viewport.apply();
            game.batch.setProjectionMatrix(viewport.getCamera().combined);
            game.batch.begin();
            currentSprite.draw(game.batch);
            game.batch.end();
        }

        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.BACK:
            case Input.Keys.ESCAPE:
                goBack();
                break;
//            case Input.Keys.F:
//                if (Gdx.graphics.isFullscreen()) {
//                    Gdx.graphics.setWindowedMode(Constants.DESKTOP_WIDTH, Constants.DESKTOP_HEIGHT);
//                } else {
//                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
//                }
//                showMainMenu();
//                break;
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
