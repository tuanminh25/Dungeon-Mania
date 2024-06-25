package dungeonmania.entities.playerManager;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.Player;

public class DefaultPickup implements PickupStrategy {
    private Player player;

    public DefaultPickup(Player player) {
        this.player = player;
    }

    public boolean pickUp(Inventory inventory, Entity item) {
        if (item instanceof Treasure || item instanceof SunStone) {
            player.collectTreasure();
        } else if (item instanceof Key) {
            if (inventory.hasKey()) {
                return false;
            }
        }

        return inventory.add((InventoryItem) item);
    }
}
