package b22.metro2033.Repository.Delivery;

import b22.metro2033.Entity.Delivery.Courier;
import b22.metro2033.Entity.Delivery.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    List<Courier> findAll();
    Optional<Courier> findById(long id);
    Courier findByOrderId(long id);
    List<Courier> findAllByOrder(DeliveryOrder order);
    Optional<Courier> findByUserId(long id);
}
