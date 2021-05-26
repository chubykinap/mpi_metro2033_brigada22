package b22.metro2033.Entity.Army;

import javassist.NotFoundException;

public enum Rank {
    CADET,
    LIEUTENANT,
    CAPTAIN,
    MAJOR;

    private final static String[] ranksRU = new String[]{
            "Рядовой", "Лейтенант", "Капитан", "Майор"
    };

    public static String[] getRankListRU() {
        return ranksRU;
    }


    public static String getStateRU(Rank state) {
        return ranksRU[state.ordinal()];
    }

    public static Rank findState(String state) throws Exception {
        switch (state) {
            case "Рядовой":
                return CADET;
            case "Лейтенант":
                return LIEUTENANT;
            case "Капитан":
                return CAPTAIN;
            case "Майор":
                return MAJOR;
        }
        throw new NotFoundException("STATE NOT FOUND");
    }
}