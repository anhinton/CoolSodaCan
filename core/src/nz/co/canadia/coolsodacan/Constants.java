package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;

/**
 * Game constants
 */

public class Constants {
    public static final String GAME_NAME = "Cool Soda Can";
    public static final String GAME_STATISTICS_PATH = "nz.co.canadia.coolsodacan.statistics";
    public static final float AUTOSAVE_INTERVAL = 60;

    public static final Color BACKGROUND_COLOUR = new Color(0f / 255, 205f / 255, 111f / 255, 1);
    public static final int GAME_WIDTH = 720;
    public static final int GAME_HEIGHT = 1280;
    public static final int BANNER_WIDTH = 1280;
    public static final int BANNER_HEIGHT = 720;

    // app dimensions for Desktop
    public static final int DESKTOP_WIDTH = 1280;
    public static final int DESKTOP_HEIGHT = 720;

    // app dimensions for Web
    public static final int HTML_WIDTH = 720;
    public static final int HTML_HEIGHT = 720;

    // Game constants
    public static final float PLAYER_SPEED = 2560f;
    public static final float WORLD_MOVEMENT_SPEED = 160f;
    public static final float CURSOR_START_X = 1 / 2f;
    public static final float CURSOR_START_Y = 1 / 2f;

    // Font constants
    public static final String FONT_CHARACTERS = FreeTypeFontGenerator.DEFAULT_CHARS + "\u2022";
    public static final Color FONT_COLOR = Color.WHITE;
    public static final Color FONT_SHADOW_COLOR = Color.BLACK;
    public static final int GAMEUI_FONT_SIZE = 32;
    public static final int GAMEUI_FONT_SHADOW_OFFSET = 2;
    public static final int TITLEMENU_FONT_SIZE = 64;
    public static final int TITLEMENU_FONT_SHADOW_OFFSET = 4;
    public static final int STATISTICS_FONT_SIZE = 48;
    public static final int STATISTICS_FONT_SHADOW_OFFSET = 3;

    // UI constants
    public static final int GAMEUI_PADDING = 5;
    public static final float GAMEUI_COLUMN_PROPORTION = 1 / 3f;
    public static final float GAMEUI_MENUBUTTON_SCALE = 1.5f;
    public static final int MENUUI_PADDING = 20;

    // Particle effect constants
    public static final Color BLUE_COLOR = new Color(0.165f, 0.427f, 0.925f, 1);
    public static final Color SILVER_COLOR = new Color(0.808f, 0.808f,0.745f, 1);
    public static final Color ORANGE_COLOR = new Color(0.965f, 0.482f,0.122f, 1);
    public static final Color PURPLE_COLOR = new Color(0.780f, 0, 0.6f, 1);
    public static final Color YELLOW_COLOR = new Color(0.965f, 0.949f, 0.121f, 1);
    // Particle size is set to min of sprite(width/height), scaled by the following
    public static final float PLANT_PARTICLE_SCALE = 1;
    public static final float ANIMAL_PARTICLE_SCALE = 0.5f;

    // Animal constants
    public static final int ANIMAL_SODAS_DRUNK = 1;
    public static final int ANIMAL_BASE_POINTS = 6;
    public static final int ANIMAL_HIGH_POINTS = 666;

    // Plant constants
    public static final int PLANT_SODAS_DRUNK = 0;
    public static final int PLANT_BASE_POINTS = 0;
    public static final int PLANT_HIGH_POINTS = ANIMAL_BASE_POINTS;

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
    public static final float WIGGLE_AMPLITUDE_IN_DEGREES = 5.0f;
    public static float ANIMAL_SHAKE_DURATION = 0.25f;
    public static int ANIMAL_SHAKE_MAGNITUDE = 5;

    // Animation constants
    public static final float CAN_FRAME_DURATION = 0.066f;
    public static final float ANIMATED_CAN_SPEED = GAME_HEIGHT / 4f;
    public static final float ANIMATED_CAN_DISTANCE = GAME_HEIGHT / 4f;
    // My player cans have drop shadows, meaning the "centre" of the can is not in the middle.
    // As at 2021-03-01 I have 80px wide cans with a 10px drop shadow
    public static final float PLAY_CENTRE_OFFSET = 4 / 9f;
}