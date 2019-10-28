package levelGenerators;

import engine.core.MarioLevelModel;
import engine.helper.MarioTimer;

import java.util.ArrayList;

public interface ParamMarioLevelGenerator extends MarioLevelGenerator {
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
