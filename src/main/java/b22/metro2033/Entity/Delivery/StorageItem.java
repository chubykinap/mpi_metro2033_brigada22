package b22.metro2033.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "item_in_storage")
public class StorageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;
    private int storageId;
    private String quantity;
    //private String login;

//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Curier> curierList;

    @ManyToOne(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> itemList;

    @ManyToOne(mappedBy = "storage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Storage> storageList;

    public StorageItem() {
    }

    public StorageItem(int itemId, int storageId, String quantity) {
        this.itemId = itemId;
        this.storageId = storageId;
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getStorageId() {
        return storageId;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
