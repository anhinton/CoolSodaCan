package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class AnimatedCan {

    private final Animation<TextureRegion> animation;
    private float timeElapsed;
    private AnimatedCanState canState;
    private TextureRegion currentFrame;
    private final float x;
    private float y;

    private enum AnimatedCanState { ACTIVE, INACTIVE }

    public AnimatedCan(Player player, TextureAtlas atlas) {
        timeElapsed = 0;
        canState = AnimatedCanState.ACTIVE;
        String animationName = player.getPlayerType().getAnimTexture();
        animation = new Animation<TextureRegion>(
                Constants.CAN_FRAME_DURATION,
                atlas.findRegions(animationName),
                Animation.PlayMode.LOOP);
        currentFrame = animation.getKeyFrames()[0];
        x = player.getAnimationX() - currentFrame.getRegionWidth() / 2f;
        y = player.getAnimationY();
    }

    void update(float delta) {
        timeElapsed += delta;
        if (isActive()) {
            y += Constants.ANIMATED_CAN_SPEED * delta;
        } else {
            y -= Constants.WORLD_MOVEMENT_SPEED * delta;
        }
        currentFrame = animation.getKeyFrame(timeElapsed, true);
    }

    void draw(SpriteBatch batch) {
        if (isActive()) {
            batch.draw(currentFrame, x, y);
        }
    }

    public float getCenterX() {
        return x + currentFrame.getRegionWidth() / 2f;
    }

    public float getCenterY() {
        return y + currentFrame.getRegionHeight() / 2f;
    }

    public float getY() {
        return y;
    }

    public Rectangle getHitBox() {
        return new Rectangle(x, y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    public void hit() {
        canState = AnimatedCanState.INACTIVE;
    }

    public boolean isActive() {
        return canState == AnimatedCanState.ACTIVE;
    }
}
