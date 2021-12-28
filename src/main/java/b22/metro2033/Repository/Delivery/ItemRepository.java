package b22.metro2033.Repository.Delivery;

import b22.metro2033.Entity.Delivery.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAll();
    Optional<Item> findByName(String name);
    Item findById(long id);
}
