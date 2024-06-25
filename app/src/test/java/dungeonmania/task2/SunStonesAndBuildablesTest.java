package dungeonmania.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SunStonesAndBuildablesTest {
    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }

    @Test
    @DisplayName("Create and pickup Sunstone")
    public void createAndPickup() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstone
        DungeonResponse res = dmc.newGame("d_basicSunStonesTest_CreateAndPickUp", "c_basicGoalsTest_exit");

        // check inventory of player before picking up
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // move player to right (where the sunstone is currently on)
        res = dmc.tick(Direction.RIGHT);

        // check inventory of player after picking up
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Sun Stones in Goal Treasure")
    public void sunStoneAsTreasures() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstone: 1 sunstone 1 normal treasure + map config has goal treasure = 2
        DungeonResponse res = dmc.newGame("d_SunStonesTest_TreasureAndExit", "c_sunstonesTest_manyTreasures");

        // Initial Goal
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // move player to right (collect 1 treasure)
        res = dmc.tick(Direction.RIGHT);

        // move player to right (to exit)
        res = dmc.tick(Direction.RIGHT);

        // Still not complete the game
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // move player to the sunstone
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        // Have fullfilled treasure goals, still not win the game
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));
        assertNotEquals("", TestUtils.getGoals(res));

        // move player to exit
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Game is completed
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Sun Stones opens unlimited doors")
    public void sunStonesOpenConsecutivesDoors() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStoneTest_OpenManyDoors", "c_sunstonesTest_manyTreasures");

        // Goal : exit
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // move player to the sunstone
        res = dmc.tick(Direction.RIGHT);

        // Player unlocks the first door then move forward and back that door
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.LEFT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // move player through doors
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Move player to exit
        res = dmc.tick(Direction.RIGHT);

        // Game is completed
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Sun Stones opens unlimited doors")
    public void prioritizingKeyWhenOpenningDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStonesTest_PrioritizeKeyOverStones", "c_sunstonesTest_manyTreasures");

        // Goal : exit
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // move player to the sunstone
        res = dmc.tick(Direction.RIGHT);

        // Player unlocks the first door then move forward and back that door
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.LEFT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // move player through 1st door
        res = dmc.tick(Direction.RIGHT);

        // check inventory of player before picking up the key
        assertEquals(0, TestUtils.getInventory(res, "key").size());

        // move player to key position
        res = dmc.tick(Direction.RIGHT);

        // check inventory of player after picking up the key
        assertEquals(1, TestUtils.getInventory(res, "key").size());

        // open the door with the key
        res = dmc.tick(Direction.RIGHT);

        // check inventory of player before picking up the key
        assertEquals(0, TestUtils.getInventory(res, "key").size());

        // Move player to exit
        res = dmc.tick(Direction.RIGHT);

        // Game is completed
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Create Bow with one sun stone")
    public void createBowWithOneSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStoneTest_SubsituteBuilds", "c_sunstonesTest_manyTreasures");

        // move player to the sunstone
        res = dmc.tick(Direction.RIGHT);

        // move player to collect 1 wood, 2 arrows
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // check inventory
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // create bow
        res = assertDoesNotThrow(() -> dmc.build("bow"));
        assertEquals(1, TestUtils.getInventory(res, "bow").size());
    }

    @Test
    @DisplayName("Create Shield with one sun stone")
    public void createShieldWithOneSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStoneTest_SubsituteBuilds", "c_sunstonesTest_manyTreasures");

        // move player to the sunstone
        res = dmc.tick(Direction.LEFT);

        // move player to collect 2 wood
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        // check inventory
        assertEquals(2, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // create bow
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());
    }

    @Test
    @DisplayName("Prioritize Other Ingredients before sun stone")
    public void createBowWithoutSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStoneTest_SubsituteBuilds", "c_sunstonesTest_manyTreasures");

        // move player to the sunstone
        res = dmc.tick(Direction.RIGHT);

        // move player to collect 1 wood, 2 arrows
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // check inventory
        assertEquals(3, TestUtils.getInventory(res, "arrow").size());
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // create bow
        res = assertDoesNotThrow(() -> dmc.build("bow"));
        assertEquals(1, TestUtils.getInventory(res, "bow").size());

        // check inventory
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Create Shield with one sun stone")
    public void createShieldWithoutSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStoneTest_SubsituteBuilds", "c_sunstonesTest_manyTreasures");

        // move player to the sunstone
        res = dmc.tick(Direction.LEFT);

        // move player to collect 2 wood
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        // check inventory
        assertEquals(2, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // create bow
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // check inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @DisplayName("Testing a mercenary cannot be bribed with a certain amount")
    public void cannotBeBrided() {
        //                                                          Wall     Wall     Wall    Wall    Wall
        // P1       P2/Treasure      P3/Treasure    P4/Treasure      M4       M3       M2     M1      Wall
        //                                                          Wall     Wall     Wall    Wall    Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SunStoneTest_cannotBribe", "c_mercenaryTest_bribeAmount");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up first treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(7, 1), getMercPos(res));

        // attempt bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // pick up second treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(6, 1), getMercPos(res));

        // attempt bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        // pick up the sun stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(5, 1), getMercPos(res));
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // still can not bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

    }

    @Test
    @DisplayName("Create Shield with many sun stones")
    public void createShieldWithManySunStones() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStoneTest_SubsituteBuilds", "c_sunstonesTest_manyTreasures");

        // collect first stones
        res = dmc.tick(Direction.RIGHT);

        // collect second stones
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        // move player to collect 1 wood
        res = dmc.tick(Direction.LEFT);

        // check inventory
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

        // create bow
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());
    }

    @Test
    @DisplayName("Create Bow with many sun stones")
    public void createBowWithManySunStones() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStoneTest_SubsituteBuilds", "c_sunstonesTest_manyTreasures");

        // collect first stone
        res = dmc.tick(Direction.LEFT);

        // collect second stones
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // move player to collect 1 wood, 1 arrow
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // check inventory
        assertEquals(1, TestUtils.getInventory(res, "arrow").size());
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

        // create bow
        res = assertDoesNotThrow(() -> dmc.build("bow"));
        assertEquals(1, TestUtils.getInventory(res, "bow").size());
    }

    @Test
    @DisplayName("Create Sceptre with basic ingredients")
    public void createSceptre() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStoneTest_SubsituteBuilds", "c_spectreTest_basicFunctions");

        // move player to the sunstone
        res = dmc.tick(Direction.LEFT);

        // move player to collect 2 wood and 1 treasure
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        // check inventory
        assertEquals(2, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // create sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // check inventory after crafting
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @DisplayName("Sceptre Basic Functions: bribe from anywhere")
    public void sceptreBribeFromAnyWhere() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map
        DungeonResponse res = dmc.newGame("d_SunStoneTest_bribeFromAnywhere", "c_spectreTest_basicFunctions");
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // move player to the treasure
        res = dmc.tick(Direction.RIGHT);

        // attempt bribe: out of range
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // check inventory
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // create sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // check inventory after crafting
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // achieve bribe with sceptre
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
    }

    @Test
    @DisplayName("Create MidNight Amour")
    public void createMidNightAmour() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStones_CreateMidNightAmour", "c_spectreTest_basicFunctions");

        // move player to the sunstone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // check inventory
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // create armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));

        // check inventory after crafting
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
    }

    @Test
    @DisplayName("Cannot create MidNight Armour with Zombies")
    public void cannotCreateMidNightAmourWithZombies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStones_CreateMidNightAmour", "c_spectreTest_basicFunctions");

        // move player to the sunstone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // check inventory
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // can not create armour due to existing zombies
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
    }

    @Test
    @DisplayName("Combat with MidNight Armour")
    public void combatWithMidNightArmour() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStones_CreateMidNightAmour", "c_spectreTest_basicFunctions");

        // move player to the sunstone
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // check inventory
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // create armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));

        // walk to the end of the road (adjacent to spawner)
        // player should be still alive with the armour, without armour, player should have died
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(getPlayerPos(res), new Position(11, 1));
    }

    @Test
    @DisplayName("sceptre duration")
    public void sceptreTickDuration() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // Create map with sunstones and 3 doors
        DungeonResponse res = dmc.newGame("d_SunStonesTest_SceptreTick", "c_spectreTest_basicFunctions");

        // move player to collect item and create sceptre
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // create sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Mindcontrol for 2 tick
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // After 2 ticks, combat
        assertEquals(1, res.getBattles().size());
    }
}
