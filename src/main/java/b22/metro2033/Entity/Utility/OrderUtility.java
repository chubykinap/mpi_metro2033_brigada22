package b22.metro2033.Entity.Utility;

import b22.metro2033.Entity.Delivery.DeliveryOrder;
import b22.metro2033.Entity.Delivery.DeliveryState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderUtility {
    private long id;
    private String departureStation;
    private String arrivalStation;
    private DeliveryState state;
    private String date;

    public OrderUtility(DeliveryOrder order) {
        this.id = order.getId();
        this.departureStation = order.getDepartureStation();
        this.arrivalStation = order.getArrivalStation();
        this.state = order.getState();
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        this.date = dt1.format(order.getDate());
    }

    public static List<OrderUtility> toOrderUtility(List<DeliveryOrder> orders) {
        List<OrderUtility> list = new ArrayList<>();
        for (DeliveryOrder order : orders) {
            list.add(new OrderUtility(order));
        }
        return list;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public String getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(String arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

    public DeliveryState getState() {
        return state;
    }

    public void setState(DeliveryState state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
