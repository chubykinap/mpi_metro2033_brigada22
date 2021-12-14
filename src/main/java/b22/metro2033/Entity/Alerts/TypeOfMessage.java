package b22.metro2033.Entity.Alerts;

import b22.metro2033.Entity.Army.Rank;
import javassist.NotFoundException;

public enum TypeOfMessage {

    ERROR,
    NOTIFICATION,
    ALARM;

    private final static String[] ranksRU = new String[]{
            "Ошибка", "Уведомление", "Тревога"
    };

    public static String[] getRankListRU() {
        return ranksRU;
    }

    public static String getStateRU(Rank state) {
        return ranksRU[state.ordinal()];
    }

    public static TypeOfMessage findState(String state) throws Exception {
        switch (state) {
            case "Ошибка":
                return ERROR;
            case "Уведомление":
                return NOTIFICATION;
            case "Тревога":
                return ALARM;
        }
        throw new NotFoundException("STATE NOT FOUND");
    }

}
