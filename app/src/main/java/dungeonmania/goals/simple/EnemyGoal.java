package dungeonmania.goals.simple;

import dungeonmania.Game;

public class EnemyGoal extends SimpleGoal {
    private int target;

    public EnemyGoal(int target) {
        this.target = target;
    }

    @Override
    public boolean isAchieved(Game game) {
        return game.getPlayerEnemyCount() >= target && game.isSpawnerActive();
    }

    @Override
    public String toStringHelper(Game game) {
        return ":enemies";
    }
}
