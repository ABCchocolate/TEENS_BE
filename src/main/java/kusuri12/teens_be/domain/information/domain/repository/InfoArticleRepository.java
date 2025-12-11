package kusuri12.teens_be.domain.information.domain.repository;

import kusuri12.teens_be.domain.information.domain.InfoArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoArticleRepository extends JpaRepository<InfoArticle, Long> {

    @Query("SELECT i FROM InfoArticle i ORDER BY i.pinned DESC, i.createdAt DESC")
    List<InfoArticle> findAllOrderByPinnedAndCreatedAt();
}