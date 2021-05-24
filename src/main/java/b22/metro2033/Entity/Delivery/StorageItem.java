package b22.metro2033.Entity.Delivery;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Embeddable
class StorageItemPK implements Serializable {
    @Column(name = "storage_id")
    private long storage_id;

    @Column(name = "item_id")
    private long item_id;
}

@Entity
@Table(name = "item_in_storage")
public class StorageItem {
    @EmbeddedId
    private StorageItemPK id;

    private int quantity;

    @ManyToOne
    @MapsId("item_id")
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @MapsId("storage_id")
    @JoinColumn(name = "storage_id")
    private Storage storage;

    public StorageItem() {
    }
}