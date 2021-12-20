package b22.metro2033.Repository.Army;

import b22.metro2033.Entity.Army.Post;
import b22.metro2033.Entity.Army.Soldier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SoldierRepository extends JpaRepository<Soldier, Long> {
    List<Soldier> findAll();
    Optional<Soldier> findById(long id);
    Optional<Soldier> findByUserId(long id);

}
