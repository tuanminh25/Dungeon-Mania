package dungeonmania.entities.logical;

import dungeonmania.map.GameMap;

public interface LogicalOperation {
    public boolean isActivated();
    public void activate();
    public void deactivate();
    public void notifyLogicalActivate(GameMap map);
    public void notifyLogicalDeactivate(GameMap map);
    public boolean canBeActivated(GameMap map);
}
