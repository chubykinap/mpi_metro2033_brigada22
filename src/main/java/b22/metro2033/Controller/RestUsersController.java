package b22.metro2033.Controller;

import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import b22.metro2033.Repository.UserRepository;
import b22.metro2033.domain.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestUsersController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RestUsersController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('users:create')")
    public Response create(@RequestBody String request) throws Exception {
        JSONObject JSON = new JSONObject(request);

        String login = JSON.getString("login");
        String name = JSON.getString("name");
        String surname = JSON.getString("surname");
        String patronymic = JSON.getString("patronymic");
        String password = JSON.getString("password");
        Role role = Role.findState(JSON.getString("role"));

        if (userRepository.findByLogin(login).isPresent()){
            return new Response("Error", "Пользователь с таким логином уже существует!");
        }

        User user = new User();
        user.setLogin(login);
        user.setName(name);
        user.setSurname(surname);
        user.setPatronymic(patronymic);
        user.setRole(role);

        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
        return new Response("Done", login);
    }

    @PreAuthorize("hasAuthority('users:write')")
    @RequestMapping(value = "/change", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public  Response change(@RequestBody String response) throws Exception { //ParesException type?

        JSONObject json = new JSONObject(response);

        boolean change_pass = true;

        long user_id = Long.parseLong(json.getString("user_id"));
        String login = json.getString("login");

        String password = json.getString("password");
        if (password.equals("")) change_pass = false;

        String encoded_password = passwordEncoder.encode(password);
        String name = json.getString("name");
        String surname = json.getString("surname");
        String patronymic = json.getString("patronymic");
        Role role = Role.findState(json.getString("role"));

        //Переделать в 1 запрос (хз как)
        User user = userRepository.findById(user_id).orElse(null);
        if(user == null) {
            return new Response("Error", "Пользователь не найден!");
        }

        if ((role == Role.SOLDIER && user.getCourier() != null) ||
                (role == Role.COURIER &&user.getSoldier() != null)){
            return new Response("Error", "Нельзя изменить пользователя, пока он выполняет свою роль");
        }

        if (!Objects.equals(login, user.getLogin())){
            if (userRepository.findByLogin(login).isPresent()){
                return new Response("Error", "Пользователь с таким логином уже существует!");
            }
        }

        if(change_pass){
            user.setPassword(encoded_password);
        }

        user.setLogin(login);
        user.setName(name);
        user.setSurname(surname);
        user.setPatronymic(patronymic);
        user.setRole(role);

        userRepository.save(user);

        return new Response("Done", login);
    }

    @PostMapping("/delete")
    public Response deleteUser(@RequestBody String request) throws IOException, JSONException {

        JSONObject JSON = new JSONObject(request);
        Long id = Long.parseLong(JSON.getString("user_id"));

        User user = userRepository.findById(id).orElse(null);
        String login;
        if(user != null){
            login = user.getLogin();
            if (user.getSoldier() != null ||
                    user.getCourier() != null){
                return new Response("Error", "Нельзя удалить пользавателя когда он назначен");
            }

            userRepository.deleteById(id);

            return new Response("Done", login);

        }else{
            return new Response("Error", "Пользователь не найден");
        }
    }

}

