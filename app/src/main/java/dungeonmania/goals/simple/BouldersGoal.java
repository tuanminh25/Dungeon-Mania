package dungeonmania.goals.simple;

import dungeonmania.Game;

public class BouldersGoal extends SimpleGoal {

    @Override
    public boolean isAchieved(Game game) {
        return game.getSwitches().stream().allMatch(s -> s.isActivated());
    }

    @Override
    public String toStringHelper(Game game) {
        return ":boulders";
    }

}
