package levelGenerators.groupE;

import agents.MarioAgent;
import engine.core.MarioGame;
import engine.core.MarioLevelModel;
import engine.core.MarioResult;
import engine.helper.MarioStats;
import engine.helper.MarioTimer;
import engine.helper.RunUtils;
import levelGenerators.ParamMarioLevelGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static engine.helper.RunUtils.getLevel;

public class RMHC {
    private Random random;

    public RMHC() {
        random = new Random();
    }

    public int[] evolve(ParamMarioLevelGenerator generator, int nIterations) {
        ArrayList<float[]> searchSpace = generator.getParameterSearchSpace();

        // Random initialization
        int[] currentBest = getRandomPoint(searchSpace);
        generator.setParameters(currentBest);
        float bestFitness = evaluate(getLevel(null,generator), false);
//        To ensure that bestFitness is greater than 0
        while (bestFitness <= 0){
            currentBest = getRandomPoint(searchSpace);
            generator.setParameters(currentBest);
            bestFitness = evaluate(getLevel(null,generator), false);
        }
        System.out.println("Initial parameters: "+Arrays.toString(currentBest));
        System.out.println("Initial fitness: "+bestFitness);

        // Repeat for nIterations
        for (int i = 0; i < nIterations; i++) {
            // Mutate current best
            int[] candidate = mutate(currentBest, searchSpace);
            generator.setParameters(candidate);
            float candidateFitness = evaluate(getLevel(null,generator), false);

            // Keep the better solution
            if (candidateFitness > 0 && candidateFitness < bestFitness) {
                currentBest = candidate;
                bestFitness = candidateFitness;
            }
        }

        System.out.println("Final parameters: " + Arrays.toString(currentBest));
        System.out.println("Final fitness: " + bestFitness);
        return currentBest;
    }

    private int[] getRandomPoint(ArrayList<float[]> searchSpace) {
        int nParams = searchSpace.size();
        int[] solution = new int[nParams];
        for (int i = 0; i < nParams; i++) {
            int nValues = searchSpace.get(i).length;
            solution[i] = random.nextInt(nValues);
        }
        return solution;
    }

    private int[] mutate(int[] solution, ArrayList<float[]> searchSpace) {
        int[] mutated = solution.clone();
        // Mutate with probability 1/n
        float mutateProb = 1f/solution.length;
        for (int i = 0; i < solution.length; i++) {
            if (random.nextFloat() < mutateProb) {
                mutated[i] = random.nextInt(searchSpace.get(i).length);
            }
        }
        return mutated;
    }


    public float evaluate(String level, boolean showParams){
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
        if (showParams) System.out.println("Gap count: " + floorGapCount);
        //  Calculating enemy count
        int enemyCount = 0;
        for (String line : lines){
            for (char x : line.toCharArray()){
                if(x=='T' || x=='g' || x=='G' || x=='r' || x=='R' || x=='k' || x=='K' || x=='y' || x=='Y' || x=='*'){
                    enemyCount++;
                }
            }
        }
        if (showParams) System.out.println("Enemy count: " + enemyCount);

        //  Calculating coins count
        int coinsCount = 0;
        int singleCoins = 0;
        int coinBlocks = 0;
        for (String line : lines){
            for (char x : line.toCharArray()){
                if(x=='C' || x=='o'){
                    singleCoins++;
                }
                else if(x=='!' || x=='2'){
                    coinBlocks++;
                }
            }
        }
        coinsCount = singleCoins + 10 * coinBlocks;

        int idealFloorGapCount = 70;
        int idealEnemyCount = 60;
        int floorGapCountScore = Math.abs(floorGapCount - idealFloorGapCount);
        int enemyCountScore = Math.abs(enemyCount - idealEnemyCount);
        return (float) (0.2 * floorGapCountScore + 0.8 * enemyCountScore);
    }
}
