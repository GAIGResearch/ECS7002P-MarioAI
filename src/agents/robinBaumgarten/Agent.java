package agents.robinBaumgarten;

import agents.MarioAgent;
import engine.core.MarioForwardModel;
import engine.helper.MarioTimer;
import engine.helper.MarioActions;

/**
 * @author RobinBaumgarten
 */
public class Agent implements MarioAgent {
    private boolean action[];
    private AStarTree tree;

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        this.action = new boolean[MarioActions.numberOfActions()];
        this.tree = new AStarTree();
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        action = this.tree.optimise(model, timer);
        return action;
    }

    @Override
    public String getAgentName() {
        return "RobinBaumgartenAgent";
    }

}
