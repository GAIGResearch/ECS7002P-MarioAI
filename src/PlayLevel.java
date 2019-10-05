import engine.core.*;
import engine.helper.MarioTimer;
import levelGenerators.MarioLevelGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PlayLevel {

    public static void main(String[] args) {
        // Create a MarioGame instance
        MarioGame game = new MarioGame();

        // Grab a level from file, found in directory "levels/"
        String level = getLevel("levels/original/lvl-1.txt");

        // Or... generate a level.
//        level = generateLevel();

        // Display the entire level.
//        game.buildWorld(level, 1);

        // Play the level, either as a human ...
        MarioResult result = game.playGame(level, 200, 0);

        // ... Or with an AI agent
//        MarioResult result = game.runGame(new agents.robinBaumgarten.Agent(), level, 20, 0, true);

        // Print the results of the game
//        System.out.println(result.getGameStatus().toString());
//	    printDetailedResult(result);
    }

    private static String getLevel(String filepath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException ignored) {
        }
        return content;
    }

    private static String generateLevel() {
        MarioLevelGenerator generator = new levelGenerators.notch.LevelGenerator();
        MarioLevelModel levelModel = new MarioLevelModel(150, 16);
        MarioTimer timer = new MarioTimer(5 * 60 * 60 * 1000);
        return generator.getGeneratedLevel(levelModel, timer);
    }

    private static void printDetailedResult(MarioResult result) {
        System.out.println("Game Status: " + result.getGameStatus().toString() +
                "\nPercentage Completion: " + result.getCompletionPercentage());
        System.out.println("Lives: " + result.getCurrentLives() + "\nCoins: " + result.getCurrentCoins() +
                "\nRemaining Time: " + (int) Math.ceil(result.getRemainingTime() / 1000f));
        System.out.println("Mario State: " + result.getMarioMode() +
                "\nMushrooms: " + result.getNumCollectedMushrooms() +
                "\nFire Flowers: " + result.getNumCollectedFireflower());
        System.out.println("Total Kills: " + result.getKillsTotal() + "\nStomp Kills: " + result.getKillsByStomp() +
                "\nFireball Kills: " + result.getKillsByFire() + "\nShell Kills: " + result.getKillsByShell() +
                "\nFall Kills: " + result.getKillsByFall());
        System.out.println("Bricks: " + result.getNumDestroyedBricks() + "\nJumps: " + result.getNumJumps() +
                "\nMax X Jump: " + result.getMaxXJump() + "\nMax Air Time: " + result.getMaxJumpAirTime());
        System.out.println();
    }
}
