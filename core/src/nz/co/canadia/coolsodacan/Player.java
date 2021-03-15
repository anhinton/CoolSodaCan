package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The player object
 */

class Player {
    private final Sprite sprite;
    private final int gameHeight;
    private final PlayerType playerType;
    private Vector2 targetXY;

    public enum PlayerType {
        BLUE    ("blue_soda_small",     "blue_anim",    Constants.BLUE_COLOR),
        ORANGE  ("orange_soda_small",   "orange_anim",  Constants.ORANGE_COLOR),
        PURPLE  ("purple_soda_small",   "purple_anim",  Constants.PURPLE_COLOR),
        SILVER  ("silver_soda_small",   "silver_anim",  Constants.SILVER_COLOR),
        YELLOW  ("yellow_soda_small",   "yellow_anim",  Constants.YELLOW_COLOR);

        private final String textureName;
        private final String animTexture;
        private final Color explosionColor;

        PlayerType(String textureName, String animTexture, Color explosionColor) {
            this.textureName = textureName;
            this.animTexture = animTexture;
            this.explosionColor = explosionColor;
        }

        public String getTextureName() {
            return textureName;
        }

        String getAnimTexture() {
            return animTexture;
        }

        Color getExplosionColor() {
            return explosionColor;
        }
    }

    Player(int gameHeight, TextureAtlas atlas, PlayerType playerType) {
        this.gameHeight = gameHeight;
        this.playerType = playerType;
        targetXY = new Vector2(
                Constants.GAME_WIDTH * Constants.CURSOR_START_X,
                gameHeight * Constants.CURSOR_START_Y);
        sprite = atlas.createSprite(playerType.textureName);
        sprite.setCenter(targetXY.x, targetXY.y);
        sprite.setSize(sprite.getWidth(), sprite.getHeight());
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    void update(float delta) {
        move(delta);
//        Gdx.app.log("Player", "X: " + String.valueOf(sprite.getX()) + " Y: " + String.valueOf(sprite.getY()));
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

    public float getAnimationX() {
        return sprite.getX() + sprite.getWidth() * Constants.PLAY_CENTRE_OFFSET;
    }

    public float getAnimationY() {
        return sprite.getY() + sprite.getHeight();
    }
}
