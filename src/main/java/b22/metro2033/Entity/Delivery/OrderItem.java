package b22.metro2033.Entity.Delivery;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Embeddable
class OrderItemPK implements Serializable {
    @Column(name = "order_id")
    private long order_id;

    @Column(name = "item_id")
    private long item_id;
}

@Entity
@Table(name = "item_in_order")
public class OrderItem {
    @EmbeddedId
    private OrderItemPK id;

    private int quantity;

    @ManyToOne
    @MapsId("order_id")
    @JoinColumn(name = "order_id")
    private DeliveryOrder deliveryOrder;

    @ManyToOne
    @MapsId("item_id")
    @JoinColumn(name = "item_id")
    private Item item;

    public OrderItem() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}