package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The player object
 */

class Player {
    private final Sprite sprite;
    private final int gameHeight;
    private final PlayerType playerType;
    private Vector2 targetXY;
    private float lastDirectionDegrees;

    public enum PlayerType {
        BLUE    ("blue_soda_small", "blue_soda_select",
                "textures_large/blue_soda.png","blue_anim",
                Constants.BLUE_COLOR, Constants.ANIMATED_CAN_INTERVAL_BLUE),
        ORANGE  ("orange_soda_small", "orange_soda_select",
                "textures_large/orange_soda.png","orange_anim",
                Constants.ORANGE_COLOR, Constants.ANIMATED_CAN_INTERVAL_ORANGE),
        PURPLE  ("purple_soda_small", "purple_soda_select",
                "textures_large/purple_soda.png", "purple_anim",
                Constants.PURPLE_COLOR, Constants.ANIMATED_CAN_INTERVAL_PURPLE),
        SILVER  ("silver_soda_small", "silver_soda_select",
                "textures_large/silver_soda.png", "silver_anim",
                Constants.SILVER_COLOR, Constants.ANIMATED_CAN_INTERVAL_SILVER),
        YELLOW  ("yellow_soda_small", "yellow_soda_select",
                "textures_large/yellow_soda.png", "yellow_anim",
                Constants.YELLOW_COLOR, Constants.ANIMATED_CAN_INTERVAL_YELLOW);

        private final String smallTextureName;
        private final String selectTextureName;
        private final String largeTextureName;
        private final String animTexture;
        private final Color explosionColor;
        private final float animatedCanInterval;

        PlayerType(String smallTextureName, String selectTextureName, String largeTextureName,
                   String animTexture, Color explosionColor, float animatedCanInterval) {
            this.smallTextureName = smallTextureName;
            this.selectTextureName = selectTextureName;
            this.largeTextureName = largeTextureName;
            this.animTexture = animTexture;
            this.explosionColor = explosionColor;
            this.animatedCanInterval = animatedCanInterval;
        }

        public String getSmallTextureName() {
            return smallTextureName;
        }

        public String getSelectTextureName() {
            return selectTextureName;
        }

        public String getLargeTextureName() {
            return largeTextureName;
        }

        public String getAnimTexture() {
            return animTexture;
        }

        Color getExplosionColor() {
            return explosionColor;
        }

        public float getAnimatedCanInterval() {
            return animatedCanInterval;
        }
    }

    Player(int gameHeight, TextureAtlas atlas, PlayerType playerType) {
        this.gameHeight = gameHeight;
        this.playerType = playerType;
        lastDirectionDegrees = -45 % 360;
        targetXY = new Vector2(
                Constants.GAME_WIDTH * Constants.CURSOR_START_X,
                gameHeight * Constants.CURSOR_START_Y);
        sprite = atlas.createSprite(playerType.smallTextureName);
        sprite.setCenter(targetXY.x, targetXY.y);
        sprite.setSize(sprite.getWidth(), sprite.getHeight());
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    void update(float delta) {
        move(delta);
//        Gdx.app.log("Player", "X: " + String.valueOf(sprite.getCenterX()) + " Y: " + String.valueOf(sprite.getY()));
        clamp();
    }

    void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    private void clamp() {
        if (sprite.getX() < 0) {
            sprite.setX(0);
        }
        if (sprite.getX() + sprite.getWidth() > Constants.GAME_WIDTH) {
            sprite.setX(Constants.GAME_WIDTH - sprite.getWidth());
        }
        if (sprite.getY() < 0) {
            sprite.setY(0);
        }
        if (sprite.getY() + sprite.getHeight() > gameHeight) {
            sprite.setY(gameHeight - sprite.getHeight());
        }
    }

    private void move(float delta) {
        // calculate change in X and Y positions
        float deltaX = targetXY.x - sprite.getX() - sprite.getWidth() / 2;
        float deltaY = targetXY.y - sprite.getY() - sprite.getHeight() / 2;
        float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        float velocity = Constants.PLAYER_SPEED * delta;

        if (length > velocity) {
            float movementX = deltaX / length * velocity;
            float movementY = deltaY / length * velocity;
            sprite.setX(sprite.getX() + movementX);
            sprite.setY(sprite.getY() + movementY);
        } else {
            sprite.setX(sprite.getX() + deltaX);
            sprite.setY(sprite.getY() + deltaY);
        }
    }

    void setTargetXY(int x, int y, Viewport viewport) {
        targetXY = viewport.unproject(new Vector2(x, y));
        if (Gdx.app.getType() == Application.ApplicationType.Android | Gdx.app.getType() == Application.ApplicationType.iOS) {
            targetXY.y += sprite.getHeight();
        }
    }

    public void throwCan(Array<AnimatedCan> animatedCanArray, TextureAtlas atlas) {
        switch (this.playerType) {
            case ORANGE:
                animatedCanArray.add(new AnimatedCan(this, atlas, 315, Constants.ANIMATED_CAN_SPEED));
                animatedCanArray.add(new AnimatedCan(this, atlas, 0, Constants.ANIMATED_CAN_SPEED));
                animatedCanArray.add(new AnimatedCan(this, atlas, 45, Constants.ANIMATED_CAN_SPEED));
                break;
            case PURPLE:
                for (int i = 0; i < 8; i++) {
                    animatedCanArray.add(new AnimatedCan(this, atlas, i * 45, Constants.ANIMATED_CAN_SPEED));
                }
                break;
            case YELLOW:
                float directionDegrees = (lastDirectionDegrees + 45) % 360;
                animatedCanArray.add(new AnimatedCan(this, atlas, directionDegrees, Constants.ANIMATED_CAN_SPEED));
                lastDirectionDegrees = directionDegrees;
                break;
            case BLUE:
            case SILVER:
            default:
                animatedCanArray.add(new AnimatedCan(this, atlas, 0, Constants.ANIMATED_CAN_SPEED));
                break;
        }
    }

    public float getAnimatedCanInterval() {
        return playerType.getAnimatedCanInterval();
    }

    public float getAnimationX() {
        return sprite.getX() + sprite.getWidth() * Constants.PLAYER_CENTRE_OFFSET_X;
    }

    public float getAnimationY() {
        return sprite.getY() + sprite.getHeight();
    }
}
