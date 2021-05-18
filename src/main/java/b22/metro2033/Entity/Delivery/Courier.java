package b22.metro2033.Entity.Delivery;

import b22.metro2033.Entity.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "courier")
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(mappedBy = "courier")
    private User user;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "courier_order",
            joinColumns = { @JoinColumn(name = "courier_id") },
            inverseJoinColumns = { @JoinColumn(name = "order_id") }
    )
    private List<DeliveryOrder> orders;

    public Courier() {
    }

    public Courier(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
