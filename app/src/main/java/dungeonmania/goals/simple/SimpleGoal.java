package dungeonmania.goals.simple;

import dungeonmania.Game;
import dungeonmania.goals.Goal;

public abstract class SimpleGoal implements Goal {

    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;

        return isAchieved(game);
    }

    @Override
    public String toString(Game game) {
        if (achieved(game)) {
            return "";
        }

        return toStringHelper(game);
    }

    public abstract boolean isAchieved(Game game);
    public abstract String toStringHelper(Game game);

}
