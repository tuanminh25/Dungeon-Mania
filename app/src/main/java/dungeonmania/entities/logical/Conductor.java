package dungeonmania.entities.logical;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Conductor extends LogicalEntity {
    private List<LogicalOperation> logicals = new ArrayList<>();
    private int activatedTick;
    private Position src;

    public Conductor(Position position) {
        super(position);
    }

    public void subscribeLogical(LogicalOperation logical) {
        logicals.add(logical);
    }

    @Override
    public void notifyLogicalActivate(GameMap map) {
        updateLogicalSubsActivate(map);
        activate();
        activatedTick = map.getTick();
        for (LogicalOperation logical : logicals) {
            if (logical instanceof Conductor) {
                Conductor conductor = (Conductor) logical;
                conductor.setSource(getPosition());
            }
            logical.notifyLogicalActivate(map);
        }
    }

    @Override
    public void notifyLogicalDeactivate(GameMap map) {
        updateLogicalSubsDeactivate(map);
        deactivate();
        for (LogicalOperation logical : logicals) {
            if (logical instanceof Conductor) {
                Conductor conductor = (Conductor) logical;
                conductor.setSource(null);
            }
            logical.notifyLogicalDeactivate(map);
        }
    }

    public void updateLogicalSubsActivate(GameMap map) {
        logicals = new ArrayList<>();
        List<LogicalOperation> surroundings = getAdjLogicals(map);
        for (LogicalOperation logical : surroundings) {
            if (canSubscribeToActivate(logical)) {
                subscribeLogical(logical);
            }
        }
    }

    public void updateLogicalSubsDeactivate(GameMap map) {
        logicals = new ArrayList<>();
        List<LogicalOperation> surroundings = getAdjLogicals(map);
        for (LogicalOperation logical : surroundings) {
            if (canSubscribeToDeactivate(logical)) {
                subscribeLogical(logical);
            }
        }
    }

    //if the wire lose connection to a switch
    @Override
    public boolean canBeActivated(GameMap map) {
        Conductor c = map.getConductor(src);
        return c != null;
    }

    public int getActivatedTick() {
        return activatedTick;
    }

    private boolean canSubscribeToActivate(LogicalOperation logical) {
        return (!logical.isActivated() && !(logical instanceof Switch)) || logical instanceof RuleLogical;
    }

    private boolean canSubscribeToDeactivate(LogicalOperation logical) {
        return logical.isActivated() && !(logical instanceof Switch);
    }

    private List<LogicalOperation> getAdjLogicals(GameMap map) {
        List<LogicalOperation> adjLogicals = new ArrayList<>();
        List<Position> cardinallyAdj = getCardinallyAdjacentPositions();
        for (Position adj : cardinallyAdj) {
            LogicalOperation logical = map.getLogical(adj);
            if (logical != null) {
                adjLogicals.add(logical);
            }
        }
        return adjLogicals;
    }

    public void setSource(Position p) {
        src = p;
    }
}
