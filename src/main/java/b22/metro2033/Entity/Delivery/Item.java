package b22.metro2033.Entity.Delivery;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "Name should not be empty")
    private String name;

    private int quantity_in_storage;

    @OneToMany(mappedBy = "id.item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    public Item(){}

    public Item(String name, int quantity_in_storage){
        this.name = name;
        this.quantity_in_storage = quantity_in_storage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity_in_storage;
    }

    public void setQuantity(int quantity_in_storage) {
        this.quantity_in_storage = quantity_in_storage;
    }
}
