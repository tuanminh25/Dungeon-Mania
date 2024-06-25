package dungeonmania.goals.simple;

import dungeonmania.Game;

public class TreasureGoal extends SimpleGoal {
    private int target;

    public TreasureGoal(int target) {
        this.target = target;
    }

    @Override
    public boolean isAchieved(Game game) {
        return game.getCollectedTreasureCount() >= target;
    }

    @Override
    public String toStringHelper(Game game) {
        return ":treasure";
    }

}
