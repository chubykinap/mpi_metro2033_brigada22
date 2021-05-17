package b22.metro2033.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "curier")
public class Curier {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String login;

    @OneToOne(mappedBy = "metro_user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> userList;

    @OneToMany(mappedBy = "curier_order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurierOrder> curierOrderList;


    public Curier() {
    }

    public Curier(int id, String login) {
        this.id = id;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
