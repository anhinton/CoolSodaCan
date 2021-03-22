package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Hittable {

    enum State { NORMAL, HIT, SUPER_HIT }

    Vector2 getCenter();

    Rectangle getHitBox();

    State getHitState();

    void hit();

    boolean isHittable();

    int getSodasDrunk();

    int getPoints();

    String getType();
}
