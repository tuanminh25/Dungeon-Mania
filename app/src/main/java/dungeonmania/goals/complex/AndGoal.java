package dungeonmania.goals.complex;

import dungeonmania.Game;
import dungeonmania.goals.Goal;

public class AndGoal extends ComplexGoal {

    public AndGoal(Goal goal1, Goal goal2) {
        super(goal1, goal2);
    }

    @Override
    public boolean isAchieved(Game game) {
        return goal1Achieved(game) && goal2Achieved(game);
    }

    @Override
    public String toStringHelper(Game game) {
        return "(" + goal1ToString(game) + " AND " + goal2ToString(game) + ")";
    }

}
