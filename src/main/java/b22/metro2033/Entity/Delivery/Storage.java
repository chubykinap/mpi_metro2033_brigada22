package b22.metro2033.Entity.Delivery;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "storage")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "Name should not be empty")
    private String name;
    @NotNull(message = "Location should not be empty")
    private String location;

    @OneToMany(mappedBy = "id.storage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageItem> storageItemList;

    public Storage(){};

    public Storage(String name, String location){
        this.name= name;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
