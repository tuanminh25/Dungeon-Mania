package dungeonmania.entities.enemies;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;
import dungeonmania.entities.enemies.MoveStrategy.MoveStrategy;
import dungeonmania.entities.enemies.MoveStrategy.MoveWithInvincible;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;
    private Random randGen = new Random();

    private MoveStrategy moveStrategy;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        if (map.getPlayerEffectivePotion() instanceof InvincibilityPotion) {
            moveStrategy = new MoveWithInvincible<ZombieToast>(map, this);
            nextPos = moveStrategy.move();
        } else {
            List<Position> pos = getCardinallyAdjacentPositions();
            pos = pos.stream().filter(p -> map.canMoveTo(this, p)).collect(Collectors.toList());
            if (pos.size() == 0) {
                nextPos = getPosition();
            } else {
                nextPos = pos.get(randGen.nextInt(pos.size()));
            }
        }
        game.moveTo(this, nextPos);

    }

}
