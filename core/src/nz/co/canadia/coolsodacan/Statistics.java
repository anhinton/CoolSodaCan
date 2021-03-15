package nz.co.canadia.coolsodacan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Statistics {
    private final Preferences statistics;
    private int highScore;
    private float longestSession;
    private int totalCansDelivered;
    private int totalCansThrown;
    private int totalPointsScored;
    private float totalTimePlayed;

    private int guineapigsSuperhit;
    private int horsesSuperhit;
    private int plantsSuperHit;

    public Statistics() {
        statistics = Gdx.app.getPreferences(Constants.GAME_STATISTICS_PATH);
    }

    void load() {
        try {
            highScore = statistics.getInteger("highScore", 0);
        } catch (RuntimeException e) {
            Gdx.app.error("Statistics", "highScore value is invalid " + e.getLocalizedMessage());
            highScore = 0;
        }
        try {
            longestSession = statistics.getFloat("longestSession", 0);
        } catch (RuntimeException e) {
            Gdx.app.error("Statistics", "longestSession value is invalid " + e.getLocalizedMessage());
            longestSession = 0;
        }
        try {
            totalCansDelivered = statistics.getInteger("totalCansDelivered", 0);
        } catch (RuntimeException e) {
            Gdx.app.error("Statistics", "totalCansDelivered value is invalid " + e.getLocalizedMessage());
            totalCansDelivered = 0;
        }
        try {
            totalCansThrown = statistics.getInteger("totalCansThrown", 0);
        } catch (RuntimeException e) {
            Gdx.app.error("Statistics", "totalCansThrown value is invalid " + e.getLocalizedMessage());
            totalCansThrown = 0;
        }
        try {
            totalPointsScored = statistics.getInteger("totalPointsScored", 0);
        } catch (RuntimeException e) {
            Gdx.app.error("Statistics", "totalPointsScored value is invalid " + e.getLocalizedMessage());
            totalPointsScored = 0;
        }
        try {
            totalTimePlayed = statistics.getFloat("totalTimePlayed", 0);
        } catch (RuntimeException e) {
            Gdx.app.error("Statistics", "totalTimePlayed value is invalid " + e.getLocalizedMessage());
            totalTimePlayed = 0;
        }
        try {
            guineapigsSuperhit = statistics.getInteger("guineapigsSuperhit", 0);
        } catch (RuntimeException e) {
            Gdx.app.error("Statistics", "guineapigsSuperhit value is invalid " + e.getLocalizedMessage());
            guineapigsSuperhit = 0;
        }
        try {
            horsesSuperhit = statistics.getInteger("horsesSuperhit", 0);
        } catch (RuntimeException e) {
            Gdx.app.error("Statistics", "horsesSuperhit value is invalid " + e.getLocalizedMessage());
            horsesSuperhit = 0;
        }
        try {
            plantsSuperHit = statistics.getInteger("plantsSuperHit", 0);
        } catch (RuntimeException e) {
            Gdx.app.error("Statistics", "plantsSuperHit value is invalid " + e.getLocalizedMessage());
            plantsSuperHit = 0;
        }
    }

    public void clear() { statistics.clear(); }

    public void save() {
        statistics.flush();
    }

    public int getHighScore() {
        return highScore;
    }

    public float getLongestSession() {
        return longestSession;
    }

    public int getTotalCansDelivered() {
        return totalCansDelivered;
    }

    public int getTotalCansThrown() {
        return totalCansThrown;
    }

    public int getTotalPointsScored() {
        return totalPointsScored;
    }

    public float getTotalTimePlayed() {
        return totalTimePlayed;
    }

    public int getGuineapigsSuperhit() {
        return guineapigsSuperhit;
    }

    public int getHorsesSuperhit() {
        return horsesSuperhit;
    }

    public int getAnimalsSuperhit() {
        return guineapigsSuperhit + horsesSuperhit;
    }

    public int getPlantsSuperHit() {
        return plantsSuperHit;
    }

    public void updateHighScore(int score) {
        this.highScore = Math.max(highScore, score);
        statistics.putInteger("highScore", highScore);
    }

    public void updateTotalPointsScored(int points) {
        this.totalPointsScored += points;
        statistics.putInteger("totalPointsScored", totalPointsScored);

    }

    public void updateLongestSession(float timeElapsed) {
        this.longestSession = Math.max(longestSession, timeElapsed);
        statistics.putFloat("longestSession", longestSession);
    }

    public void updateTotalTimePlayed(float delta) {
        this.totalTimePlayed += delta;
        statistics.putFloat("totalTimePlayed", totalTimePlayed);
    }

    public void updateTotalCansDelivered(int nCans) {
        this.totalCansDelivered += nCans;
        statistics.putInteger("totalCansDelivered", totalCansDelivered);
    }

    public void incrementTotalCansThrown() {
        this.totalCansThrown++;
        statistics.putInteger("totalCansThrown", totalCansThrown);
    }

    public void incrementSuperHit(String type) {
        try {
            switch (type) {
                case "COCO":
                    guineapigsSuperhit++;
                    statistics.putInteger("guineapigsSuperhit", guineapigsSuperhit);
                    break;
                case "HORSE01":
                case "HORSE02":
                    horsesSuperhit++;
                    statistics.putInteger("horsesSuperhit", horsesSuperhit);
                    break;
                case "FERN01":
                case "FLOWER01":
                case "TREE01":
                case "TREE02":
                    plantsSuperHit++;
                    statistics.putInteger("plantsSuperHit", plantsSuperHit);
                    break;
                default:
                    throw new RuntimeException("Unrecognised Hittable type: '" + type + "'");
            }
        } catch (RuntimeException e) {
            Gdx.app.error("Statistics", "Unable to do incrementSuperHit " + e.getLocalizedMessage());
        }
    }

    public boolean isSodaUnlocked(Player.PlayerType playerType) {
        switch(playerType) {
            case ORANGE:
                return totalPointsScored >= Constants.UNLOCK_POINTS_THRESHOLD;
            case PURPLE:
                return guineapigsSuperhit >= Constants.UNLOCK_GUINEAPIGS_THRESHOLD;
            case SILVER:
                return longestSession >= Constants.UNLOCK_SESSION_THRESHOLD;
            case YELLOW:
                return plantsSuperHit >= Constants.UNLOCK_PLANTS_THRESHOLD;
            case BLUE:
            default:
                return true;
        }
    }
}
