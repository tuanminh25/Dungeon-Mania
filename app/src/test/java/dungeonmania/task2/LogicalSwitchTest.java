package dungeonmania.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import dungeonmania.mvp.TestUtils;

public class LogicalSwitchTest {
    private DungeonManiaController dmc;
    @BeforeEach
    public void setUp() {
        dmc =  new DungeonManiaController();
    }

    @Test
    @Tag("2f-1")
    @DisplayName("light up bulb Or Simple ")
    public void simpleOrLightBulb() {
        DungeonResponse res = dmc.newGame("d_LogicalTest_simpleOrLightBulb", "c_simple");
        assertEquals(1, TestUtils.countType(res, "light_bulb_off"));

        //push bolder activate switch
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.countType(res, "light_bulb_on"));
        assertEquals(0, TestUtils.countType(res, "light_bulb_off"));

        //deactivate
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.countType(res, "light_bulb_on"));
        assertEquals(1, TestUtils.countType(res, "light_bulb_off"));

    }

    @Test
    @Tag("2f-2")
    @DisplayName("Open Door Or simple ")
    public void simpleOrOpenDoor() {
        DungeonResponse res = dmc.newGame("d_LogicalTest_simpleOrDoor", "c_simple");
        //door not open player stuck
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        Position initial = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.RIGHT);
        Position after = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertEquals(initial, after);


        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);

        //push bolder activate switch
        //door open
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        Position player = TestUtils.getEntities(res, "player").get(0).getPosition();
        Position door = TestUtils.getEntities(res, "switch_door").get(0).getPosition();
        assertEquals(player, door);
    }

    @Test
    @Tag("2f-3")
    @DisplayName("Bomb Explode Or simple ")
    public void simpleOrBombExplode() throws InvalidActionException {
        DungeonResponse res = dmc.newGame("d_LogicalTest_simpleOrBomb", "c_simple");
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        //place bomb
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);

        //activate switch
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "switch").size());
        assertEquals(0, TestUtils.getEntities(res, "wire").size());
    }

    @Test
    @Tag("2f-4")
    @DisplayName("3 surrounding conductor ")
    public void andLightBulb() {
        DungeonResponse res = dmc.newGame("d_LogicalTest_andLightBulb", "c_simple");

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.countType(res, "light_bulb_off"));
        //push bolder activate switch
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.countType(res, "light_bulb_on"));
        assertEquals(0, TestUtils.countType(res, "light_bulb_off"));
    }

    @Test
    @Tag("2f-5")
    @DisplayName("1 conductor not activate and")
    public void andLightBulbNotOn() {
        DungeonResponse res = dmc.newGame("d_LogicalTest_andLightBulbNotOn", "c_simple");

        //activate
        // only 1 wire adj
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.countType(res, "light_bulb_off"));
    }

    @Test
    @Tag("2f-6")
    @DisplayName("xor light bulb on then off because 3 surrounding conductor")
    public void xorLightBulbOn() {
        DungeonResponse res = dmc.newGame("d_LogicalTest_xorLightBulb", "c_simple");

        //activate
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.countType(res, "light_bulb_on"));
        assertEquals(0, TestUtils.countType(res, "light_bulb_off"));

        //activate other switch but bulb not light up cuz 3 surrounding
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.countType(res, "light_bulb_off"));
        assertEquals(0, TestUtils.countType(res, "light_bulb_on"));

    }

    @Test
    @Tag("2f-7")
    @DisplayName("co_and on because same tick")
    public void coAndLightBulbOn() {
        DungeonResponse res = dmc.newGame("d_LogicalTest_coAndLightBulb", "c_simple");

        //activate
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.countType(res, "light_bulb_on"));
        assertEquals(0, TestUtils.countType(res, "light_bulb_off"));

    }

    @Test
    @Tag("2f-8")
    @DisplayName("co_and off bcause not same tick")
    public void coAndLightBulbNotOn() {
        DungeonResponse res = dmc.newGame("d_LogicalTest_coAndLightBulb", "c_simple");

        //activate
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.countType(res, "light_bulb_off"));
        assertEquals(0, TestUtils.countType(res, "light_bulb_on"));
    }

    @Test
    @Tag("2f-9")
    @DisplayName("light bulb on then off because bomb placed next to the wire")
    public void bombDisruptConnection() throws InvalidActionException {
        DungeonResponse res = dmc.newGame("d_LogicalTest_bombDisruptConnection", "c_simple");

        //activate
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.countType(res, "light_bulb_off"));
        assertEquals(1, TestUtils.countType(res, "light_bulb_on"));
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        //pick up bomb
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        //place bomb and explode
        res = dmc.tick(Direction.UP);
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());
        assertEquals(1, TestUtils.countType(res, "light_bulb_off"));
        assertEquals(0, TestUtils.countType(res, "light_bulb_on"));
    }

    @Test
    @Tag("2f-10")
    @DisplayName("light bulb wont turn on because bomb explode first")
    public void bombDisruptConnection2() throws InvalidActionException {
        DungeonResponse res = dmc.newGame("d_LogicalTest_bombDisruptBeforeBulbOn", "c_simple");

        //activate
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.countType(res, "light_bulb_on"));
        assertEquals(1, TestUtils.countType(res, "light_bulb_off"));
    }
}
