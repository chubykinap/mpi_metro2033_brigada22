package b22.metro2033.Entity.Delivery;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "delivery_order")
public class DeliveryOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String departureStation;
    private String arrivalStation;
    private String state;
    private Date date;

    @ManyToMany(mappedBy = "orders")
    private List<Courier> couriers;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    DeliveryOrder() {}

    DeliveryOrder(long id, String departureStation, String arrivalStation, String state, Date date){
        this.id = id;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
