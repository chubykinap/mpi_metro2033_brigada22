package b22.metro2033.Repository;

import b22.metro2033.Entity.Army.Characteristics;
import b22.metro2033.Entity.Army.Soldier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacteristicsRepository extends JpaRepository<Characteristics, Long> {
    Optional<Characteristics> findBySoldier_id(long soldier_id);
    Characteristics findById(long soldier_id);
}
