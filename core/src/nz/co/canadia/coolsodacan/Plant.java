package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Comparator;

@SuppressWarnings("NullableProblems")
public class Plant implements GameObject, Hittable, Comparable<GameObject>, Comparator<GameObject> {
    private final Sprite hitSprite;
    private final PlantType plantType;
    private Sprite currentSprite;
    private final ParticleEffect explosion;
    private Hittable.State hitState;
    private int hitCount;

    private enum PlantType {
        FERN01      ("fern01",  "fern01_hit"),
        FLOWER01    ("flower01","flower01_hit"),
        TREE01      ("tree01",  "tree01_hit"),
        TREE02      ("tree02",  "tree02_hit");

        private final String normalTexture;
        private final String hitTexture;

        PlantType(String normalTexture, String hitTexture) {
            this.normalTexture = normalTexture;
            this.hitTexture = hitTexture;
        }
    }

    Plant(int y, TextureAtlas atlas) {
        hitCount = 0;
        hitState = State.NORMAL;

        // Give us a random set of PlantTextures
        plantType = PlantType.values()[MathUtils.random(PlantType.values().length - 1)];
        Sprite normalSprite = atlas.createSprite(plantType.normalTexture);
        hitSprite = atlas.createSprite(plantType.hitTexture);

        boolean flipSprite = MathUtils.randomBoolean();
        normalSprite.flip(flipSprite, false);
        hitSprite.flip(flipSprite, false);

        currentSprite = normalSprite;
        currentSprite.setCenterX(MathUtils.random(0, Constants.GAME_WIDTH));
        currentSprite.setY(y);

        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("particleEffects/explosion.p"), atlas);
        // Set explosion dimensions to sprite size
        explosion.getEmitters().first().getSpawnWidth().setHigh(currentSprite.getWidth());
        explosion.getEmitters().first().getSpawnHeight().setHigh(currentSprite.getHeight());
        // Increase scale of particle to match sprite (THIS IS SO BAD)
        explosion.getEmitters().first().getXScale().setHigh(
                Math.min(currentSprite.getWidth(), currentSprite.getHeight()) * Constants.PLANT_PARTICLE_SCALE);
    }

    @Override
    public void update(float delta) {
        currentSprite.setY(currentSprite.getY() - Constants.WORLD_MOVEMENT_SPEED * delta);
        explosion.setPosition(currentSprite.getX() + currentSprite.getWidth() / 2, currentSprite.getY() + currentSprite.getHeight() / 2);
        explosion.update(delta);
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
    public Vector2 getCenter() {
        return new Vector2(currentSprite.getX() + currentSprite.getWidth() / 2,
                currentSprite.getY() + currentSprite.getHeight() / 2);
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
        hitState = State.HIT;
        if (hitCount >= 3) {
            hitSprite.setPosition(currentSprite.getX(), currentSprite.getY());
            currentSprite = hitSprite;
            hitState = State.SUPER_HIT;
            explosion.start();
        }
    }

    @Override
    public boolean isHittable() {
        return hitState == State.NORMAL | hitState == State.HIT;
    }

    @Override
    public int getSodasDrunk() {
        return Constants.PLANT_SODAS_DRUNK;
    }

    @Override
    public int getPoints() {
        int score;
        switch (hitState) {
            case NORMAL:
            case HIT:
            default:
                score = Constants.PLANT_BASE_POINTS;
                break;
            case SUPER_HIT:
                score = Constants.PLANT_HIGH_POINTS;
                break;
        }
        return score;
    }

    @Override
    public String getType() {
        return plantType.name();
    }
}
