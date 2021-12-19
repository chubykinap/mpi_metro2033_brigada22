package b22.metro2033.Repository.Delivery;

import b22.metro2033.Entity.Delivery.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<DeliveryOrder, Long> {
    List<DeliveryOrder> findAll();
    Optional<DeliveryOrder> findById(long id);
    List<DeliveryOrder> findAllByCourierId(long id);
}
