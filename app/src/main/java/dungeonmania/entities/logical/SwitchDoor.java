package dungeonmania.entities.logical;

import dungeonmania.entities.Entity;
import dungeonmania.entities.logical.rule.Rule;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwitchDoor extends RuleLogical {

    public SwitchDoor(Position position, Rule rule) {
        super(position, rule);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return isActivated();
    }
}
