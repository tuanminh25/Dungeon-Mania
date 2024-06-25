package dungeonmania.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import dungeonmania.mvp.TestUtils;

public class EnemyGoalTest {
    private DungeonManiaController dmc;
    @BeforeEach
    public void setUp() {
        dmc =  new DungeonManiaController();
    }

    @Test
    @Tag("2a-1")
    @DisplayName("Test Player defeat a mercenary ")
    public void simpleDefeatMercenary() {
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_oneMerc", "c_enemyGoalTest_noZombies_oneDefeat");
        res = dmc.tick(Direction.RIGHT);

        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2a-2")
    @DisplayName("Test Player defeat three mercenaries ")
    public void simpleDefeatThreeMercenary() {
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_threeMerc", "c_enemyGoalTest_noZombies_threeDefeat");
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        //defeat 1 merc
        assertEquals(2, TestUtils.countType(res, "mercenary"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        //defeat 2 merc
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.countType(res, "mercenary"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        //defeat 3 merc
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.countType(res, "mercenary"));
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2a-3")
    @DisplayName("Test Player defeat 1 mercenary or exit ")
    public void defeatOrExit() {
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_orExit", "c_enemyGoalTest_noZombies_threeDefeat");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.countType(res, "mercenary"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        assertEquals("", TestUtils.getGoals(res));

    }

    @Test
    @Tag("2a-4")
    @DisplayName("Test Player defeat 1 mercenary and exit ")
    public void defeatOneAndExit() {
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_andExit", "c_enemyGoalTest_noZombies_oneDefeat");
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.countType(res, "mercenary"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        assertEquals("", TestUtils.getGoals(res));

    }

    @Test
    @Tag("2a-5")
    @DisplayName("Test Player defeat 3 mercenaries and exit ")
    public void defeatThreeAndExit() {
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_andExitThreeMerc", "c_enemyGoalTest_noZombies_threeDefeat");
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        //defeat 1 merc
        assertEquals(2, TestUtils.countType(res, "mercenary"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        //get to exit
        res = dmc.tick(Direction.RIGHT);


        //defeat 2 mercd
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.countType(res, "mercenary"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        //defeat 3 merc
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.countType(res, "mercenary"));

        //exit
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2a-6")
    @DisplayName("Test Player defeat 1 spawner")
    public void defeatOneSpawnerAndWin() {
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_zombies", "c_enemyGoalTest_withZombies_noDefeat");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertEquals(1, TestUtils.countType(res, "zombie_toast"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = dmc.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2a-7")
    @DisplayName("Test Player defeat 1 spawner 1 merc and exit ")
    public void defeatOneSpawnerOneMercAndExit() {
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_mercZombies", "c_enemyGoalTest_noZombies_oneDefeat");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        res = dmc.tick(Direction.LEFT);

        //kill mercenery
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.countType(res, "mercenary"));

        res = dmc.tick(Direction.LEFT);
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2a-8")
    @DisplayName("Test Player defeat 3 spawners and exit ")
    public void defeatSpawners() {
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_andExitThreeSpawners",
        "c_enemyGoalTest_noZombies_bigSword");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        String spawnerId2 = TestUtils.getEntities(res, "zombie_toast_spawner").get(1).getId();
        String spawnerId3 = TestUtils.getEntities(res, "zombie_toast_spawner").get(2).getId();

        res = dmc.tick(Direction.LEFT);

        //kill spawners
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId2));
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId3));

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2a-8")
    @DisplayName("Test Player defeat 3 spawners and exit ")
    public void defaultWin() {
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_noDefeatWin", "c_enemyGoalTest_withZombies_noDefeat");
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", TestUtils.getGoals(res));
    }
}
