package b22.metro2033.Repository.Delivery;

import b22.metro2033.Entity.Delivery.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAll();
    Item findByName(String name);
}
