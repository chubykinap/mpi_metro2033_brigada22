package b22.metro2033.Entity.Army;

import javax.persistence.*;

@Entity
@Table(name = "movement_sensor")
public class MovementSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String location;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public MovementSensor() {
    }

    public MovementSensor(long id, String name, String location, Post post) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.post = post;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
