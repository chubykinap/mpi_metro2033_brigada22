package b22.metro2033.Entity.Army;

import javassist.NotFoundException;

public enum HealthState {
    HEALTHY,
    ILL,
    INJURED,
    CRITICAL;

    private final static String[] stateRU = new String[]{
            "Здоров", "Болен", "Ранен", "Критическое состояние"
    };

    public static String[] getStateListRU() {
        return stateRU;
    }

    public static String getStateRU(HealthState state) {
        return stateRU[state.ordinal()];
    }

    public static HealthState findState(String state) throws Exception {
        switch (state) {
            case "Здоров":
                return HEALTHY;
            case "Болен":
                return ILL;
            case "Ранен":
                return INJURED;
            case "Критическое состояние":
                return CRITICAL;
        }
        throw new NotFoundException("STATE NOT FOUND");
    }
}
