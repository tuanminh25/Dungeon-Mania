package dungeonmania.entities.buildables;
import dungeonmania.battles.BattleStatistics;

public class Sceptre extends Buildable {
    private int mindControlTime;
    public Sceptre(int durability, int mindControlTime) {
        super(null, durability);
        this.mindControlTime = mindControlTime;
    }

    // Do notthing with statistic
    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin,
        new BattleStatistics(0, 0, 0, 1, 1));
    }

    public int getMindControlTime() {
        return mindControlTime;
    }

    public void setMindControlTime(int mindControlTime) {
        this.mindControlTime = mindControlTime;
    }
}
