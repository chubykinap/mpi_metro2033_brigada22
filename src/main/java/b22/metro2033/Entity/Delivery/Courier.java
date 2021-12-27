package b22.metro2033.Entity.Delivery;

import b22.metro2033.Entity.User;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "courier")
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Nullable
    @OneToOne(cascade = CascadeType.ALL)
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