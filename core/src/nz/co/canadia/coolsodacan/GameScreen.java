package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Main game screen
 */

public class GameScreen implements Screen, InputProcessor {

    private final CoolSodaCan game;
    private final Player player;
    private final Viewport viewport;
    private final Stage bannerStage;
    private final Stage uiStage;
    private final Stage menuStage;
    private final TextureAtlas atlas;
    private final Array<GameObject> gameObjectArray;
    private final Array<Hittable> hittableArray;
    private final Array<AnimatedCan> animatedCanArray;
    private final Table gameUiTable;
    private final Table menuUiTable;
    private final InputMultiplexer multiplexer;
    private final float menuButtonWidth;
    private final float buttonHeight;
    private final float gameUiButtonWidth;
    private final ObjectMap<Player.PlayerType, Boolean> sodaIsUnlocked;
    private final Table menuBox;
    private final Array<ParticleEffectPool.PooledEffect> effects;
    private final ParticleEffectPool pointsBaseEffectPool;
    private final ParticleEffectPool pointsHighEffectPool;
    private float nextAnimatedCan;
    private float timeElapsed;
    private float lastSaved;
    private float nextGrass;
    private float nextPlant;
    private float nextAnimal;
    private boolean playerIsFiring;
    private int cansThrown;
    private int cansDelivered;
    private int score;
    private Label cansThrownLabel;
    private Label cansDeliveredLabel;
    private Label scoreLabel;
    private Label timeLabel;
    private Image sodaImage;

    private enum GameState { ACTIVE, PAUSED }
    private GameState currentState;

    GameScreen(CoolSodaCan game, Player.PlayerType playerType) {
        this.game = game;
        if (game.debugUnlocks) {
            timeElapsed = 55;
        } else {
            timeElapsed = 0;
        }
        lastSaved = 0;
        playerIsFiring = false;
        cansThrown = 0;
        cansDelivered = 0;
        score = 0;
        currentState = GameState.ACTIVE;
        menuButtonWidth = game.getUiWidth() * Constants.GAMEMENU_BUTTON_WIDTH;
        buttonHeight = menuButtonWidth * Constants.GAMEMENU_BUTTON_RELATIVE_HEIGHT;
        gameUiButtonWidth = game.getUiWidth() * Constants.GAMEUI_BUTTON_WIDTH;
        effects = new Array<>();
        sodaIsUnlocked = new ObjectMap<>(Player.PlayerType.values().length);
        for (Player.PlayerType pt : Player.PlayerType.values()) {
            sodaIsUnlocked.put(pt, game.statistics.isSodaUnlocked(pt));
        }

        atlas = game.manager.get("graphics/graphics.atlas", TextureAtlas.class);

        ParticleEffect pointsBaseEffect = new ParticleEffect();
        pointsBaseEffect.load(Gdx.files.internal("particleEffects/points_base.p"), atlas);
        pointsBaseEffectPool = new ParticleEffectPool(pointsBaseEffect, 1, 2);
        ParticleEffect pointsHighEffect = new ParticleEffect();
        pointsHighEffect.load(Gdx.files.internal("particleEffects/points_high.p"), atlas);
        pointsHighEffectPool = new ParticleEffectPool(pointsHighEffect, 1, 2);

        // create player object
        player = new Player(game.getGameHeight(), atlas, playerType);

        // create game objects
        gameObjectArray = new Array<>();
        hittableArray = new Array<>();

        // Create grass
        int nGrass = MathUtils.round(MathUtils.randomTriangular(
                Constants.MIN_GRASS_START, Constants.MAX_GRASS_START));
        for (int i = 0; i < nGrass; i++) {
            gameObjectArray.add(new Grass(MathUtils.random(0, game.getGameHeight()), atlas));
        }
        nextGrass = MathUtils.randomTriangular(0, Constants.MAX_GRASS_DISTANCE) / Constants.WORLD_MOVEMENT_SPEED;

        // Create plants
        int nPlant = MathUtils.round(MathUtils.randomTriangular(
                Constants.MIN_PLANT_START, Constants.MAX_PLANT_START));
        for (int i = 0; i < nPlant; i++) {
            Plant plant = new Plant(MathUtils.random(0, game.getGameHeight()), atlas);
            gameObjectArray.add(plant);
            hittableArray.add(plant);
        }
        nextPlant = MathUtils.randomTriangular(0, Constants.MAX_PLANT_DISTANCE) / Constants.WORLD_MOVEMENT_SPEED;

        // Create animals
        int nAnimal = MathUtils.round(MathUtils.randomTriangular(
                Constants.MIN_ANIMAL_START, Constants.MAX_ANIMAL_START));
        for (int i = 0; i < nAnimal; i++) {
            Animal animal = new Animal(MathUtils.random(0, game.getGameHeight()), atlas, player.getPlayerType().getExplosionColor());
            gameObjectArray.add(animal);
            hittableArray.add(animal);
        }
        nextAnimal = MathUtils.randomTriangular(0, Constants.MAX_ANIMAL_DISTANCE) / Constants.WORLD_MOVEMENT_SPEED;

        // Sort gameObjectArray so we can render in reverse Y order
        gameObjectArray.sort();

        // Create AnimatedCan array
        animatedCanArray = new Array<>();
        nextAnimatedCan = 0;

        // create the game viewport
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.GAME_WIDTH, game.getGameHeight());
        viewport = new FitViewport(Constants.GAME_WIDTH,
                game.getGameHeight(), camera);

        // Create the side banners.
        // These are in a different Viewport to game objects so they can be wholly or partially "off-screen"
        Viewport bannerViewport = new FillViewport(Constants.BANNER_WIDTH, Constants.BANNER_HEIGHT);
        bannerStage = new Stage(bannerViewport);
        Texture bannerLeftTexture = game.manager.get("textures_large/banner_left.jpg", Texture.class);
        Texture bannerRightTexture = game.manager.get("textures_large/banner_right.jpg", Texture.class);
        Image bannerLeftImage = new Image(bannerLeftTexture);
        bannerLeftImage.setPosition(0, 0);
        bannerStage.addActor(bannerLeftImage);
        Image bannerRightImage = new Image(bannerRightTexture);
        bannerRightImage.setPosition(bannerStage.getWidth() - bannerRightTexture.getWidth(), 0);
        bannerStage.addActor(bannerRightImage);

        // Create the Game UI
        Viewport uiViewport = new FitViewport(game.getUiWidth(), Gdx.graphics.getBackBufferHeight());
        uiStage = new Stage(uiViewport);
        gameUiTable = new Table();
        gameUiTable.setFillParent(true);
        gameUiTable.pad(game.getGameUiPadding());
        uiStage.addActor(gameUiTable);
        sodaImage = new Image();

        // Create the Game Menu
        menuStage = new Stage(uiViewport);
        menuUiTable = new Table();
        menuUiTable.setFillParent(true);
        menuStage.addActor(menuUiTable);
        menuBox = new Table();
        menuBox.pad(game.getMenuUiPadding());
        menuBox.setSkin(game.skin);
        menuBox.setBackground("default-rect");

        showGameUi();

        multiplexer = new InputMultiplexer();
        setGameInputs();

        Gdx.input.setCursorPosition(
                MathUtils.round(Gdx.graphics.getBackBufferWidth() * Constants.CURSOR_START_X),
                MathUtils.round(Gdx.graphics.getBackBufferHeight() * Constants.CURSOR_START_Y));
    }

    private void setGameInputs() {
        multiplexer.clear();
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.input.setCursorCatched(true);
        }
    }

    private void setMenuInputs() {
        multiplexer.clear();
        multiplexer.addProcessor(menuStage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.input.setCursorCatched(false);
        }
    }

    private void showGameUi() {
        float columnWidth = game.getUiWidth() * Constants.GAMEUI_COLUMN_WIDTH;

        gameUiTable.clear();
        gameUiTable.top().left();

        Table leftColumn = new Table().left();
        // Cans thrown
        cansThrownLabel = new Label("", game.skin.get("game", Label.LabelStyle.class));
        setCansThrownLabel();
        leftColumn.add(cansThrownLabel).left();
        leftColumn.row();
        // Cans delivered
        cansDeliveredLabel = new Label("", game.skin.get("game", Label.LabelStyle.class));
        setCansDeliveredLabel();
        leftColumn.add(cansDeliveredLabel).left();

        Table middleColumn = new Table();
        // Score
        scoreLabel = new Label("", game.skin.get("game", Label.LabelStyle.class));
        setScoreLabel();
        middleColumn.add(scoreLabel).left();
        middleColumn.row();
        // Timer
        timeLabel = new Label("", game.skin.get("game", Label.LabelStyle.class));
        setTimeLabel();
        middleColumn.add(timeLabel);

        Table rightColumn = new Table().right();
        switch (Gdx.app.getType()) {
            case Desktop:
            case WebGL:
                // Menu label Desktop
                Label menuLabel = new Label(game.bundle.get("gameUiMenuLabelDesktop"), game.skin.get("game", Label.LabelStyle.class));
                rightColumn.add(menuLabel).right();
                break;
            case Android:
            case iOS:
                // Menu button mobile
                TextButton menuButton = new TextButton(game.bundle.get("gameUiMenuButton"),
                        game.skin.get("game", TextButton.TextButtonStyle.class));
                menuButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        goBack();
                    }
                });
                // Button should be 2/3 as wide as a column and as tall as the column next to it
                rightColumn.add(menuButton).prefSize(gameUiButtonWidth, buttonHeight);
                break;
        }

        gameUiTable.add(leftColumn).prefWidth(columnWidth).left();
        gameUiTable.add(middleColumn).prefWidth(columnWidth).center();
        gameUiTable.add(rightColumn).prefWidth(columnWidth).right();
    }

    private void showMenu() {
        playerIsFiring = false;
        menuBox.clear();

        Label pauseLabel = new Label(game.bundle.get("gameMenuLabel"), game.skin, "game");
        menuBox.add(pauseLabel).space(game.getMenuUiPadding());
        menuBox.row();

        TextButton continueButton = new TextButton(game.bundle.get("gameMenuContinueButton"), game.skin, "game");
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                continueGame();
            }
        });
        menuBox.add(continueButton).prefSize(menuButtonWidth, buttonHeight)
                .space(game.getMenuUiPadding());
        menuBox.row();

        TextButton exitButton = new TextButton(game.bundle.get("gameMenuExitButton"), game.skin, "game");
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exit();
            }
        });
        menuBox.add(exitButton).prefSize(menuButtonWidth, buttonHeight)
                .space(game.getMenuUiPadding());

        menuUiTable.add(menuBox);
    }

    private void showSodaUnlocked(Player.PlayerType pt) {
        playerIsFiring = false;
        setMenuInputs();
        currentState = GameState.PAUSED;
        menuBox.clear();

        Label sodaUnlockedLabel = new Label(game.bundle.get("gameSodaUnlockedLabel"), game.skin, "game");

        Sprite sodaSprite = atlas.createSprite(pt.getSmallTextureName());
        sodaImage = new Image(new SpriteDrawable(sodaSprite));
        float imageWidth = game.calculateImageWidth(sodaImage.getWidth());
        float imageHeight = game.calculateImageHeight(sodaImage.getWidth(), sodaImage.getHeight());
        sodaImage.setOrigin(imageWidth * Constants.PLAYER_CENTRE_OFFSET_X, imageHeight / 2);
        RotateByAction rotateByAction = Actions.rotateBy(Constants.UNLOCK_SODA_ROTATION, Constants.UNLOCK_SODA_DURATION);
        rotateByAction.setInterpolation(Interpolation.swingOut);
        RepeatAction repeatAction = Actions.forever(rotateByAction);
        sodaImage.addAction(repeatAction);

        TextButton continueButton = new TextButton(game.bundle.get("gameSodaUnlockedContinueButton"), game.skin, "game");
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                continueGame();
            }
        });

        menuBox.add(sodaUnlockedLabel).space(game.getMenuUiPadding());
        menuBox.row();
        menuBox.add(sodaImage)
                .center()
                .prefSize(imageWidth, imageHeight)
                .space(game.getMenuUiPadding());
        menuBox.row();
        menuBox.add(continueButton)
                .prefSize(menuButtonWidth, buttonHeight)
                .space(game.getMenuUiPadding());
        menuUiTable.add(menuBox);

        Gdx.app.log("GameScreen", pt.name() + " soda can unlocked!");
    }

    private void continueGame() {
        menuUiTable.clear();
        currentState = GameState.ACTIVE;
        showGameUi();
        setGameInputs();
    }

    private void spawnAnimal() {
        Animal animal = new Animal(game.getGameHeight(), atlas, player.getPlayerType().getExplosionColor());
        gameObjectArray.add(animal);
        hittableArray.add(animal);
        nextAnimal = timeElapsed + MathUtils.randomTriangular(0, Constants.MAX_ANIMAL_DISTANCE) / Constants.WORLD_MOVEMENT_SPEED;
    }

    private void spawnGrass() {
        gameObjectArray.add(new Grass(game.getGameHeight(), atlas));
        nextGrass = timeElapsed + MathUtils.randomTriangular(0, Constants.MAX_GRASS_DISTANCE) / Constants.WORLD_MOVEMENT_SPEED;
    }

    private void spawnPlant() {
        Plant plant = new Plant(game.getGameHeight(), atlas);
        gameObjectArray.add(plant);
        hittableArray.add(plant);
        nextPlant = timeElapsed + MathUtils.randomTriangular(0, Constants.MAX_PLANT_DISTANCE) / Constants.WORLD_MOVEMENT_SPEED;
    }

    private void addScoreEffect(int points, float x, float y) {
        try {
            // Triger a score particle
            ParticleEffectPool.PooledEffect scoreEffect;
            switch (points) {
                case Constants.ANIMAL_BASE_POINTS:
                    scoreEffect = pointsBaseEffectPool.obtain();
                    break;
                case Constants.ANIMAL_HIGH_POINTS:
                    scoreEffect = pointsHighEffectPool.obtain();
                    break;
                default:
                    throw new RuntimeException("Unexpected points amount: " + points);
            }
            scoreEffect.setPosition(x, y);
            effects.add(scoreEffect);
        } catch (RuntimeException e) {
            Gdx.app.error("GameScreen", "Unable to create score particleEffect: " + e);
        }
    }

    private void exit() {
        game.statistics.save();
        game.setScreen(new TitleScreen(game));
    }

    private void goBack() {
        switch (currentState) {
            case ACTIVE:
                showMenu();
                setMenuInputs();
                currentState = GameState.PAUSED;
                break;
            case PAUSED:
                switch (Gdx.app.getType()) {
                    case Desktop:
                    case WebGL:
                        continueGame();
                        break;
                    case Android:
                        exit();
                        break;
                }
                break;
        }
    }

    private void updateCansDelivered(int nCans) {
        cansDelivered += nCans;
        game.statistics.updateTotalCansDelivered(nCans);
        setCansDeliveredLabel();
    }

    private void setCansDeliveredLabel() {
        cansDeliveredLabel.setText(game.bundle.get("gameUiDeliveredLabel") + ": " + cansDelivered);
    }

    private void updateScore(int points) {
        score += points;
        game.statistics.updateTotalPointsScored(points);
        game.statistics.updateHighScore(score);
        setScoreLabel();
    }

    private void setScoreLabel() {
        scoreLabel.setText(game.bundle.get("gameUiScoreLabel") + ": " + game.formatter.printScore(score));
    }

    private void updateTime(float delta) {
        timeElapsed += delta;
        lastSaved += delta;
        game.statistics.updateTotalTimePlayed(delta);
        game.statistics.updateLongestSession(timeElapsed);
        setTimeLabel();
    }

    private void setTimeLabel() {
        timeLabel.setText(game.bundle.get("gameUiTimeLabel") + ": "
                + game.displayTime(timeElapsed));
    }

    private void throwCan() {
        animatedCanArray.add(new AnimatedCan(player, atlas));
        nextAnimatedCan = timeElapsed + Constants.ANIMATED_CAN_RATE;
        cansThrown++;
        game.statistics.incrementTotalCansThrown();
        setCansThrownLabel();
    }

    private void setCansThrownLabel() {
        cansThrownLabel.setText(game.bundle.get("gameUiThrownLabel") + ": " + cansThrown);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Constants.BACKGROUND_COLOUR);
        viewport.getCamera().update();

        if (currentState == GameState.ACTIVE) {
            updateTime(delta);

            if (lastSaved > Constants.AUTOSAVE_INTERVAL) {
                game.statistics.save();
                lastSaved = 0;
            }

            // Remove old objects
            for (int i = 0; i < gameObjectArray.size; i++) {
                if (gameObjectArray.get(i).getTopY() < 0) {
                    gameObjectArray.removeIndex(i);
                }
            }
            for (int i = 0; i < animatedCanArray.size; i++) {
                if (animatedCanArray.get(i).getY() > game.getGameHeight()) {
                    animatedCanArray.removeIndex(i);
                }
            }

            // Add new objects to top of screen
            if (timeElapsed > nextAnimal) {
                spawnAnimal();
            }
            if (timeElapsed > nextGrass) {
                spawnGrass();
            }
            if (timeElapsed > nextPlant) {
                spawnPlant();
            }

            // Add new cans if player firing
            if (playerIsFiring) {
                if (timeElapsed > nextAnimatedCan) {
                    throwCan();
                }
            }

            // Check for can/hittable collisions
            for (AnimatedCan ac : animatedCanArray) {
                if (ac.isActive()) {
                    for (Hittable h : hittableArray) {
                        if (ac.getHitBox().overlaps(h.getHitBox()) & h.isHittable()) {
                            // Hit the hittable
                            h.hit();
                            updateCansDelivered(h.getSodasDrunk());
                            int points = h.getPoints();
                            if (points > 0) {
                                updateScore(points);
                                addScoreEffect(points, ac.getCenterX(), ac.getY());
                            }
                            if (h.getHitState() == Hittable.State.SUPER_HIT) {
                                game.statistics.incrementSuperHit(h.getType());
                            }
                            // Hit the can
                            ac.hit();
                        }
                    }
                }
            }

            // Check for can unlocks
            for (Player.PlayerType pt : Player.PlayerType.values()) {
                if (!sodaIsUnlocked.get(pt)) {
                    if (game.statistics.isSodaUnlocked(pt)) {
                        sodaIsUnlocked.put(pt, game.statistics.isSodaUnlocked(pt));
                        showSodaUnlocked(pt);
                    }
                }
            }

            // update objects
            for (GameObject g : gameObjectArray) {
                g.update(delta);
            }
            for (AnimatedCan ac : animatedCanArray) {
                ac.update(delta);
            }
            player.update(delta);
        } else if (currentState == GameState.PAUSED) {
            menuStage.act(delta);
        }

        // draw sprites
        viewport.apply();
        game.batch.setProjectionMatrix(viewport.getCamera().combined);
        game.batch.begin();
        // Game objects
        for (int i = gameObjectArray.size - 1; i >= 0; i--) {
            gameObjectArray.get(i).draw(game.batch);
        }
        // Animated Cans
        for (AnimatedCan ac : animatedCanArray) {
            ac.draw(game.batch);
        }
        //Draw particle effects
        for (int i = effects.size - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = effects.get(i);
            effect.draw(game.batch, delta);
            if (effect.isComplete()) {
                effect.free();
                effects.removeIndex(i);
            }
        }
        // Player
        player.draw(game.batch);
        game.batch.end();

        // Draw side banners
        bannerStage.getViewport().apply();
        bannerStage.draw();

        // Draw game UI
        uiStage.getViewport().apply();
        uiStage.draw();

        // Draw menu UI
        menuStage.getViewport().apply();
        menuStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        bannerStage.getViewport().update(width, height);
        uiStage.getViewport().update(width, height);
        menuStage.getViewport().update(width, height);
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
        uiStage.dispose();
        for (int i = effects.size - 1; i >= 0; i--)
            effects.get(i).free();
        effects.clear();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.ESCAPE:
            case Input.Keys.BACK:
                goBack();
                break;
            case Input.Keys.F:
                if (Gdx.graphics.isFullscreen()) {
                    Gdx.graphics.setWindowedMode(Constants.DESKTOP_WIDTH, Constants.DESKTOP_HEIGHT);
                } else {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                }
                break;
            case Input.Keys.C:
                Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
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
        if (currentState == GameState.ACTIVE) {
            if (!playerIsFiring) playerIsFiring = true;
            player.setTargetXY(screenX, screenY, viewport);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (currentState == GameState.ACTIVE) {
            playerIsFiring = false;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (currentState == GameState.ACTIVE) {
            player.setTargetXY(screenX, screenY, viewport);
        }
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
