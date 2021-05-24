package b22.metro2033.Repository.Army;

import b22.metro2033.Entity.Army.Post;
import b22.metro2033.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAll();
    Optional<Post> findById(long id);
}
