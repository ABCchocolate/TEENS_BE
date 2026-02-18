package kusuri12.teens_be.domain.information.repository;

import kusuri12.teens_be.domain.information.domain.Information;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InformationRepository extends JpaRepository<Information, Long> {

    @Query("SELECT i FROM Information i ORDER BY i.pinned DESC, i.createdAt DESC")
    List<Information> findAllOrderByPinnedAndCreatedAt();

    // 제목 또는 내용으로 검색
    @Query("SELECT i FROM Information i WHERE i.title LIKE %:keyword% OR i.content LIKE %:keyword% ORDER BY i.pinned DESC, i.createdAt DESC")
    Page<Information> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<Information> findAllByOrderByCreatedAtDesc(Pageable pageable);
}