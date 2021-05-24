package b22.metro2033.Repository;

import b22.metro2033.Entity.Army.Characteristics;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    List<User> findAllByRoleIn(List<Role> roles);
    Optional<User> findById(long id);
}
