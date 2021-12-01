package b22.metro2033.Repository.Engineering;

import b22.metro2033.Entity.Engineering.Engineer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EngineerRepository extends JpaRepository<Engineer, Long> {
    Engineer findByLogin(long id);
    List<Engineer> findAll();
}
