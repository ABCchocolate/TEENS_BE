package kusuri12.teens_be.domain.forum.repository;

import kusuri12.teens_be.domain.forum.domain.Forum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ForumRepository extends JpaRepository<Forum, Long> {

    @Query("SELECT f FROM Forum f JOIN FETCH f.user ORDER BY f.createdAt DESC")
    Page<Forum> findAllOrderByCreatedAtDesc(Pageable pageable);

    // 제목 또는 내용으로 검색
    @Query(value = "SELECT f FROM Forum f JOIN FETCH f.user WHERE f.title LIKE %:keyword% OR f.content LIKE %:keyword% ORDER BY f.createdAt DESC",
            countQuery = "SELECT COUNT(f) FROM Forum f WHERE f.title LIKE %:keyword% OR f.content LIKE %:keyword%")
    Page<Forum> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}