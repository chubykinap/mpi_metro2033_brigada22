package b22.metro2033.Entity;

import javax.persistence.*;

@Entity
@Table
public class Soldier {
    private int id;
    private String rank;
    private String health_state;

    @OneToOne(mappedBy = "login")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id")
    private Post post;

    public Soldier() {
    }

    public Soldier(int id, String rank, String health_state) {
        this.id = id;
        this.rank = rank;
        this.health_state = health_state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getHealth_state() {
        return health_state;
    }

    public void setHealth_state(String health_state) {
        this.health_state = health_state;
    }
}
