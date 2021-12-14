package b22.metro2033.Entity.Alerts;

import b22.metro2033.Entity.Army.MovementSensor;
import b22.metro2033.Entity.Army.Rank;
import b22.metro2033.Entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Table(name = "alert_messages")
public class AlertMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String alert_message;

    @Enumerated(EnumType.STRING)
    private TypeOfMessage type_of_message;

    @Column(updatable = false)
    private LocalDateTime alert_message_date;

    @ManyToOne
    @JoinColumn(name = "alert_message_id")
    private User user;

    @PrePersist
    void onCreate() {
        this.setAlert_message_date(LocalDateTime.now());
    }

    public AlertMessages() {
    }

    public AlertMessages(long id, String alert_message, TypeOfMessage type_of_message, LocalDateTime alert_message_date, User user) {
        this.id = id;
        this.alert_message = alert_message;
        this.type_of_message = type_of_message;
        this.alert_message_date = alert_message_date;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlert_message() {
        return alert_message;
    }

    public void setAlert_message(String alert_message) {
        this.alert_message = alert_message;
    }

    public TypeOfMessage getType_of_message() {
        return type_of_message;
    }

    public void setType_of_message(TypeOfMessage type_of_message) {
        this.type_of_message = type_of_message;
    }

    public LocalDateTime getAlert_message_date() {
        return alert_message_date;
    }

    public void setAlert_message_date(LocalDateTime alert_message_date) {
        this.alert_message_date = alert_message_date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

