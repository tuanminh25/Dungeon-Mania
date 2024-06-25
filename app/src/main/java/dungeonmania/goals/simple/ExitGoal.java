package dungeonmania.goals.simple;

import java.util.List;


import dungeonmania.Game;
import dungeonmania.util.Position;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Exit;
import dungeonmania.entities.Player;

public class ExitGoal extends SimpleGoal {

    @Override
    public boolean isAchieved(Game game) {
        Player character = game.getPlayer();
        Position pos = character.getPosition();
        List<Exit> es = game.getExits();
        if (es == null || es.size() == 0)
            return false;
        return es.stream().map(Entity::getPosition).anyMatch(pos::equals);
    }

    @Override
    public String toStringHelper(Game game) {
        return ":exit";
    }

}
