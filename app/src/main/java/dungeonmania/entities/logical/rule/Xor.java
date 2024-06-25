package dungeonmania.entities.logical.rule;

import java.util.List;

import dungeonmania.entities.logical.Conductor;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Xor extends Rule {
    public static final int REQUIREMENT = 1;
    @Override
    public boolean canBeActivated(GameMap map, Position p) {
        int count = 0;
        List<Conductor> adjConductors = getAdjConductors(map, p);
        for (Conductor conductor : adjConductors) {
            if (conductor.isActivated()) {
                count++;
            }
        }

        return count == REQUIREMENT;
    }
}
