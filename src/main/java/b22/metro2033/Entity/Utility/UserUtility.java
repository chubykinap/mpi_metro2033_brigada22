package b22.metro2033.Entity.Utility;

import b22.metro2033.Entity.Army.Soldier;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserUtility {
    private long id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String patronymic;
    private Role role;

    public UserUtility() {
    }

    public UserUtility(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.patronymic = user.getPatronymic();
        this.role = user.getRole();
    }

    public static List<UserUtility> toUserUtility(List<User> users) {
        List<UserUtility> res = new ArrayList<>();
        for (User user : users) {
            res.add(new UserUtility(user));
        }
        return res;
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
}
