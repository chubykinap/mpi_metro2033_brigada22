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

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "sensor_message",
            joinColumns = { @JoinColumn(name = "sensor_id") },
            inverseJoinColumns = { @JoinColumn(name = "message_id") }
    )
    private List<SensorMessages> sensorMessages;

    public MovementSensor(){

    }

    public MovementSensor(long id, String name, String location, List<SensorMessages> sensorMessages) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.sensorMessages = sensorMessages;
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
}