package b22.metro2033.Repository.Engineering;

import b22.metro2033.Entity.Engineering.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEngineers_Id(long id);
    List<Request> findAll();
}
