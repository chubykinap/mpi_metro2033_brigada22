package b22.metro2033.Repository;

import b22.metro2033.Entity.Army.Characteristics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicsRepository extends JpaRepository<Characteristics, Long> {
    Characteristics findBySoldier_id(long soldier_id);
    Characteristics findById(long soldier_id);

}
