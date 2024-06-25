package dungeonmania.entities.logical;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class LogicalEntity extends Entity implements LogicalOperation {
    private boolean activated = false;

    public LogicalEntity(Position position) {
        super(position);
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

    public abstract void notifyLogicalActivate(GameMap map);
    public abstract void notifyLogicalDeactivate(GameMap map);

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }
}
