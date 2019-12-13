package levelGenerators.groupE;

import engine.core.MarioLevelModel;
import engine.helper.MarioTimer;
import levelGenerators.ParamMarioLevelGenerator;

import java.util.ArrayList;
import java.util.Random;

public class LevelGenerator implements ParamMarioLevelGenerator {
    private int GROUND_Y_LOCATION = 13;
    private float GROUND_PROB = 0.4f;
    private int OBSTACLES_LOCATION = 10;
    private float OBSTACLES_PROB = 0.1f;
    private int COLLECTIBLE_LOCATION = 3;
    private float COLLECTIBLE_PROB = 0.05f;
    private float ENEMY_PROB = 0.1f;
    private int FLOOR_PADDING = 3;

    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
        Random random = new Random();
        model.clearMap();
        for (int x = 0; x < model.getWidth(); x++) {
            for (int y = 0; y < model.getHeight(); y++) {
                model.setBlock(x, y, MarioLevelModel.EMPTY);
                if (y > GROUND_Y_LOCATION) {
                    if (random.nextDouble() < GROUND_PROB) {
                        model.setBlock(x, y, MarioLevelModel.GROUND);
                    }
                } else if (y > OBSTACLES_LOCATION) {
                    if (random.nextDouble() < OBSTACLES_PROB) {
                        model.setBlock(x, y, MarioLevelModel.PYRAMID_BLOCK);
                    } else if (random.nextDouble() < ENEMY_PROB) {
                        model.setBlock(x, y,
                                MarioLevelModel.getEnemyCharacters()[random.nextInt(MarioLevelModel.getEnemyCharacters().length)]);
                    }
                } else if (y > COLLECTIBLE_LOCATION) {
                    if (random.nextDouble() < COLLECTIBLE_PROB) {
                        model.setBlock(x, y,
                                MarioLevelModel.getCollectablesTiles()[random.nextInt(MarioLevelModel.getCollectablesTiles().length)]);
                    }
                }
            }
        }
//        model.setRectangle(0, 14, FLOOR_PADDING, 2, MarioLevelModel.GROUND);
        model.setRectangle(0, GROUND_Y_LOCATION, FLOOR_PADDING, 2, MarioLevelModel.GROUND);
        model.setRectangle(0, GROUND_Y_LOCATION-1, FLOOR_PADDING, 2, MarioLevelModel.GROUND);
        model.setRectangle(model.getWidth() - 1 - FLOOR_PADDING, GROUND_Y_LOCATION, FLOOR_PADDING, 2, MarioLevelModel.GROUND);
        model.setBlock(FLOOR_PADDING / 2, GROUND_Y_LOCATION-2, MarioLevelModel.MARIO_START);
        model.setBlock(model.getWidth() - 1 - FLOOR_PADDING / 2, GROUND_Y_LOCATION, MarioLevelModel.MARIO_EXIT);
        return model.getMap();
    }

    @Override
    public String getGeneratorName() {
        return "groupE_Evolutionary_Generator";
    }

    private float[] newSearchSpace(float start, float end, float interval) {
        int spaceSize = (int) Math.ceil((end - start) / interval) + 1;
        float[] searchSpace = new float[spaceSize];
        for (int i = 0; i < spaceSize; i++) {
            searchSpace[i] = Math.min(start + i * interval, end);
        }
        return searchSpace;
    }

    @Override
    public ArrayList<float[]> getParameterSearchSpace() {
        ArrayList<float[]> searchSpace = new ArrayList<>();
        float[] newSearchSpace = newSearchSpace(0.01f, 1f, 1e-4f);
        searchSpace.add(new float[]{14}); // GROUND_Y_LOCATION
        searchSpace.add(newSearchSpace); // GROUND_PROB
        searchSpace.add(new float[]{10}); // OBSTACLES_LOCATION
        searchSpace.add(newSearchSpace(0.05f, 0.2f, 2e-4f)); // OBSTACLES_PROB
        searchSpace.add(new float[]{3}); // COLLECTIBLE_LOCATION
        searchSpace.add(newSearchSpace(0.05f, 0.2f, 1e-5f)); // COLLECTIBLE_PROB
        searchSpace.add(newSearchSpace); // ENEMY_PROB
        searchSpace.add(new float[]{2}); // FLOOR_PADDING
        return searchSpace;
    }

    @Override
    public void setParameters(int[] paramIndex) {
        ArrayList<float[]> searchSpace= getParameterSearchSpace();
        GROUND_Y_LOCATION = (int) searchSpace.get(0)[paramIndex[0]];
        GROUND_PROB = searchSpace.get(1)[paramIndex[1]];
        OBSTACLES_LOCATION = (int) searchSpace.get(2)[paramIndex[2]];
        OBSTACLES_PROB = searchSpace.get(3)[paramIndex[3]];
        COLLECTIBLE_LOCATION = (int) searchSpace.get(4)[paramIndex[4]];
        COLLECTIBLE_PROB = searchSpace.get(5)[paramIndex[5]];
        ENEMY_PROB = searchSpace.get(6)[paramIndex[6]];
        FLOOR_PADDING = (int) searchSpace.get(7)[paramIndex[7]];
    }
}
