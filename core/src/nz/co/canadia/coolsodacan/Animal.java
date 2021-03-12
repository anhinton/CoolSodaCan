package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.Comparator;

@SuppressWarnings("NullableProblems")
public class Animal implements GameObject, Hittable, Comparable<GameObject>, Comparator<GameObject> {
    private final Sprite normalSprite;
    private final Sprite hitSprite;
    private final ParticleEffect explosion;

    private final AnimalType animalType;
    private float rot;
    private int hitCount;

    private State hitState;
    private Sprite currentSprite;

    private enum AnimalType {
        COCO    ("coco",    "coco_smile"),
        HORSE01 ("horse01", "horse01_smile"),
        HORSE02 ("horse02", "horse02_smile");

        private final String textureName;
        private final String hitTextureName;

        AnimalType(String textureName, String hitTextureName) {
            this.textureName = textureName;
            this.hitTextureName = hitTextureName;
        }
    }

    Animal(int y, TextureAtlas atlas, Color explosionColor) {
        hitCount = 0;
        hitState = State.NORMAL;

        // Give us a random set of AnimalTextures
        animalType = AnimalType.values()[MathUtils.random(AnimalType.values().length - 1)];
        normalSprite = atlas.createSprite(animalType.textureName);
        hitSprite = atlas.createSprite(animalType.hitTextureName);

        boolean flipSprite = MathUtils.randomBoolean();
        normalSprite.flip(flipSprite, false);
        hitSprite.flip(flipSprite, false);

        currentSprite = normalSprite;
        currentSprite.setCenterX(MathUtils.random(0 + normalSprite.getWidth() / 2, Constants.GAME_WIDTH - normalSprite.getWidth() / 2));
        currentSprite.setY(y);
        rot = MathUtils.random(0, 360f);
        wiggle(0);

        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("particleEffects/explosion.p"), atlas);
        // Set explosion dimensions to sprite size
        explosion.getEmitters().first().getSpawnWidth().setHigh(currentSprite.getWidth());
        explosion.getEmitters().first().getSpawnHeight().setHigh(currentSprite.getHeight());
        // Increase scale of particle to match half sprite size (not so big as Plants)
        explosion.getEmitters().first().getXScale().setHigh(
                Math.min(currentSprite.getWidth(), currentSprite.getHeight()) * Constants.ANIMAL_PARTICLE_SCALE);
        // Set explosion to can colour
        float[] tint = new float[3];
        tint[0] = explosionColor.r;
        tint[1] = explosionColor.g;
        tint[2] = explosionColor.b;
        explosion.getEmitters().first().getTint().setColors(tint);
    }

    // Wiggle!
    void wiggle(float delta) {
        rot = (rot + delta * Constants.DEGREES_PER_SECOND) % 360;
        float shake = MathUtils.sin(rot) * Constants.SHAKE_AMPLITUDE_IN_DEGREES;
        currentSprite.setRotation(shake);
    }

    @Override
    public void update(float delta) {
        if (hitState != State.SUPER_HIT) {
            wiggle(delta);
        } else {
            explosion.update(delta);
        }
        currentSprite.setY(currentSprite.getY() - Constants.WORLD_MOVEMENT_SPEED * delta);
        hitSprite.setPosition(currentSprite.getX(), currentSprite.getY());
        explosion.setPosition(currentSprite.getX() + currentSprite.getWidth() / 2, currentSprite.getY() + currentSprite.getHeight() / 2);
    }

    public void draw(SpriteBatch batch) {
        currentSprite.draw(batch);
        if (hitState == State.SUPER_HIT & !explosion.isComplete()) {
            explosion.draw(batch);
        }
    }

    @Override
    public float getY() {
        return currentSprite.getY();
    }

    @Override
    public float getTopY() {
         return getY() + currentSprite.getHeight();
    }

    @Override
    public int compare(GameObject o1, GameObject o2) {
        float diff = o1.getY() - o2.getY();
        if (diff == 0) {
            return 0;
        } else if (diff > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int compareTo(GameObject o) {
        return compare(this, o);
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(
                currentSprite.getX(),
                currentSprite.getY() + currentSprite.getHeight() / 2,
                currentSprite.getWidth(),
                currentSprite.getHeight() / 2);
    }

    @Override
    public State getHitState() {
        return hitState;
    }

    @Override
    public void hit() {
        hitCount += 1;
        if (hitCount < 3) {
            hitState = State.HIT;
            currentSprite = hitSprite;
            currentSprite.flip(true, false);
        } else if (hitCount == 3) {
            currentSprite.flip(false, true);
            hitState = State.SUPER_HIT;
            explosion.start();
        }
    }

    @Override
    public boolean isHittable() {
        return hitState == State.NORMAL | hitState == State.HIT;
    }

    @Override
    public String getType() {
        return animalType.name();
    }

    @Override
    public int getSodasDrunk() {
        return Constants.ANIMAL_SODAS_DRUNK;
    }

    @Override
    public int getPoints() {
        int score;
        switch (hitState) {
            case NORMAL:
            case HIT:
            default:
                score = Constants.ANIMAL_BASE_POINTS;
                break;
            case SUPER_HIT:
                score = Constants.ANIMAL_HIGH_POINTS;
                break;
        }
        return score;
    }
}
