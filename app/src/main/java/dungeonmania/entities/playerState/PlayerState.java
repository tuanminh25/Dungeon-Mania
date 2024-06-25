package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.potions.Potion;

public abstract class PlayerState {
    private Player player;

    PlayerState(Player player) {
        this.player = player;
    }

    public void transition(Potion potion) {
        player.changeState(potion.nextState(player));
    }

    public abstract BattleStatistics applyBuff(BattleStatistics origin);
}
