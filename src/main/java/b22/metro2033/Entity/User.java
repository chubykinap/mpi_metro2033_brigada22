package b22.metro2033.Entity;

import b22.metro2033.Entity.Alerts.AlertMessages;
import b22.metro2033.Entity.Army.Soldier;
import b22.metro2033.Entity.Delivery.Courier;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@Table(name = "metro_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotEmpty(message = "Логин не должен быть пустым")
    private String login;

    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;

    @NotEmpty(message = "Имя не должен быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно иметь от 2 до 100 символов и содержать только русские буквы")
    private String name;

    @NotEmpty(message = "Фамилия не должен быть пустым")
    @Size(min = 2, max = 100, message = "Фамилия должна иметь от 2 до 100 символов и содержать только русские буквы")
    private String surname;

    @NotEmpty(message = "Отчество не должен быть пустым")
    @Size(min = 2, max = 100, message = "Отчество должно иметь от 2 до 100 символов и содержать только русские буквы")
    private String patronymic;

    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "Необходимо выбрать роль пользователя")
    private Role role;

    @OneToOne(mappedBy = "user")
    private Soldier soldier;

    @OneToOne(mappedBy = "user")
    private Courier courier;

    private boolean enabled = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlertMessages> alertMessages;

    public List<AlertMessages> getAlertMessages() {
        return alertMessages;
    }

    public void setAlertMessages(List<AlertMessages> alertMessages) {
        this.alertMessages = alertMessages;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Soldier getSoldier() {
        return soldier;
    }

    public void setSoldier(Soldier soldier) {
        this.soldier = soldier;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public User(int id, String login, String password, String name, String surname, String patronymic, Role role, Soldier soldier, Courier courier, boolean enabled) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.role = role;
        this.soldier = soldier;
        this.courier = courier;
        this.enabled = enabled;
    }

    public User(){}
}