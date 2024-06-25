package dungeonmania.entities.collectables.bombState;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class InventoryState extends BombState {

    @Override
    public void onPutDown(GameMap map, Position p, Bomb bomb) {
        bomb.changeState(new PlacedState());

        bomb.translate(Position.calculatePositionBetween(bomb.getPosition(), p));
        map.addEntity(bomb);

        List<Position> adjPosList = bomb.getCardinallyAdjacentPositions();
        adjPosList.stream().forEach(node -> {
            List<Entity> entities = map.getEntities(node).stream().filter(e -> (e instanceof Switch))
                    .collect(Collectors.toList());
            entities.stream().map(Switch.class::cast).forEach(s -> s.subscribe(bomb, map));
        });

    }

    @Override
    public void explode(GameMap map, Position p, int radius) {
        return;
    }
}
