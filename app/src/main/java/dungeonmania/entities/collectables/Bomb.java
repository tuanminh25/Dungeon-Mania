package dungeonmania.entities.collectables;

import dungeonmania.util.Position;



import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.bombState.BombState;
import dungeonmania.entities.collectables.bombState.SpawnState;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;

public class Bomb extends Entity implements InventoryItem {

    public static final int DEFAULT_RADIUS = 1;
    private BombState state;
    private int radius;

    public Bomb(Position position, int radius) {
        super(position);
        this.radius = radius;
        state = new SpawnState();
    }

    public void notify(GameMap map) {
        explode(map);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        state.onOverlap(map, entity, this);
    }

    public void onPutDown(GameMap map, Position p) {
        state.onPutDown(map, p, this);

    }

    public void explode(GameMap map) {
        state.explode(map, getPosition(), radius);
    }

    public void changeState(BombState state) {
        this.state = state;
    }
}
