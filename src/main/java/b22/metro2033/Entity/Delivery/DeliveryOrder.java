package b22.metro2033.Entity.Delivery;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    private String departureStation;
    @NotNull(message = "Arrival station should not be empty")
    private String arrivalStation;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "State should not be empty")
    private DeliveryState state;
    private Date date;

    @ManyToMany(mappedBy = "orders")
    private List<Courier> couriers;

    @OneToMany(mappedBy = "id.order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orders;

    public DeliveryOrder() {}

    public DeliveryOrder(String departureStation, String arrivalStation, DeliveryState state, Date date){
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.state = state;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
