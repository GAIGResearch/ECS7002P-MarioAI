package levelGenerators;

import engine.core.MarioLevelModel;
import engine.helper.MarioTimer;

import java.util.ArrayList;

public interface ParamMarioLevelGenerator extends MarioLevelGenerator {
    /**
     * Generate a playable mario level.
     * @param model contain a model of the level.
     * @param timer time limit to complete generation.
     *
     * @return String of generated level.
     */
    String getGeneratedLevel(MarioLevelModel model, MarioTimer timer);

    /**
     * @return the name of the level generator
     */
    String getGeneratorName();

    /**
     * @return the parameter search space for this generator. Each entry in the list represents values for a parameter,
     * as a float array containing the possible values the paramter at that index can take.
     */
    ArrayList<float[]> getParameterSearchSpace();

    /**
     * Sets the generator parameters given a list of indices; each index is in the range specified in the previous
     * methods and thus corresponds to a value, which the generator must interpret accordingly.
     * @param paramIndex - list of parameter indices.
     */
    void setParameters(int[] paramIndex);
}
