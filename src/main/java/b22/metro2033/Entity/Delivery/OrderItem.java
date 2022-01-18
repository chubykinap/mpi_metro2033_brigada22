package b22.metro2033.Entity.Delivery;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
class OrderItemPK implements Serializable {
    @ManyToOne(cascade = CascadeType.ALL)
    private DeliveryOrder order;
    @ManyToOne(cascade = CascadeType.ALL)
    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public DeliveryOrder getOrder() {
        return order;
    }

    public void setOrder(DeliveryOrder order) {
        this.order = order;
    }
}

@Entity
@Table(name = "item_in_order")
@AssociationOverrides({
        @AssociationOverride(name = "id.item", joinColumns = @JoinColumn(name = "item_id")),
        @AssociationOverride(name = "id.order", joinColumns = @JoinColumn(name = "order_id"))
})
public class OrderItem {
    @EmbeddedId
    private OrderItemPK id = new OrderItemPK();

    private int quantity;

    public OrderItem() {
    }

    public OrderItemPK getId() {
        return id;
    }

    public void setId(OrderItemPK id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Transient
    public Item getItem() {
        return id.getItem();
    }

    public void setItem(Item item) {
        this.id.setItem(item);
    }

    @Transient
    public DeliveryOrder getOrder() {
        return id.getOrder();
    }

    public void setOrder(DeliveryOrder order) {
        this.id.setOrder(order);
    }
}
