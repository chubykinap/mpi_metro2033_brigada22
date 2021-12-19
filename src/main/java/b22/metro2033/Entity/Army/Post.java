package b22.metro2033.Entity.Army;

import b22.metro2033.Entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table(name = "security_post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @NotEmpty(message = "Location should not be empty")
    private String location;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Soldier> soldier;

    /*@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovementSensor> sensors;*/

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovementSensor> movementSensors;

    /*@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "post_sensor",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "sensor_id") }
    )
    private List<SensorMessages> sensorMessages;*/

    public Post() {
    }

    public Post(long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
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

    public List<Soldier> getSoldier() {
        return soldier;
    }

    public void setSoldier(List<Soldier> soldier) {
        this.soldier = soldier;
    }

    public List<MovementSensor> getMovementSensors() {
        return movementSensors;
    }

    public void setMovementSensors(List<MovementSensor> movementSensors) {
        this.movementSensors = movementSensors;
    }
}