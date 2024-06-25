package dungeonmania.entities.playerManager;

import dungeonmania.entities.Entity;
import dungeonmania.entities.inventory.Inventory;

public interface PickupStrategy {
    public boolean pickUp(Inventory inventory, Entity item);
}
