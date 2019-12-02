import agents.MarioAgent;
import engine.core.*;
import javafx.util.Pair;
import levelGenerators.MarioLevelGenerator;
import levelGenerators.ParamMarioLevelGenerator;
import levelGenerators.myRandom.LevelGenerator;

import java.util.ArrayList;

import static engine.helper.RunUtils.*;

@SuppressWarnings("ConstantConditions")
public class PlayLevel {

    public double featureAnalyser(String level){
//        MarioGame game = new MarioGame();
//        game.buildWorld(level,1);
        String[] lines=level.split("\n");
        ArrayList<String> levelLines= new ArrayList<String>();
        for (String line : lines) {
            levelLines.add(line);
        }
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
        boolean visuals = false;  // Set to false if no visuals required for this run.
//        boolean generateDifferentLevels = false;  // If true, each play will be a different generated level.
        boolean generateDifferentLevels = true;  // If true, each play will be a different generated level.
        //String levelFile = "levels/original/lvl-1.txt";;
        String levelFile = null;  // null;
        //MarioLevelGenerator generator = new levelGenerators.notch.LevelGenerator();  // null;
        ParamMarioLevelGenerator generator = new levelGenerators.myRandom.LevelGenerator();  // null;
        ArrayList<float[]> searchSpace = generator.getParameterSearchSpace();
        System.out.println(searchSpace);
        generator.setParameters(new int[]{0,0,0,0,0,0,0,0});

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

        PlayLevel demo = new PlayLevel();


        // Display the entire level.
        //game.buildWorld(level, 1);

        // Repeat the game several times, maybe.
        int playAgain = 0;
        while (playAgain == 0) {  // 0 - play again! 1 - end execution.

            // Play the level, either as a human ...
            //MarioResult result = game.playGame(level, 200, 0);

            // ... Or with an AI agent
            MarioResult result = game.runGame(robinBaumgartenAgent, level, 20, 0, visuals);
            System.out.println("robinBaumgartenAgent:"+result.getGameStatus().toString());
            result = game.runGame(glennHartmann, level, 20, 0, visuals);
            System.out.println("glennHartmann:"+result.getGameStatus().toString());
            result = game.runGame(sergeyPolikarpov, level, 20, 0, visuals);
            System.out.println("sergeyPolikarpov:"+result.getGameStatus().toString());
            result = game.runGame(sergeyKarakovskiy, level, 20, 0, visuals);
            System.out.println("sergeyKarakovskiy:"+result.getGameStatus().toString());
            result = game.runGame(spencerSchumann, level, 20, 0, visuals);
            System.out.println("spencerSchumann:"+result.getGameStatus().toString());
            result = game.runGame(trondEllingsen, level, 20, 0, visuals);
            System.out.println("trondEllingsen:"+result.getGameStatus().toString());

            // Print the results of the game
//            System.out.println(result.getGameStatus().toString());
//            System.out.println(resultToStats(result).toString());

            if (generateDifferentLevels) {
                level = generateLevel(generator);
            }

            //  Printing game level fitness
            System.out.println(demo.featureAnalyser(level));

            // Check if we should play again.
            playAgain = (game.playAgain == 0 && visuals) ? 0 : 1;  // If visuals are not on, only play 1 time
        }
    }
}
