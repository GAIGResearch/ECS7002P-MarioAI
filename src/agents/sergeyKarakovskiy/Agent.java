package agents.sergeyKarakovskiy;

import agents.MarioAgent;
import engine.core.MarioForwardModel;
import engine.helper.MarioTimer;
import engine.helper.MarioActions;

/**
 * @author SergeyKarakovskiy
 */
public class Agent implements MarioAgent {
    private boolean[] actions = null;

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        actions = new boolean[MarioActions.numberOfActions()];
        actions[MarioActions.RIGHT.getValue()] = true;
        actions[MarioActions.SPEED.getValue()] = true;
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        actions[MarioActions.SPEED.getValue()] = actions[MarioActions.JUMP.getValue()] = model.mayMarioJump() || !model.isMarioOnGround();
        return actions;
    }

    @Override
    public String getAgentName() {
        return "SergeyKarakovskiyAgent";
    }
}
