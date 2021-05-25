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

    @OneToMany(mappedBy = "id.item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    @OneToMany(mappedBy = "id.item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageItem> storageItemList;

    public Item(){}

    public Item(String name){
        this.name = name;
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
}
