package b22.metro2033.Repository;

import b22.metro2033.Entity.Army.MovementSensor;
import b22.metro2033.Entity.Army.SensorMessages;
import b22.metro2033.Entity.Engineering.Request;
import b22.metro2033.Entity.Role;
import b22.metro2033.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorMessagesRepository extends JpaRepository<SensorMessages, Long> {
    Optional<SensorMessages> findById(long id);
    List<SensorMessages> findAllByMovementSensor(MovementSensor movementSensor);
    List<SensorMessages> findAllByMovementSensorId(long id);
}
