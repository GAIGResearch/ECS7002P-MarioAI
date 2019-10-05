package engine.helper;

import engine.core.MarioLevelModel;
import engine.core.MarioResult;
import levelGenerators.MarioLevelGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Includes several methods facilitating running the framework.
 */
public class RunUtils {

    /**
     * Returns a level, given filepath OR generator.
     * @param filepath - if not null, the level at the corresponding path will be loaded.
     * @param generator - if filepath null, this will be used to generate a level instead.
     * @return the resulting level. May be an empty string if filepath specified but not found.
     */
    public static String getLevel(String filepath, MarioLevelGenerator generator) {
        if (filepath != null) {
            return retrieveLevel(filepath);
        } else {
            return generateLevel(generator);
        }
    }

    /**
     * Retrieves an already created level.
     * @param filepath - path where level file can be found.
     * @return String with level contents, each line a list of characters, separated by '\n'.
     */
    public static String retrieveLevel(String filepath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException ignored) {
        }
        return content;
    }

    /**
     * Generates a level with given generator. Level dimensions are 150x60 by default. Execution must finish in
     * maximum 5 hours.
     * @param generator - level generator.
     * @return String with level contents, each lne a list of characters, separated by '\n'.
     */
    public static String generateLevel(MarioLevelGenerator generator) {
        MarioLevelModel levelModel = new MarioLevelModel(150, 16);
        MarioTimer timer = new MarioTimer(5 * 60 * 60 * 1000);
        return generator.getGeneratedLevel(levelModel, timer);
    }

    /**
     * Turns a MarioResult object into a MarioStats object.
     * @param result - object encapsulating gameplay information and events.
     */
    public static MarioStats resultToStats(MarioResult result) {
        return new MarioStats(result.getGameStatus(), result.getCompletionPercentage(),
                result.getCurrentLives(), result.getCurrentCoins(), (int) Math.ceil(result.getRemainingTime() / 1000f),
                result.getMarioMode(), result.getNumCollectedMushrooms(), result.getNumCollectedFireflower(),
                result.getKillsTotal(), result.getKillsByStomp(), result.getKillsByFire(), result.getKillsByShell(),
                result.getKillsByFall(), result.getNumDestroyedBricks(), result.getNumJumps(), result.getMaxXJump(),
                result.getMaxJumpAirTime(), result.getNumBumpBrick(), result.getNumBumpQuestionBlock(),
                result.getMarioNumHurts());
    }
}
