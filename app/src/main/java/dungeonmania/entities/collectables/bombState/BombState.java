package dungeonmania.entities.collectables.bombState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.logical.Conductor;
import dungeonmania.entities.logical.RuleLogical;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class BombState {
    public void onOverlap(GameMap map, Entity entity, Bomb bomb) {
        return;
    }

    public void onPutDown(GameMap map, Position p, Bomb bomb) {
        bomb.changeState(new PlacedState());
    }

    public void explode(GameMap map, Position p, int radius) {
        int x = p.getX();
        int y = p.getY();
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                List<Entity> entities = map.getEntities(new Position(i, j));
                entities = entities.stream().filter(e -> !(e instanceof Player)).collect(Collectors.toList());
                for (Entity e : entities)
                    map.destroyEntity(e);
            }
        }

        checkConnection(map, p, radius);
        updateLogicals(map);
    }

    private List<Position> rightOutsideZone(Position p, int radius) {
        List<Position> outside = new ArrayList<>();
        int x = p.getX();
        int y = p.getY();

        //top left to top right
        for (int i = x - radius; i <= x + radius; i++) {
            int k = y + radius;
            outside.add(new Position(i, k));
        }

        //bot left to bot right
        for (int i = x - radius; i <= x + radius; i++) {
            int k = y - radius;
            outside.add(new Position(i, k));
        }

        //top right to bot right
        for (int i = y + radius; i >= y - radius; i--) {
            int k = x + radius;
            outside.add(new Position(k, i));
        }

        //top left to bot left
        for (int i = y + radius; i >= y - radius; i--) {
            int k = x - radius;
            outside.add(new Position(k, i));
        }

        return outside;
    }

    private void checkConnection(GameMap map, Position p, int radius) {
        List<Position> outside = rightOutsideZone(p, radius + 1);
        for (Position position : outside) {
            Conductor conductor = map.getConductor(position);
            if (conductor != null) {
                if (!conductor.canBeActivated(map)) {
                    conductor.notifyLogicalDeactivate(map);
                }
            }
        }
    }

    private void updateLogicals(GameMap map) {
        List<RuleLogical> logicals = map.getEntities(RuleLogical.class);
        for (RuleLogical logical : logicals) {
            logical.notifyLogicalActivate(map);
        }
    }
}
