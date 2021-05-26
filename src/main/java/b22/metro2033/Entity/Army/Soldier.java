package b22.metro2033.Entity.Army;

import b22.metro2033.Entity.User;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "soldier")
public class Soldier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private Rank rank;
    @Enumerated(EnumType.STRING)
    private HealthState health_state;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Valid
    @OneToOne(mappedBy = "soldier")
    private Characteristics characteristics;

    public Soldier() {
    }

    public Soldier(Rank rank, HealthState health_state, User user, Post post, Characteristics characteristics) {
        this.rank = rank;
        this.health_state = health_state;
        this.user = user;
        this.post = post;
        this.characteristics = characteristics;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public HealthState getHealth_state() {
        return health_state;
    }

    public void setHealth_state(HealthState health_state) {
        this.health_state = health_state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Characteristics getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Characteristics characteristics) {
        this.characteristics = characteristics;
    }
}