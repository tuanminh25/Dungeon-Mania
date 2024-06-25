package dungeonmania.entities.enemies;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.enemies.MoveStrategy.MoveStrategy;
import dungeonmania.entities.enemies.MoveStrategy.MoveWithInvincible;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;

    private double allyAttack;
    private double allyDefence;
    private boolean allied = false;
    private boolean isAdjacentToPlayer = false;
    private boolean mindControlled = false;
    private int mindControlDuration;

    private MoveStrategy moveStrategy;
    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
    }

    public boolean isAllied() {
        return allied || mindControlled;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (isAllied())
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return canBeBribedBySceptre(player) || canBeBribedByTreasure(player);
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        if (canBeBribedBySceptre(player)) {
            Sceptre sceptre = player.getFirstInInventory(Sceptre.class);
            this.mindControlDuration = sceptre.getMindControlTime();
            player.getInventory().remove(sceptre);
            mindControlled = true;
        } else if (canBeBribedByTreasure(player)) {
            for (int i = 0; i < bribeAmount; i++) {
                player.use(Treasure.class);
            }
            allied = true;
        }
    }

    @Override
    public void interact(Player player, Game game) {
        bribe(player);
        if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
            isAdjacentToPlayer = true;
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        Player player = game.getPlayer();
        if (isAllied()) {
            nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                    : map.dijkstraPathFind(getPosition(), player.getPosition(), this);
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), nextPos))
                isAdjacentToPlayer = true;
            if (!isAdjacentToPlayer && player.getPosition().equals(nextPos)) {
                isAdjacentToPlayer = true;
                nextPos = player.getPreviousDistinctPosition();
            }

        } else if (map.getPlayerEffectivePotion() instanceof InvisibilityPotion) {
            // Move random
            Random randGen = new Random();
            List<Position> pos = getCardinallyAdjacentPositions();
            pos = pos.stream().filter(p -> map.canMoveTo(this, p)).collect(Collectors.toList());
            if (pos.size() == 0) {
                nextPos = getPosition();
                map.moveTo(this, nextPos);
            } else {
                nextPos = pos.get(randGen.nextInt(pos.size()));
                map.moveTo(this, nextPos);
            }
        } else if (map.getPlayerEffectivePotion() instanceof InvincibilityPotion) {
            moveStrategy = new MoveWithInvincible<Mercenary>(map, this);
            nextPos = moveStrategy.move();
        } else {
            // Follow hostile
            nextPos = map.dijkstraPathFind(getPosition(), player.getPosition(), this);
        }
        map.moveTo(this, nextPos);
    }

    @Override
    public boolean isInteractable(Player player) {
        return !isAllied() && canBeBribed(player);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!isAllied())
            return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }

    private boolean isInRange(Player player) {
        Position dis = Position.calculatePositionBetween(player.getPosition(), this.getPosition());
        int x = Math.abs(dis.getX());
        int y = Math.abs(dis.getY());
        return x <= bribeRadius && y <= bribeRadius;
    }

    private boolean canBeBribedByTreasure(Player player) {
        return isInRange(player) && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }
    private boolean canBeBribedBySceptre(Player player) {
        return player.countEntityOfType(Sceptre.class) > 0;
    }

    public void controlUpdate() {
        if (mindControlled) {
            mindControlDuration--;
        } else {
            return;
        }
        if (mindControlDuration <= 0) {
            mindControlled = false;
        }
    }
}
