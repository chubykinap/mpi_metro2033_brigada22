package b22.metro2033.Entity.Delivery;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "delivery_order")
public class DeliveryOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "Departure station should not be empty")
    private String station;
    private boolean isPointOfDeparture;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "State should not be empty")
    private DeliveryState state;
    private Date date;

    @OneToOne(mappedBy = "order")
    private Courier courier;

    @OneToMany(mappedBy = "id.order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public DeliveryOrder() {}

    public DeliveryOrder(String station, boolean isPointOfDeparture, DeliveryState state, Date date){
        this.station = station;
        this.isPointOfDeparture = isPointOfDeparture;
        this.state = state;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public boolean isPointOfDeparture() {
        return isPointOfDeparture;
    }

    public void setPointOfDeparture(boolean pointOfDeparture) {
        isPointOfDeparture = pointOfDeparture;
    }

    public DeliveryState getState() {
        return state;
    }

    public void setState(DeliveryState state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
