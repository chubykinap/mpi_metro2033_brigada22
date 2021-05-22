package b22.metro2033.Entity.Army;

import b22.metro2033.Entity.Engineering.Engineer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sensor_messages")
public class SensorMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String messages;
    private Boolean error;

    @Column(updatable = false)
    private LocalDateTime messages_date;

    @PrePersist
    void onCreate() {
        this.setMessages_date(LocalDateTime.now());
    }

    /*@OneToMany(mappedBy = "messages", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovementSensor> sensors;*/

    @ManyToMany(mappedBy = "sensorMessages")
    private List<MovementSensor> movementSensors;

    public SensorMessages(){

    }

    public SensorMessages(long id, String messages, Boolean error, LocalDateTime messages_date, List<MovementSensor> movementSensors) {
        this.id = id;
        this.messages = messages;
        this.error = error;
        this.messages_date = messages_date;
        this.movementSensors = movementSensors;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public LocalDateTime getMessages_date() {
        return messages_date;
    }

    public void setMessages_date(LocalDateTime messages_date) {
        this.messages_date = messages_date;
    }

    public List<MovementSensor> getMovementSensors() {
        return movementSensors;
    }

    public void setMovementSensors(List<MovementSensor> movementSensors) {
        this.movementSensors = movementSensors;
    }
}
