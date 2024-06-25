package dungeonmania.entities.enemies.MoveStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.entities.enemies.Enemy;

public class MoveWithInvincible<E extends Enemy> implements MoveStrategy {
    private GameMap map;
    private E enemyEntity;

    public MoveWithInvincible(GameMap map, E enemyEntity) {
        this.map = map;
        this.enemyEntity = enemyEntity;
    }

    @Override
    public Position move() {
        Position plrDiff = Position.calculatePositionBetween(map.getPlayerPosition(), enemyEntity.getPosition());

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(enemyEntity.getPosition(), Direction.RIGHT)
                : Position.translateBy(enemyEntity.getPosition(), Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(enemyEntity.getPosition(), Direction.UP)
                : Position.translateBy(enemyEntity.getPosition(), Direction.DOWN);
        Position offset = enemyEntity.getPosition();
        if (plrDiff.getY() == 0 && map.canMoveTo(enemyEntity, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && map.canMoveTo(enemyEntity, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (map.canMoveTo(enemyEntity, moveX))
                offset = moveX;
            else if (map.canMoveTo(enemyEntity, moveY))
                offset = moveY;
            else
                offset = enemyEntity.getPosition();
        } else {
            if (map.canMoveTo(enemyEntity, moveY))
                offset = moveY;
            else if (map.canMoveTo(enemyEntity, moveX))
                offset = moveX;
            else
                offset = enemyEntity.getPosition();
        }
        return offset;
    }
}
