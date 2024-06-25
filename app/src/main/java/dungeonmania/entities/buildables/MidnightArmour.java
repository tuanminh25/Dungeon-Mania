package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;

public class MidnightArmour extends Buildable {
    private double defence;
    private double attack;

    public MidnightArmour(int durability, double defence, double attack) {
        super(null, durability);
        this.defence = defence;
        this.attack = attack;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, attack, defence, 1, 1));
    }
}
