package b22.metro2033.Repository;

import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    List<User> findAllByRoleIn(List<Role> roles);
    Optional<User> findById(long id);

    @Query(value = "SELECT * FROM metro_user u LEFT JOIN soldier s ON s.user_id = u.id " +
            "WHERE u.role = 'SOLDIER' AND s.user_id IS NULL", nativeQuery = true)
    List<User> findFreeSoldiers();

    @Query(value = "SELECT * FROM metro_user u LEFT JOIN courier c ON c.user_id = u.id " +
            "WHERE u.role = 'COURIER' AND c.user_id IS NULL", nativeQuery = true)
    List<User> findFreeCourier();
}
