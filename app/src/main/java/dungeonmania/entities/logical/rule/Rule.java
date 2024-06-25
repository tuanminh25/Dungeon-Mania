package dungeonmania.entities.logical.rule;

import java.util.List;
import java.util.ArrayList;

import dungeonmania.entities.logical.Conductor;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Rule {
    public abstract boolean canBeActivated(GameMap map, Position p);

    public List<Conductor> getAdjConductors(GameMap map, Position p) {
        List<Conductor> conductors = new ArrayList<>();
        List<Position> cardinallyAdj = p.getCardinallyAdjacentPositions();
        for (Position adj : cardinallyAdj) {
            Conductor logical = map.getConductor(adj);
            if (logical != null) {
                conductors.add(logical);
            }
        }

        return conductors;
    }
}
