package b22.metro2033.Entity.Delivery;

import java.util.ArrayList;
import java.util.List;

public enum DeliveryState {
    CLOSED,
    RECEIVED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED;

    public static List<DeliveryState> getHigher(DeliveryState state) {
        List<DeliveryState> res = new ArrayList<>();
        if (state == CLOSED)
            res.add(CLOSED);
        else if (state != COMPLETED) {
            int value = state.ordinal();
            if (value == 0)
                value++;
            while (value < 4) {
                res.add(DeliveryState.values()[value++]);
            }
        }
        else
            res.add(COMPLETED);
        return res;
    }
}
