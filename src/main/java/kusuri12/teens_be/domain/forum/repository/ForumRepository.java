package kusuri12.teens_be.domain.forum.repository;

import kusuri12.teens_be.domain.forum.domain.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {

    @Query("SELECT f FROM Forum f ORDER BY f.createdAt DESC")
    List<Forum> findAllOrderByCreatedAtDesc();

    // 제목 또는 내용으로 검색
    @Query("SELECT f FROM Forum f WHERE f.title LIKE %:keyword% OR f.content LIKE %:keyword% ORDER BY f.createdAt DESC")
    List<Forum> searchByKeyword(@Param("keyword") String keyword);

    Long countByUserId(Long userId);

    int countByUser_Id(Long id);
}