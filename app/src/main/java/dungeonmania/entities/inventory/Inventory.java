package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.BuildableFactory;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.enemies.ZombieToast;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables() {

        int wood = count(Wood.class);
        int arrows = count(Arrow.class);
        int treasure = count(Treasure.class);
        int keys = count(Key.class);
        int sunStones = count(SunStone.class);
        int swords = count(Sword.class);
        List<String> result = new ArrayList<>();

        if (bowCondition(wood, arrows, sunStones)) {
            result.add("bow");
        }
        if (shieldCondition(wood, treasure, keys, sunStones)) {
            result.add("shield");
        }
        if (sceptreCondition(wood, treasure, keys, arrows, sunStones)) {
            result.add("sceptre");
        }
        if (midNightAmourCondition(swords, sunStones)) {
            result.add("midnight_armour");
        }
        return result;
    }



    public InventoryItem checkBuildCriteria(Player p, String entityString, EntityFactory factory) {

        List<Wood> woods = getEntities(Wood.class);
        List<Arrow> arrows = getEntities(Arrow.class);
        List<Treasure> treasures = getEntities(Treasure.class);
        List<Key> keys = getEntities(Key.class);
        List<SunStone> sunStones = getEntities(SunStone.class);
        List<Sword> swords = getEntities(Sword.class);
        BuildableFactory buildableFactory = new BuildableFactory();
        return buildableFactory.createBuildable(items, woods, treasures, keys, arrows,
        sunStones, swords, factory, entityString);
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId))
                return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public BattleItem getWeapon() {
        BattleItem weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

    public void useWeapon(Game game) {
        getWeapon().use(game);
    }

    private boolean bowCondition(int wood, int arrows, int sunStones) {
        int sunStonesNeeded = 0;

        if (wood >= 1) {
            sunStonesNeeded = 0;
        } else {
            sunStonesNeeded = 1;
        }

        if (arrows < 3) {
            sunStonesNeeded = sunStonesNeeded + (3 - arrows);
        }

        return sunStones >= sunStonesNeeded;
    }

    private boolean shieldCondition(int woods, int treasures, int keys, int sunStones) {
        int sunStonesNeeded = 0;
        if (woods >= 2) {
            sunStonesNeeded = 0;
        } else {
            sunStonesNeeded = 2 - woods;
        }

        if (keys < 1 && treasures < 1) {
            sunStonesNeeded = sunStonesNeeded + 1;
        }
        return sunStones >= sunStonesNeeded;
    }

    private boolean sceptreCondition(int woods, int treasures, int keys, int arrows, int sunStones) {
        int sunStonesNeeded = 1;

        if (woods < 1 && arrows < 2) {
            sunStonesNeeded = sunStonesNeeded + 1;
        }

        if (treasures < 1 && keys < 1) {
            sunStonesNeeded = sunStonesNeeded + 1;
        }
        return sunStones >= sunStonesNeeded;
    }

    public boolean hasKey() {
        return getFirst(Key.class) != null;
    }

    private boolean midNightAmourCondition(int swords, int sunStones) {
        return swords > 0 && sunStones > 0 && !isZombieActive();
    }

    private boolean isZombieActive() {
        return getEntities(ZombieToast.class).size() > 0;
    }
}
