package dungeonmania.entities.logical.rule;

import java.util.List;

import dungeonmania.entities.logical.Conductor;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class CoAnd extends Rule {
    private int sameTick = -1;
    public static final int REQUIREMENT = 2;

    @Override
    public boolean canBeActivated(GameMap map, Position p) {
        sameTick = -1;
        int count = 0;
        List<Conductor> adjConductors = getAdjConductors(map, p);
        for (Conductor conductor : adjConductors) {
            if (conductor.isActivated()) {
                int tick = conductor.getActivatedTick();
                if (sameTick == -1) {
                    sameTick = tick;
                } else if (!isSameTick(tick)) {
                    return false;
                }
                count++;
            }
        }

        return count >= REQUIREMENT;
    }

    private boolean isSameTick(int tick) {
        return sameTick == tick;
    }
}
