package kusuri12.teens_be.domain.forum.repository;

import kusuri12.teens_be.domain.forum.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.user where c.forum.id = :forumId order by c.createdAt asc")
    Slice<Comment> findByForumIdOrderByCreatedAtAsc(Long forumId, Pageable pageable);

    Long countByForumId(Long id);
}