package kusuri12.teens_be.domain.forum.domain.repository;

import kusuri12.teens_be.domain.forum.domain.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {

    @Query("SELECT f FROM Forum f ORDER BY f.createdAt DESC")
    List<Forum> findAllOrderByCreatedAtDesc();

    Long countByUserId(Long userId);

    int countByUser_Id(Long id);
}