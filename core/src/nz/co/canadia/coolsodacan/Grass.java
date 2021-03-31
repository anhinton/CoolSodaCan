package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;

import java.util.Comparator;

@SuppressWarnings("NullableProblems")
public class Grass implements GameObject, Pool.Poolable, Comparable<GameObject>, Comparator<GameObject> {
    private Sprite sprite;
    private GrassType grassType;
    private final TextureAtlas atlas;

    enum GrassType {
        GRASS01 ("grass01"),
        GRASS02 ("grass02"),
        GRASS03 ("grass03"),
        GRASS04 ("grass04");

        private final String texture;

        GrassType (String texture) {
            this.texture = texture;
        }
    }

    Grass(TextureAtlas atlas) {
        this.atlas = atlas;
        grassType = GrassType.values()[MathUtils.random(GrassType.values().length - 1)];
        sprite = atlas.createSprite(grassType.texture);
        sprite.setFlip(MathUtils.randomBoolean(), false);
    }

    public void init(int y) {
        sprite.setCenterX(MathUtils.random(0, Constants.GAME_WIDTH));
        sprite.setY(y);
    }

    @Override
    public void reset() {
        grassType = GrassType.values()[MathUtils.random(GrassType.values().length - 1)];
        sprite = atlas.createSprite(grassType.texture);
        sprite.setFlip(MathUtils.randomBoolean(), false);
    }

    @Override
    public void update(float delta) {
        sprite.setY(sprite.getY() - Constants.WORLD_MOVEMENT_SPEED * delta);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public float getY() {
        return sprite.getY();
    }

    @Override
    public float getTopY() {
         return getY() + sprite.getHeight();
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
    public String getType() {
        return "grass";
    }
}
