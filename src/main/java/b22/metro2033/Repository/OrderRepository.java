package b22.metro2033.Repository;

import b22.metro2033.Entity.Delivery.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<DeliveryOrder, Long> {
    List<DeliveryOrder> findAll();
}
