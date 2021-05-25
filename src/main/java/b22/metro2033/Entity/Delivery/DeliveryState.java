package b22.metro2033.Entity.Delivery;

import java.util.ArrayList;
import java.util.List;

public enum DeliveryState {
    RECEIVED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED;

    public static List<DeliveryState> getHigher(DeliveryState state){
        List<DeliveryState> res = new ArrayList<>();
        int value = state.ordinal();
        while (value < 4) {
            res.add(DeliveryState.values()[value++]);
        }
        return res;
    }
}
