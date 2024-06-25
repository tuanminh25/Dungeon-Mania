package dungeonmania.entities.collectables.bombState;


import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;

public class SpawnState extends BombState {

    @Override
    public void onOverlap(GameMap map, Entity entity, Bomb bomb) {
        if (entity instanceof Player) {
            if (!((Player) entity).pickUp(bomb))
                return;
            bomb.changeState(new InventoryState());
            map.destroyEntity(bomb);
        }
    }

    @Override
    public String toString() {
        return "SPAWNED";
    }

}
