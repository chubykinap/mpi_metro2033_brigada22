package b22.metro2033.Entity.Army;

import b22.metro2033.Entity.Engineering.Request;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "movement_sensor")
public class MovementSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String location;

    @OneToMany(mappedBy = "movementSensor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensorMessages> sensorMessages;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(value = EnumType.STRING)
    private SensorStatus sensorStatus;

    public MovementSensor(){

    }

    public MovementSensor(long id, String name, String location, List<SensorMessages> sensorMessages, Post post, SensorStatus sensorStatus) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.sensorMessages = sensorMessages;
        this.post = post;
        this.sensorStatus = sensorStatus;
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

    public List<SensorMessages> getSensorMessages() {
        return sensorMessages;
    }

    public void setSensorMessages(List<SensorMessages> sensorMessages) {
        this.sensorMessages = sensorMessages;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public SensorStatus getSensorStatus() {
        return sensorStatus;
    }

    public void setSensorStatus(SensorStatus sensorStatus) {
        this.sensorStatus = sensorStatus;
    }
}