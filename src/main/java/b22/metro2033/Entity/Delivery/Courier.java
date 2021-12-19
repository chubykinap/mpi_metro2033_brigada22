package b22.metro2033.Entity.Delivery;

import b22.metro2033.Entity.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "courier")
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private boolean working;

    @OneToOne
    @JoinColumn(name = "order_id")
    private DeliveryOrder order;

    public Courier() {
    }

    public Courier(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DeliveryOrder getOrder() {
        return order;
    }

    public void setOrder(DeliveryOrder order) {
        this.order = order;
    }
}