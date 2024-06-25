package dungeonmania.entities.logical.rule;

import java.util.List;

import dungeonmania.entities.logical.Conductor;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Or extends Rule {

    @Override
    public boolean canBeActivated(GameMap map, Position p) {
        List<Conductor> adjConductors = getAdjConductors(map, p);
        for (Conductor conductor : adjConductors) {
            if (conductor.isActivated()) {
                return true;
            }
        }
        return false;
    }
}
