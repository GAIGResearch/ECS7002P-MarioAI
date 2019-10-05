package agents.doNothing;

import agents.MarioAgent;
import engine.core.MarioForwardModel;
import engine.helper.MarioTimer;
import engine.helper.MarioActions;

public class Agent implements MarioAgent {
    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {

    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        return new boolean[MarioActions.numberOfActions()];
    }

    @Override
    public String getAgentName() {
        return "DoNothingAgent";
    }
}
