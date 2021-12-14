package b22.metro2033.Repository.Delivery;

import b22.metro2033.Entity.Delivery.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    List<Courier> findAll();
    Optional<Courier> findById(long id);
    List<Courier> findAllByOrdersId(long id);
}
