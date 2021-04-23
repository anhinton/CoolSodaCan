package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Main game screen
 */

public class GameScreen implements Screen, InputProcessor {

    private final CoolSodaCan game;
    private final Player player;
    private final Viewport gameViewport;
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
    private final Array<ParticleEffectPool.PooledEffect> effects;
    private final ParticleEffectPool pointsBaseEffectPool;
    private final ParticleEffectPool pointsHighEffectPool;
    private final Pool<AnimatedCan> animatedCanPool;
    private final Pool<Grass> grassPool;
    private final Pool<Plant> plantPool;
    private final Pool<Animal> animalPool;
    private final Sprite bannerLeftSprite;
    private final Sprite bannerRightSprite;
    private final Sound throwSound;
    private final Sound hitSound;
    private final Sound plantSuperhitSound;
    private final Sound animalSuperhitSound;
    private final Sound unlockSound;
    private float nextAnimatedCan;
    private float timeElapsed;
    private float lastSaved;
    private float nextGrass;
    private float nextPlant;
    private float nextAnimal;
    private boolean playerIsFiring;
    private boolean tutorialIsShown;
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

    GameScreen(CoolSodaCan game, final Player.PlayerType playerType) {
        this.game = game;
        if (game.debugUnlocks) {
            timeElapsed = 55;
        } else {
            timeElapsed = 0;
        }
        lastSaved = 0;
        playerIsFiring = false;
        tutorialIsShown = true;
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

        animalSuperhitSound = game.manager.get("sounds/animal_superhit.wav", Sound.class);
        hitSound = game.manager.get("sounds/hit.wav", Sound.class);
        plantSuperhitSound = game.manager.get("sounds/plant_superhit.wav", Sound.class);
        throwSound = game.manager.get("sounds/throw.wav", Sound.class);
        unlockSound = game.manager.get("sounds/unlock.wav", Sound.class);

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
        grassPool = new Pool<Grass>() {
            @Override
            protected Grass newObject() {
                return new Grass(atlas);
            }
        };
        int nGrass = MathUtils.round(MathUtils.randomTriangular(
                Constants.MIN_GRASS_START, Constants.MAX_GRASS_START));
        for (int i = 0; i < nGrass; i++) {
            spawnGrass(MathUtils.random(0, game.getGameHeight()));
        }
        nextGrass = MathUtils.randomTriangular(0, Constants.MAX_GRASS_DISTANCE) / Constants.WORLD_MOVEMENT_SPEED;

        // Create plants
        plantPool = new Pool<Plant>() {
            @Override
            protected Plant newObject() {
                return new Plant(atlas);
            }
        };
        int nPlant = MathUtils.round(MathUtils.randomTriangular(
                Constants.MIN_PLANT_START, Constants.MAX_PLANT_START));
        for (int i = 0; i < nPlant; i++) {
            spawnPlant(MathUtils.random(0, game.getGameHeight()));
        }
        nextPlant = MathUtils.randomTriangular(0, Constants.MAX_PLANT_DISTANCE) / Constants.WORLD_MOVEMENT_SPEED;

        // Create animals
        animalPool = new Pool<Animal>() {
            @Override
            protected Animal newObject() {
                return new Animal(atlas, playerType.getExplosionColor());
            }
        };
        int nAnimal = MathUtils.round(MathUtils.randomTriangular(
                Constants.MIN_ANIMAL_START, Constants.MAX_ANIMAL_START));
        for (int i = 0; i < nAnimal; i++) {
            spawnAnimal(MathUtils.random(0, game.getGameHeight()));
        }
        nextAnimal = MathUtils.randomTriangular(0, Constants.MAX_ANIMAL_DISTANCE) / Constants.WORLD_MOVEMENT_SPEED;

        // Sort gameObjectArray so we can render in reverse Y order
        gameObjectArray.sort();

        // Create AnimatedCan array
        animatedCanArray = new Array<>();
        // Create AnimatedCan Pool
        animatedCanPool = new Pool<AnimatedCan>() {
            @Override
            protected AnimatedCan newObject() {
                return new AnimatedCan(player, atlas);
            }
        };
        nextAnimatedCan = 0;

        // create the game viewport
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.GAME_WIDTH, game.getGameHeight());
        gameViewport = new ExtendViewport(Constants.GAME_WIDTH,
                game.getGameHeight(), camera);

        // Create the side banners.
        // These are in a different Viewport to game objects so they can be wholly or partially "off-screen"
        bannerLeftSprite = new Sprite(game.manager.get("textures_large/banner_left.jpg", Texture.class));
        bannerLeftSprite.setSize(Constants.BANNER_WIDTH, game.getGameHeight());
        bannerLeftSprite.setPosition(-bannerLeftSprite.getWidth(), 0);
        bannerRightSprite = new Sprite(game.manager.get("textures_large/banner_right.jpg", Texture.class));
        bannerRightSprite.setSize(Constants.BANNER_WIDTH, game.getGameHeight());
        bannerRightSprite.setPosition(Constants.GAME_WIDTH, 0);

        // Create the Game UI
        Viewport uiViewport = new FitViewport(game.getUiWidth(), Gdx.graphics.getBackBufferHeight());
        uiStage = new Stage(uiViewport);

        // Game UI elements
        gameUiTable = new Table();
        gameUiTable.setFillParent(true);
        gameUiTable.pad(game.getGameUiPadding());
        gameUiTable.padTop(game.getGameUiPadding() + Gdx.graphics.getSafeInsetTop());
        uiStage.addActor(gameUiTable);
        sodaImage = new Image();

        // Create the Game Menu
        menuStage = new Stage(uiViewport);
        menuUiTable = new Table();
        menuUiTable.setFillParent(true);
        menuUiTable.padTop(Gdx.graphics.getSafeInsetTop());
        menuStage.addActor(menuUiTable);

        showGameUi();
        showTutorial();

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
                // Menu label Desktop
                Label menuLabel = new Label(game.bundle.get("gameUiMenuLabelDesktop"), game.skin.get("game", Label.LabelStyle.class));
                rightColumn.add(menuLabel).right();
                break;
            case Android:
            case iOS:
            case WebGL:
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

    private void showTutorial() {
        menuUiTable.clear();

        String text = "";
        try {
            switch (Gdx.app.getType()) {
                case Desktop:
                case WebGL:
                    text = game.bundle.get("gameTutorialDesktop");
                    break;
                case Android:
                case iOS:
                    text = game.bundle.get("gameTutorialMobile");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + Gdx.app.getType());
            }
        } catch (IllegalStateException e) {
            Gdx.app.error("GameScreen", "showTutorial: Unknown app type " + e);
        }

        Label tutorialLabel = new Label(text, game.skin, "game");
        tutorialLabel.setAlignment(Align.center);

        Table menuBox = new Table();
        menuBox.pad(game.getMenuUiPadding());
        menuBox.setSkin(game.skin);
        menuBox.setBackground("default-rect");

        menuBox.add(tutorialLabel);
        menuUiTable.add(menuBox);
    }

    private void showMenu() {
        playerIsFiring = false;
        menuUiTable.clear();

        Table menuBox = new Table();
        menuBox.pad(game.getMenuUiPadding());
        menuBox.setSkin(game.skin);
        menuBox.setBackground("default-rect");

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
        menuUiTable.clear();
        playerIsFiring = false;
        setMenuInputs();
        currentState = GameState.PAUSED;

        unlockSound.play(game.getSoundVolume());

        Table menuBox = new Table();
        menuBox.pad(game.getMenuUiPadding());
        menuBox.setSkin(game.skin);
        menuBox.setBackground("default-rect");

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

    private void
    freeGameObject(GameObject gameObject) {
        switch(gameObject.getType()) {
            // Free Animal objects
            case "COCO":
            case "HEDGEHOG":
            case "HORSE01":
            case "HORSE02":
            case "YELLOW_RAT":
                animalPool.free((Animal) gameObject);
                break;
            // Free Grass objects
            case "grass":
                grassPool.free((Grass) gameObject);
                break;
            // Free Plant objects
            case "FERN01":
            case "FLOWER01":
            case "TREE01":
            case "TREE02":
                plantPool.free((Plant) gameObject);
                break;
        }
    }

    private void spawnAnimal(int y) {
        Animal animal = animalPool.obtain();
        animal.init(y);
        gameObjectArray.add(animal);
        hittableArray.add(animal);
    }

    private void spawnGrass(int y) {
        Grass grass = grassPool.obtain();
        grass.init(y);
        gameObjectArray.add(grass);
        nextGrass = timeElapsed + MathUtils.randomTriangular(0, Constants.MAX_GRASS_DISTANCE) / Constants.WORLD_MOVEMENT_SPEED;
    }

    private void spawnPlant(int y) {
        Plant plant = plantPool.obtain();
        plant.init(y);
        gameObjectArray.add(plant);
        hittableArray.add(plant);
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
        scoreLabel.setText(game.bundle.get("gameUiScoreLabel") + ": " + game.formatter.commaPrint(score));
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
        throwSound.play(game.getSoundVolume());
        player.throwCan(animatedCanArray, animatedCanPool, atlas);
        nextAnimatedCan = timeElapsed + player.getAnimatedCanInterval();
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
        Gdx.gl.glClearColor(Constants.BACKGROUND_COLOUR.r, Constants.BACKGROUND_COLOUR.g, Constants.BACKGROUND_COLOUR.b, Constants.BACKGROUND_COLOUR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameViewport.getCamera().update();

        if (currentState == GameState.ACTIVE) {
            updateTime(delta);

            if (lastSaved > Constants.AUTOSAVE_INTERVAL) {
                game.statistics.save();
                lastSaved = 0;
            }

            // update objects
            for (GameObject g : gameObjectArray) {
                g.update(delta);
            }
            for (AnimatedCan ac : animatedCanArray) {
                ac.update(delta);
            }
            player.update(delta);

            // Remove old objects
            for (int i = 0; i < hittableArray.size; i++) {
                if (hittableArray.get(i).getTopY() < 0) {
                    hittableArray.removeIndex(i);
                }
            }
            for (int i = 0; i < gameObjectArray.size; i++) {
                if (gameObjectArray.get(i).getTopY() < 0) {
                    GameObject go = gameObjectArray.get(i);
                    gameObjectArray.removeIndex(i);
                    freeGameObject(go);
                }
            }
            for (int i = 0; i < animatedCanArray.size; i++) {
                if (animatedCanArray.get(i).getY() > game.getGameHeight()
                        | animatedCanArray.get(i).getY() + animatedCanArray.get(i).getHeight() < 0
                        | animatedCanArray.get(i).getX() > Constants.GAME_WIDTH
                        | animatedCanArray.get(i).getX() + animatedCanArray.get(i).getWidth() < 0) {
                    AnimatedCan ac = animatedCanArray.get(i);
                    animatedCanArray.removeIndex(i);
                    animatedCanPool.free(ac);
                }
            }

            // Add new objects to top of screen
            if (timeElapsed > nextAnimal) {
                spawnAnimal(game.getGameHeight());
                nextAnimal = timeElapsed + MathUtils.randomTriangular(0, Constants.MAX_ANIMAL_DISTANCE) / Constants.WORLD_MOVEMENT_SPEED;
            }
            if (timeElapsed > nextGrass) {
                spawnGrass(game.getGameHeight());
            }
            if (timeElapsed > nextPlant) {
                spawnPlant(game.getGameHeight());
                nextPlant = timeElapsed + MathUtils.randomTriangular(0, Constants.MAX_PLANT_DISTANCE) / Constants.WORLD_MOVEMENT_SPEED;
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
                            // Play sound
                            hitSound.play(game.getSoundVolume());
                            // Hit the hittable
                            h.hit();
                            updateCansDelivered(h.getSodasDrunk());
                            int points = h.getPoints();
                            if (points > 0) {
                                updateScore(points);
                                addScoreEffect(points, ac.getCenterX(), ac.getY());
                            }
                            if (h.getHitState() == Hittable.State.SUPER_HIT) {
                                switch(h.getClass().getSimpleName()) {
                                    case "Animal":
                                        animalSuperhitSound.play(game.getSoundVolume());
                                        break;
                                    case "Plant":
                                        plantSuperhitSound.play(game.getSoundVolume());
                                        break;
                                }
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
        } else if (currentState == GameState.PAUSED) {
            menuStage.act(delta);
        }

        // draw sprites
        gameViewport.apply();
        game.batch.setProjectionMatrix(gameViewport.getCamera().combined);
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
        // Banners
        bannerLeftSprite.draw(game.batch);
        bannerRightSprite.draw(game.batch);
        game.batch.end();

        // Draw game UI
        uiStage.getViewport().apply();
        uiStage.draw();

        // Draw menu UI
        menuStage.getViewport().apply();
        menuStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
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
            case Input.Keys.SPACE:
                if (tutorialIsShown) {
                    menuUiTable.clear();
                    tutorialIsShown = false;
                }
                if (currentState == GameState.ACTIVE) {
                    if (!playerIsFiring) playerIsFiring = true;
                }
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            if (currentState == GameState.ACTIVE) {
                playerIsFiring = false;
            }
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (tutorialIsShown) {
            menuUiTable.clear();
            tutorialIsShown = false;
        }
        if (currentState == GameState.ACTIVE) {
            if (!playerIsFiring) playerIsFiring = true;
            player.setTargetXY(screenX, screenY, gameViewport);
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
            player.setTargetXY(screenX, screenY, gameViewport);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        player.setTargetXY(screenX, screenY, gameViewport);
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
