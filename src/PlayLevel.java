import agents.MarioAgent;
import engine.core.MarioGame;
import engine.core.MarioResult;
import levelGenerators.ParamMarioLevelGenerator;
import levelGenerators.groupE.LevelGenerator;
import levelGenerators.groupE.RMHC;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static engine.helper.RunUtils.generateLevel;
import static engine.helper.RunUtils.getLevel;

@SuppressWarnings("ConstantConditions")
public class PlayLevel {

    public double featureAnalyser(String level){
//        MarioGame game = new MarioGame();
//        game.buildWorld(level,1);
        String[] lines=level.split("\n");
        ArrayList<String> levelLines = new ArrayList<>(Arrays.asList(lines));
        //  Calculating the number of gaps in the floor
        int floorGapCount = 0;
        for (char x : levelLines.get(15).toCharArray()){
            if (x == '-'){
                floorGapCount++;
            }
        }
        //  Calculating density of enemies
        int enemyDensity = 0;
        for (String line : lines){
            for (char x : line.toCharArray()){
                if(x=='T' || x=='g' || x=='G' || x=='r' || x=='R' || x=='k' || x=='K' || x=='y' || x=='Y' || x=='*'){
                    enemyDensity++;
                }
            }
        }
        int idealFloorGapCount=50;
        int idealEnemyDensity=20;

        return (0.6*(floorGapCount-idealFloorGapCount)+0.4*(enemyDensity-idealEnemyDensity));
    }

    public static void main(String[] args) {
        // Run settings:
        boolean visuals = true;  // Set to false if no visuals required for this run.
//        boolean generateDifferentLevels = false;  // If true, each play will be a different generated level.
        boolean generateDifferentLevels = true;  // If true, each play will be a different generated level.
//        String levelFile = "levels/original/lvl-1.txt";
        String levelFile = null;  // null;
        //MarioLevelGenerator generator = new levelGenerators.notch.LevelGenerator();  // null;
        ParamMarioLevelGenerator generator = new LevelGenerator();  // null;
//        generator.
        ArrayList<float[]> searchSpace = generator.getParameterSearchSpace();
        System.out.println(searchSpace);
        generator.setParameters(new int[]{0,0,0,0,0,0,0,0});
        RMHC hillClimber = new RMHC();
        generator.setParameters(hillClimber.evolve(generator,10000));
        hillClimber.evaluate(getLevel(null, generator), true);

        // Note: either levelFile or generator must be non-null. If neither is null, levelFile takes priority.
        if (levelFile == null && generator == null) {
            return;
        }

        // Create a MarioGame instance and game-playing AI
        MarioGame game = new MarioGame();
        MarioAgent robinBaumgartenAgent = new agents.robinBaumgarten.Agent();
        MarioAgent andySloane = new agents.andySloane.Agent();
        MarioAgent glennHartmann = new agents.glennHartmann.Agent();
        MarioAgent sergeyPolikarpov = new agents.sergeyPolikarpov.Agent();
        MarioAgent sergeyKarakovskiy = new agents.sergeyKarakovskiy.Agent();
        MarioAgent spencerSchumann = new agents.spencerSchumann.Agent();
        MarioAgent trondEllingsen = new agents.trondEllingsen.Agent();

        // Grab a level from file, found in directory "levels/" or pass null to generate a level automatically.
        String level = getLevel(levelFile, generator);

        PlayLevelBackup demo = new PlayLevelBackup();


        // Display the entire level.
        game.buildWorld(level, 1);

        // Repeat the game several times, maybe.
        // for experiment
        int maxRounds = 10;      // for experiment
        int round = 1;          // for experiment
        int winCount = 0;       // for experiment
        ArrayList<Integer> jumpsCounts = new ArrayList<>();
        ArrayList<Integer> jumpsMaxHeights = new ArrayList<>();
        ArrayList<Integer> totalKills = new ArrayList<>();
        ArrayList<Integer> remainingTimes = new ArrayList<>();
        //////
        int playAgain = 0;
        while (playAgain == 0 && round++ <= maxRounds) {  // 0 - play again! 1 - end execution.

            // Play the level, either as a human ...
            //MarioResult result = game.playGame(level, 200, 0);

            // ... Or with an AI agent
            MarioResult result = game.runGame(robinBaumgartenAgent, level, 20, 0, visuals);
            System.out.println("robinBaumgartenAgent:" + result.getGameStatus().toString());
//            result = game.runGame(glennHartmann, level, 20, 0, visuals);
//            System.out.println("glennHartmann:"+result.getGameStatus().toString());
//            result = game.runGame(sergeyPolikarpov, level, 20, 0, visuals);
//            System.out.println("sergeyPolikarpov:"+result.getGameStatus().toString());
//            result = game.runGame(sergeyKarakovskiy, level, 20, 0, visuals);
//            System.out.println("sergeyKarakovskiy:"+result.getGameStatus().toString());
//            result = game.runGame(spencerSchumann, level, 20, 0, visuals);
//            System.out.println("spencerSchumann:"+result.getGameStatus().toString());
//            result = game.runGame(trondEllingsen, level, 20, 0, visuals);
//            System.out.println("trondEllingsen:"+result.getGameStatus().toString());
            winCount += result.getGameStatus().toString().equals("WIN") ? 1 : 0;
            jumpsCounts.add(result.getNumJumps());
            jumpsMaxHeights.add(result.getMaxJumpAirTime());
            totalKills.add(result.getKillsTotal());
            remainingTimes.add(result.getRemainingTime());

            // Print the results of the game
//            System.out.println(result.getGameStatus().toString());
//            System.out.println(resultToStats(result).toString());

            if (generateDifferentLevels) {
                generator.setParameters(hillClimber.evolve(generator,10000));
                level = generateLevel(generator);
                game.buildWorld(level, 1);

            }

            // Check if we should play again.
            playAgain = (game.playAgain == 0 && visuals) ? 0 : 1;  // If visuals are not on, only play 1 time
        }
        // experiment statistics
        System.out.println("Win rate: " + (double) winCount / Math.min(round, maxRounds));

        int totalJumps = 0;
        for (int jumpCount : jumpsCounts) totalJumps += jumpCount;
        System.out.println("Numbers of jumps:" + Arrays.toString(jumpsCounts.toArray()));
        System.out.println("Average number of jumps:" + (double) totalJumps / jumpsCounts.size());

        int totalJumpHeight = 0;
        for (int jumpHeight : jumpsMaxHeights) totalJumpHeight += jumpHeight;
        System.out.println("Max jump heights:" + Arrays.toString(jumpsMaxHeights.toArray()));
        System.out.println("Average max jump height:" + (double) totalJumpHeight / jumpsMaxHeights.size());

        int totalKillsCount = 0;
        for (int totalKill : totalKills) totalKillsCount += totalKill;
        System.out.println("Kills count:" + Arrays.toString(totalKills.toArray()));
        System.out.println("Average number of kills:" + (double) totalKillsCount / totalKills.size());

        int totalRemainingTimes = 0;
        for (int remainingTime : remainingTimes) totalRemainingTimes += remainingTime;
        System.out.println("Remaining times:" + Arrays.toString(remainingTimes.toArray()));
        System.out.println("Average remaining time:" + (double) totalRemainingTimes / remainingTimes.size());
    }
}
