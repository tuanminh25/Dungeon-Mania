package dungeonmania.entities.buildables;
import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.InventoryItem;

public class BuildableFactory {
    private Buildable createBow(List<InventoryItem> items, List<Wood> woods,
    List<Arrow> arrows, List<SunStone> sunStones, EntityFactory factory) {
        int sunStonesNeeded = 0;
        int woodsNeeded = 0;
        int arrowsNeeded = 0;

        if (woods.size() >= 1) {
            sunStonesNeeded = 0;
            woodsNeeded = 1;
        } else {
            sunStonesNeeded = 1;
        }

        if (arrows.size() < 3) {
            sunStonesNeeded = sunStonesNeeded + (3 - arrows.size());
            arrowsNeeded = 3 - arrows.size();
        } else {
            arrowsNeeded = 3;
        }

        if (sunStones.size() >= sunStonesNeeded) {
            removeItems(woodsNeeded, woods, items);
            removeItems(arrowsNeeded, arrows, items);
            return factory.buildBow();
        }
        return null;
    }

    private Buildable createShield(List<InventoryItem> items, List<Wood> woods, List<Treasure> treasures,
    List<Key> keys, List<SunStone> sunStones, EntityFactory factory) {
        int sunStonesNeeded = 0;
        int woodsNeeded = 0;
        int treasuresNeeded = 0;
        int keysNeeded = 0;
        if (woods.size() >= 2) {
            sunStonesNeeded = 0;
            woodsNeeded = 2;
        } else {
            sunStonesNeeded = 2 - woods.size();
            woodsNeeded = 0;
        }

        if (keys.size() >= 1) {
            keysNeeded = 1;
        } else if (treasures.size() >= 1) {
            treasuresNeeded = 1;
        } else {
            sunStonesNeeded = sunStonesNeeded + 1;
        }
        if (sunStones.size() >= sunStonesNeeded) {
            removeItems(woodsNeeded, woods, items);
            removeItems(treasuresNeeded, treasures, items);
            removeItems(keysNeeded, keys, items);
            return factory.buildShield();
        }
        return null;
    }

    private Buildable createSceptre(List<InventoryItem> items, List<Wood> woods, List<Arrow> arrows,
    List<Key> keys, List<Treasure> treasures, List<SunStone> sunStones, EntityFactory factory) {
        int woodsNeeded = 0;
        int keysNeeded = 0;
        int treasuresNeeded = 0;
        int arrowsNeeded = 0;
        int sunStonesNeeded = 1;

        // Considering wood or arrows
        if (woods.size() >= 1) {
            woodsNeeded = 1;
        } else if (arrows.size() >= 2) {
            arrowsNeeded = 2;
        } else {
            // Dont need wood or arrow
            sunStonesNeeded = sunStonesNeeded + 1;
        }

        // Considering treasure or sunstone
        if (treasures.size() >= 1) {
            treasuresNeeded = 1;
        } else if (keys.size() >= 1) {
            keysNeeded = 1;
        } else {
            // Dont need keys or treasures
            sunStonesNeeded = sunStonesNeeded + 1;
        }
        if (sunStones.size() >= sunStonesNeeded) {
            removeItems(woodsNeeded, woods, items);
            removeItems(treasuresNeeded, treasures, items);
            removeItems(keysNeeded, keys, items);
            removeItems(arrowsNeeded, arrows, items);
            removeItems(sunStonesNeeded, sunStones, items);
            return factory.buildSceptre();
        }
        return null;
    }

    private Buildable createMidNightAmour(List<InventoryItem> items, List<Sword> swords,
    List<SunStone> sunStones, EntityFactory factory) {
        if (swords.size() > 0 && sunStones.size() > 0) {
            removeItems(1, swords, items);
            removeItems(1, sunStones, items);
            return factory.buildMidNightAmour();
        }
        return null;
    }

    public Buildable createBuildable(List<InventoryItem> items, List<Wood> woods, List<Treasure> treasures,
    List<Key> keys, List<Arrow> arrows, List<SunStone> sunStones, List<Sword> swords, EntityFactory factory,
    String entityString) {
        switch (entityString) {
            case "bow":
                return createBow(items, woods, arrows, sunStones, factory);
            case "shield":
                return createShield(items, woods, treasures, keys, sunStones, factory);
            case "sceptre":
                return createSceptre(items, woods, arrows, keys, treasures, sunStones, factory);
            case "midnight_armour":
                return createMidNightAmour(items, swords, sunStones, factory);
            default:
                break;
        }
        return null;
    }

    private <T> void removeItems(int needed, List<T> itemList, List<InventoryItem> items) {
        for (int i = 0; i < needed && i < itemList.size(); i++) {
            items.remove(itemList.get(i));
        }
    }
}
