package b22.metro2033.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "curier_order")
public class CurierOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;
    private int curierId;

    @OneToMany(mappedBy = "curier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Curier> curierList;

    @ManyToOne(mappedBy = "delivery_order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryOrder> deliveryOrderList;

    public CurierOrder() {
    }

    public CurierOrder(int curierId, int orderId) {
        this.curierId = curierId;
        this.orderId = orderId;
    }

    public int getCurierId() {
        return curierId;
    }

    public void setCurierId(int curierId) {
        this.curierId = curierId;
    }

    public int getOrderId() { return orderId; }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

}
