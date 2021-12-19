package b22.metro2033.Repository.Army;

import b22.metro2033.Entity.Army.MovementSensor;
import b22.metro2033.Entity.Army.Post;
import b22.metro2033.Entity.Army.SensorMessages;
import b22.metro2033.Entity.Army.SensorStatus;
import b22.metro2033.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovementSensorRepository extends JpaRepository<MovementSensor, Long> {
    Optional<MovementSensor> findById(long id);
    List<MovementSensor> findAllByPost(Post post);
    List<MovementSensor> findAllByPostId(long id);
    List<MovementSensor> findAllBySensorStatus(SensorStatus sensorStatus);

    @Query(value = "SELECT * FROM movement_sensor s LEFT JOIN sensor_messages m ON m.message_id = s.id " +
            "WHERE m.error IS True", nativeQuery = true)
    List<MovementSensor> findSensorsWithErrors();
    List<MovementSensor> findAllByPostIsNotNull();
    List<MovementSensor> findAllByPostIsNull();
}
