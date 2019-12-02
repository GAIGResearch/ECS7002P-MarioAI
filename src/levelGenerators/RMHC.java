package levelGenerators;

import agents.MarioAgent;
import engine.core.MarioGame;
import engine.core.MarioResult;
import engine.helper.MarioStats;
import engine.helper.RunUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RMHC {
    private Random random;

    public RMHC() {
        random = new Random();
    }

    public void evolve(ParamMarioLevelGenerator generator, int nIterations) {
        ArrayList<float[]> searchSpace = generator.getParameterSearchSpace();

        // Random initialization
        int[] currentBest = getRandomPoint(searchSpace);
        float bestFitness = evaluate(currentBest, generator);

        // Repeat for nIterations
        for (int i = 0; i < nIterations; i++) {
            // Mutate current best
            int[] candidate = mutate(currentBest, searchSpace);
            float candidateFitness = evaluate(candidate, generator);

            // Keep the better solution
            if (candidateFitness > bestFitness) {
                currentBest = candidate;
                bestFitness = candidateFitness;
            }
        }

        System.out.println(Arrays.toString(currentBest));
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

    private float evaluate(int[] solution, ParamMarioLevelGenerator generator) {
        int noRepsPlay = 5;
        int noLevelsGen = 1;
        MarioAgent agent = new agents.robinBaumgarten.Agent();

        generator.setParameters(solution);
        MarioGame game = new MarioGame();

        MarioStats stats = new MarioStats();
        for (int i = 0; i < noLevelsGen; i++) {
            String level = RunUtils.generateLevel(generator);

            for (int j = 0; j < noRepsPlay; j++) {
                MarioResult result = game.runGame(agent, level, 20, 0, false);
                stats = stats.merge(RunUtils.resultToStats(result));
            }
        }

        return stats.winRate;
    }
}
