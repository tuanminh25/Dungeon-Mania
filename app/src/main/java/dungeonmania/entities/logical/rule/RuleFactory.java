package dungeonmania.entities.logical.rule;

public class RuleFactory {

    public Rule createRule(String rule) {
        switch (rule) {
            case "or":
                return new Or();
            case "and":
                return new And();
            case "xor":
                return new Xor();
            case "co_and":
                return new CoAnd();
            default:
                return null;
        }
    }
}
