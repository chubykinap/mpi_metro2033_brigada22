package b22.metro2033.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "item_in_order")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;
    private int orderId;
    private int quantity;

//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Curier> curierList;

    @ManyToOne(mappedBy = "delivery_order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryOrder> deliveryOrderList;

    @ManyToOne(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> itemList;


    public OrderItem() {
    }

    public OrderItem(int itemId, int orderId, int quantity) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
