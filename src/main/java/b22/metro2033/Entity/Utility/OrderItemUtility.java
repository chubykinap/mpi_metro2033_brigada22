package b22.metro2033.Entity.Utility;

import b22.metro2033.Entity.Delivery.Item;

public class OrderItemUtility {
    private String item_name;
    private int quantity;

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
