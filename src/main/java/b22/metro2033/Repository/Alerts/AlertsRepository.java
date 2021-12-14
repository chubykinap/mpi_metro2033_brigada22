package b22.metro2033.Repository.Alerts;

import b22.metro2033.Entity.Alerts.AlertMessages;
import b22.metro2033.Entity.Army.Characteristics;
import b22.metro2033.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlertsRepository extends JpaRepository<AlertMessages, Long> {
    Optional<AlertMessages> findByUser(User user);
}
