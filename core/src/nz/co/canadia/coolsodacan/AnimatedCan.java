package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class AnimatedCan implements Pool.Poolable {

    private final Animation<TextureRegion> animation;
    private final ParticleEffect explosion;
    private float speed;
    private float directionDegrees;
    private float timeElapsed;
    private AnimatedCanState canState;
    private TextureRegion currentFrame;
    private float x;
    private float y;

    private enum AnimatedCanState { ACTIVE, INACTIVE }

    public AnimatedCan(Player player, TextureAtlas atlas) {
        timeElapsed = 0;
        canState = AnimatedCanState.ACTIVE;
        String animationName = player.getPlayerType().getAnimTexture();
        Color particleColor = player.getPlayerType().getExplosionColor();
        animation = new Animation<TextureRegion>(
                Constants.CAN_FRAME_DURATION,
                atlas.findRegions(animationName),
                Animation.PlayMode.LOOP);
        currentFrame = animation.getKeyFrames()[0];

        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("particleEffects/explosion.p"), atlas);
        // Set tint of particle effect
        float[] tint = new float[3];
        tint[0] = particleColor.r;
        tint[1] = particleColor.g;
        tint[2] = particleColor.b;
        explosion.getEmitters().first().getTint().setColors(tint);
    }

    public void init(Player player, float directionDegrees, float speed) {
        x = player.getAnimationX() - currentFrame.getRegionWidth() / 2f;
        y = player.getAnimationY();
        this.directionDegrees = directionDegrees;
        this.speed = speed;
        explosion.setPosition(getCenterX(), getY());
    }

    @Override
    public void reset() {
        timeElapsed = 0;
        canState = AnimatedCanState.ACTIVE;
        currentFrame = animation.getKeyFrames()[0];
        explosion.reset();
    }

    void update(float delta) {
        timeElapsed += delta;
        if (isActive()) {
            float changeX = MathUtils.sinDeg(directionDegrees);
            float changeY = MathUtils.cosDeg(directionDegrees);
            x += changeX * speed * delta;
            y += changeY * speed * delta;
        } else {
            y -= Constants.WORLD_MOVEMENT_SPEED * delta;
        }
        currentFrame = animation.getKeyFrame(timeElapsed, true);
        explosion.setPosition(getCenterX(), getY());
        explosion.update(delta);
    }

    void draw(SpriteBatch batch) {
        if (isActive()) {
            batch.draw(currentFrame, x, y,
                    currentFrame.getRegionWidth() / 2f,
                    currentFrame.getRegionHeight() / 2f,
                    currentFrame.getRegionWidth(),
                    currentFrame.getRegionHeight(),
                    1, 1, -directionDegrees);
        } else {
            explosion.draw(batch);
        }
    }

    public float getCenterX() {
        return x + currentFrame.getRegionWidth() / 2f;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return currentFrame.getRegionWidth();
    }

    public float getHeight() {
        return currentFrame.getRegionHeight();
    }

    public Rectangle getHitBox() {
        return new Rectangle(x, y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    public void hit() {
        canState = AnimatedCanState.INACTIVE;
        explosion.start();
    }

    public boolean isActive() {
        return canState == AnimatedCanState.ACTIVE;
    }
}
