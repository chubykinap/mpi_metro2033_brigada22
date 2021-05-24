package b22.metro2033.Entity.Delivery;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Embeddable
class StorageItemPK implements Serializable {
    @ManyToOne(cascade = CascadeType.ALL)
    private Storage storage;
    @ManyToOne(cascade = CascadeType.ALL)
    private Item item;

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

@Entity
@Table(name = "item_in_storage")
@AssociationOverrides({
        @AssociationOverride(name = "id.item", joinColumns = @JoinColumn(name = "item_id")),
        @AssociationOverride(name = "id.storage", joinColumns = @JoinColumn(name = "storage_id"))
})
public class StorageItem {
    @EmbeddedId
    private StorageItemPK id = new StorageItemPK();

    private int quantity;

    public StorageItem() {
    }

    public StorageItemPK getId() {
        return id;
    }

    public void setId(StorageItemPK id) {
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
    public Storage getStorage() {
        return id.getStorage();
    }

    public void setStorage(Storage storage) {
        this.id.setStorage(storage);
    }
}
