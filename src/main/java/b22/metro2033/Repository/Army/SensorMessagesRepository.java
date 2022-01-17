package b22.metro2033.Repository.Army;

import b22.metro2033.Entity.Army.MovementSensor;
import b22.metro2033.Entity.Army.SensorMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorMessagesRepository extends JpaRepository<SensorMessages, Long> {
    Optional<SensorMessages> findById(long id);
    List<SensorMessages> findAllByMovementSensor(MovementSensor movementSensor);
    List<SensorMessages> findAllByMovementSensorId(long id);
    List<SensorMessages> findByMovementSensorOrderByIdDesc(MovementSensor movementSensor);
}
