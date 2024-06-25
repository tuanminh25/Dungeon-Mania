package dungeonmania.entities.logical;

import dungeonmania.entities.logical.rule.Rule;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class RuleLogical extends LogicalEntity {
    private Rule rule;

    public RuleLogical(Position position, Rule rule) {
        super(position);
        this.rule = rule;
    }

    @Override
    public void notifyLogicalActivate(GameMap map) {
        ruleNotify(map);
    }

    @Override
    public void notifyLogicalDeactivate(GameMap map) {
        ruleNotify(map);
    }

    @Override
    public boolean canBeActivated(GameMap map) {
        return rule.canBeActivated(map, getPosition());
    }

    private void ruleNotify(GameMap map) {
        if (canBeActivated(map)) {
            activate();
        } else {
            deactivate();
        }
    }
}
