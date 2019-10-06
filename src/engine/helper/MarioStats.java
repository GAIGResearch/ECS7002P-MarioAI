package engine.helper;

import java.util.ArrayList;


/**
 * Can contain multiple objects in this format, given by the number of objects in list `status'.
 */
public class MarioStats {
    public ArrayList<GameStatus> status;
    public float winRate;
    public float percentageComplete;
    public int lives;
    public int coins;
    public int remainingTime;
    public int marioState;
    public int mushroomsCollected;
    public int flowersCollected;
    public int killsTotal;
    public int stompKills;
    public int fireKills;
    public int shellKills;
    public int fallKills;
    public int bricksDestroyed;
    public int numJumps;
    public float maxXjump;
    public int maxJumpAirTime;
    public int numBumpBrick;
    public int numBumpQuestionBlock;
    public int numHurts;

    public MarioStats() {
        status = new ArrayList<>();
    }

    public MarioStats(GameStatus status, float percentage, int l, int c, int time, int state, int mushrooms,
                      int flowers, int kills, int stomp, int fire, int shell, int fall, int bricks, int jumps,
                      float maxJump, int jumpAirTime, int bumpBrick, int bumpQuestion, int hurts) {
        this.status = new ArrayList<>();
        this.status.add(status);
        this.percentageComplete = percentage;
        this.lives = l;
        this.coins = c;
        this.remainingTime = time;
        this.marioState = state;
        this.mushroomsCollected = mushrooms;
        this.flowersCollected = flowers;
        this.killsTotal = kills;
        this.stompKills = stomp;
        this.fireKills = fire;
        this.shellKills = shell;
        this.fallKills = fall;
        this.bricksDestroyed = bricks;
        this.numJumps = jumps;
        this.maxXjump = maxJump;
        this.maxJumpAirTime = jumpAirTime;
        this.numBumpBrick = bumpBrick;
        this.numBumpQuestionBlock = bumpQuestion;
        this.numHurts = hurts;
        calculateWinRate();
    }

    public MarioStats merge(MarioStats other) {
        MarioStats merged = new MarioStats();
        merged.status.addAll(this.status);
        merged.status.addAll(other.status);
        merged.percentageComplete = this.percentageComplete + other.percentageComplete;
        merged.lives = this.lives + other.lives;
        merged.coins = this.coins + other.coins;
        merged.remainingTime = this.remainingTime + other.remainingTime;
        merged.marioState = this.marioState + other.marioState;
        merged.mushroomsCollected = this.mushroomsCollected + other.mushroomsCollected;
        merged.flowersCollected = this.flowersCollected + other.flowersCollected;
        merged.killsTotal = this.killsTotal + other.killsTotal;
        merged.stompKills = this.stompKills + other.stompKills;
        merged.fireKills = this.fireKills + other.fireKills;
        merged.shellKills = this.shellKills + other.shellKills;
        merged.fallKills = this.fallKills + other.fallKills;
        merged.bricksDestroyed = this.bricksDestroyed + other.bricksDestroyed;
        merged.numJumps = this.numJumps + other.numJumps;
        merged.maxXjump = this.maxXjump + other.maxXjump;
        merged.maxJumpAirTime = this.maxJumpAirTime + other.maxJumpAirTime;
        merged.numBumpBrick = this.numBumpBrick + other.numBumpBrick;
        merged.numBumpQuestionBlock = this.numBumpQuestionBlock + other.numBumpQuestionBlock;
        merged.numHurts = this.numHurts + other.numHurts;
        calculateWinRate();
        return merged;
    }

    private void calculateWinRate() {
        float wins = 0;
        for (GameStatus st: status) {
            if (st == GameStatus.WIN) {
                wins++;
            }
        }
        winRate = wins / status.size();
    }

    @Override
    public String toString() {
        double records = status.size();
        calculateWinRate();

        return "Win Rate: " + winRate +
                "\nPercentage Completion: " + (percentageComplete/records) * 100 +
                "\nLives: " + lives/records +
                "\nCoins: " + coins/records +
                "\nRemaining Time: " + remainingTime/records +
                "\nMario State: " + marioState/records +
                "\nMushrooms: " + mushroomsCollected/records +
                "\nFire Flowers: " + flowersCollected/records +
                "\nTotal Kills: " + killsTotal/records +
                "\nStomp Kills: " + stompKills/records +
                "\nFireball Kills: " + fireKills/records +
                "\nShell Kills: " + shellKills/records +
                "\nFall Kills: " + fallKills/records +
                "\nBricks: " + bricksDestroyed/records +
                "\nJumps: " + numJumps/records +
                "\nMax X Jump: " + maxXjump/records +
                "\nMax Air Time: " + maxJumpAirTime/records +
                "\nBrick bump: " + numBumpBrick/records +
                "\nQuestion block bump: " + numBumpQuestionBlock/records +
                "\nMario hits: " + numHurts/records +
                "\n";
    }
}
