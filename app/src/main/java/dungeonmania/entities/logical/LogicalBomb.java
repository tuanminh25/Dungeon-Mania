package dungeonmania.entities.logical;

import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.logical.rule.Rule;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class LogicalBomb extends Bomb implements LogicalOperation {
    private boolean activated = false;
    private Rule rule;

    public LogicalBomb(Position position, int radius, Rule rule) {
        super(position, radius);
        this.rule = rule;
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    @Override
    public void activate() {
        activated = true;
    }

    @Override
    public void deactivate() {
        activated = false;
    }

    @Override
    public void notifyLogicalActivate(GameMap map) {
        if (canBeActivated(map)) {
            explode(map);
        } else {
            deactivate();
        }
    }

    @Override
    public void explode(GameMap map) {
        if (canBeActivated(map)) {
            super.explode(map);
        }
    }

    @Override
    public void notifyLogicalDeactivate(GameMap map) {
        deactivate();
    }

    @Override
    public boolean canBeActivated(GameMap map) {
        return rule.canBeActivated(map, getPosition());
    }

    @Override
    public void onPutDown(GameMap map, Position p) {
        super.onPutDown(map, p);
        if (canBeActivated(map)) {
            explode(map);
        }
    }

}
