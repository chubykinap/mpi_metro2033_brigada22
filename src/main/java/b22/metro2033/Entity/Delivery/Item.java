package b22.metro2033.Entity.Delivery;

import javax.persistence.*;
import javax.validation.constraints.Min;
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
    @Min(value = 0,message = "value should not be less than 0")
    public int quantity_in_storage;
    @Min(value = 0,message = "value should not be less than 0")
    public int weight;

    @OneToMany(mappedBy = "id.item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    public Item(){}

    public Item(String name, int quantity_in_storage, int weight){
        this.name = name;
        this.quantity_in_storage = quantity_in_storage;
        this.weight = weight;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
