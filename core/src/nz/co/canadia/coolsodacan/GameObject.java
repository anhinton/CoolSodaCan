package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameObject{
    void update(float delta);

    void draw(SpriteBatch batch);

    float getY();

    float getTopY();

    int compare(GameObject o1, GameObject o2);

    int compareTo(GameObject o);

    String getType();
}
