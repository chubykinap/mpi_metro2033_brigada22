package b22.metro2033.Entity.Army;

import b22.metro2033.Entity.User;

import javax.persistence.*;

enum Rank {
    CADET,
    LIEUTENANT,
    CAPTAIN,
    MAJOR
}

@Entity
@Table(name = "soldier")
public class Soldier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private Rank rank;
    private String health_state;

    @OneToOne(mappedBy = "soldier")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "characteristics_id")
    private Characteristics characteristics;

    public Soldier() {
    }

    public Soldier(int id, Rank rank, String health_state, User user, Post post, Characteristics characteristics) {
        this.id = id;
        this.rank = rank;
        this.health_state = health_state;
        this.user = user;
        this.post = post;
        this.characteristics = characteristics;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getHealth_state() {
        return health_state;
    }

    public void setHealth_state(String health_state) {
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
