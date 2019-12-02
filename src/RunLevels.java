import agents.MarioAgent;
import engine.core.MarioGame;
import engine.core.MarioResult;
import engine.helper.MarioStats;
import levelGenerators.MarioLevelGenerator;

import static engine.helper.RunUtils.*;

public class RunLevels {

    private static void printHelp() {
        System.out.println("RunLevels.java usage: 4+ args expected\n" +
                "\t[index = 0] number of levels\n" +
                "\t[index = 1] repetitions per level\n" +
                "\t[index = 2] using level generator (boolean; using preset levels if false)" +
                "\t[index = 3] AI agent response limit (20 default)" +
                "\t[index = 4...] preset levels (if not using generator). there should be exactly the number specified" +
                "before (index 0) of filepaths, separated by space.");
    }

    public static void main(String[] args) {
        // Run default settings:
        int noLevels = 2;
        int repsPerLevel = 50;
        boolean usingGenerator = true;
        int agentTimer = 20;
        String[] levels = new String[]{"levels/original/lvl-1.txt", "levels/original/lvl-2.txt"};

        // Check arguments for overwrite:
        if (args.length >= 4) {
            noLevels = Integer.parseInt(args[0]);
            repsPerLevel = Integer.parseInt(args[1]);
            usingGenerator = Boolean.parseBoolean(args[2]);
            agentTimer = Integer.parseInt(args[3]);

            // Optional arguments, to be set only if not using level generator:
            levels = new String[noLevels];
            if (!usingGenerator && args.length == 4 + noLevels) {
                System.arraycopy(args, 4, levels, 0, noLevels);
            } else if (!usingGenerator) {
                printHelp();
                return;
            }
        }

        // Create a MarioGame instance, AI to play the game and level generator (not necessary if `levels' used)
        MarioGame game = new MarioGame();
        MarioAgent agent = new agents.robinBaumgarten.Agent();  // TODO: agent to play the game
        //MarioAgent agent = new agents.sergeyKarakovskiy.Agent();  // TODO: agent to play the game
        MarioLevelGenerator generator = new levelGenerators.notch.LevelGenerator();  // TODO: level generator

        if (!usingGenerator) {  // Make sure the value is correct if not using level generator.
            noLevels = levels.length;
        }

        MarioStats average = new MarioStats();  // Keep average of statistics over all the runs
        for (int i = 0; i < noLevels; i++) {
            // Find level
            String level;
            if (usingGenerator) {
                level = generateLevel(generator);
            } else {
                level = retrieveLevel(levels[i]);
            }

            // Run the level several times
            for (int j = 0; j < repsPerLevel; j++) {
                MarioResult result = game.runGame(agent, level, agentTimer, 0, false);

                System.out.println((i+1) + "/" + noLevels + ";" + (j+1) + "/" + repsPerLevel + ": "
                        + result.getGameStatus().toString());
                MarioStats stats = resultToStats(result);
                average = average.merge(stats);
            }
        }

        System.out.println("------------");
        System.out.println(average.toString());
    }
}
