package b22.metro2033.Repository.Delivery;

import b22.metro2033.Entity.Delivery.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByIdOrderId(long id);
    List<OrderItem> findAllByIdItemId(long id);
}
