package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

/**
 * Game constants
 */

public class Constants {
    public static final String GAME_NAME = "Cool Soda Can by Ashley Noel Hinton";

    public static final Color BACKGROUND_COLOUR = new Color(0f / 255, 205f / 255, 111f / 255, 1);
    public static final int GAME_WIDTH = 720;
    public static final int GAME_HEIGHT = 1280;
    public static final int BANNER_WIDTH = 1280;
    public static final int BANNER_HEIGHT = 720;

    // app dimensions for Desktop
    public static final int DESKTOP_WIDTH = 1280;
    public static final int DESKTOP_HEIGHT = 720;

    // app dimensions for Web
    public static final int HTML_WIDTH = 1280;
    public static final int HTML_HEIGHT = 720;

    // Game constants
    public static final float PLAYER_SPEED = 2560f;
    public static final float WORLD_MOVEMENT_SPEED = 160f;
    public static final float CURSOR_START_X = 1 / 2f;
    public static final float CURSOR_START_Y = 1 / 2f;

    // Spawn constants
    public static final int MIN_GRASS_START = 5;
    public static final int MAX_GRASS_START = 20;
    public static final int MAX_GRASS_DISTANCE = MathUtils.round((float) GAME_HEIGHT / MIN_GRASS_START);
    public static final int MIN_PLANT_START = 2;
    public static final int MAX_PLANT_START = 5;
    public static final int MAX_PLANT_DISTANCE = MathUtils.round((float) GAME_HEIGHT / MIN_PLANT_START);
    public static final int MIN_ANIMAL_START = 3;
    public static final int MAX_ANIMAL_START = 6;
    public static final int MAX_ANIMAL_DISTANCE = MathUtils.round((float) GAME_HEIGHT / MIN_ANIMAL_START);

    // Animal constants
    public static final float DEGREES_PER_SECOND = 10.0f;
    public static final float SHAKE_AMPLITUDE_IN_DEGREES = 5.0f;
}