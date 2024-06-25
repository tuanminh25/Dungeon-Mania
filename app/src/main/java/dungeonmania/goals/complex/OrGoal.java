package dungeonmania.goals.complex;

import dungeonmania.Game;
import dungeonmania.goals.Goal;

public class OrGoal extends ComplexGoal {
    public OrGoal(Goal goal1, Goal goal2) {
        super(goal1, goal2);
    }

    @Override
    public boolean isAchieved(Game game) {
        return goal1Achieved(game) || goal2Achieved(game);
    }

    @Override
    public String toStringHelper(Game game) {
        return "(" + goal1ToString(game) + " OR " + goal2ToString(game) + ")";
    }
}
