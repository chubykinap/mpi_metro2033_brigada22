package b22.metro2033.Repository;

import b22.metro2033.Entity.Army.MovementSensor;
import b22.metro2033.Entity.Army.Post;
import b22.metro2033.Entity.Army.SensorMessages;
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

}
