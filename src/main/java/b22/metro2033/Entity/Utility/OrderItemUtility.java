package b22.metro2033.Entity.Utility;

import b22.metro2033.Entity.Delivery.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemUtility {
    private String item_name;
    private int quantity;

    public OrderItemUtility(String item_name, int quantity) {
        this.item_name = item_name;
        this.quantity = quantity;
    }

    public OrderItemUtility(OrderItem item){
        this.item_name = item.getItem().getName();
        this.quantity = item.getQuantity();
    }

    static public List<OrderItemUtility> toUtility(List<OrderItem> items){
        List<OrderItemUtility> res = new ArrayList<>();
        for (OrderItem item : items)
            res.add(new OrderItemUtility(item));
        return res;
    }

    public String getItem() {
        return item_name;
    }

    public void setItem(String item) {
        this.item_name = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
