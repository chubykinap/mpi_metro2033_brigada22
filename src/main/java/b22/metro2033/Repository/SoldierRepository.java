package b22.metro2033.Repository;

import b22.metro2033.Entity.Army.Soldier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoldierRepository extends JpaRepository<Soldier, Long> {
    List<Soldier> findAll();
    List<Soldier> findAllByPost_id(long post_id);
}
