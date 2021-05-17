package b22.metro2033.Entity.Engineering;

import b22.metro2033.Entity.User;

import javax.persistence.*;
import java.util.List;

enum Qualification{
    THIRD,
    SECOND,
    FIRST
}

@Entity
@Table(name = "engineer")
public class Engineer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private Qualification qualification;

    @OneToOne(mappedBy = "soldier")
    private User user;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "engineer_request",
            joinColumns = { @JoinColumn(name = "engineer_id") },
            inverseJoinColumns = { @JoinColumn(name = "request_id") }
    )
    private List<Request> requests;

    public Engineer() {
    }

    public Engineer(int id, Qualification qualification, User user) {
        this.id = id;
        this.qualification = qualification;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
