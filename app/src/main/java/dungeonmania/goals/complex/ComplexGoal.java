package dungeonmania.goals.complex;

import dungeonmania.Game;
import dungeonmania.goals.Goal;
import dungeonmania.goals.simple.SimpleGoal;

public abstract class ComplexGoal extends SimpleGoal {
    private Goal goal1;
    private Goal goal2;

    public ComplexGoal(Goal goal1, Goal goal2) {
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    public boolean goal1Achieved(Game game) {
        return goal1.achieved(game);
    }

    public boolean goal2Achieved(Game game) {
        return goal2.achieved(game);
    }

    public String goal1ToString(Game game) {
        return goal1.toString(game);
    }

    public String goal2ToString(Game game) {
        return goal2.toString(game);
    }
}
