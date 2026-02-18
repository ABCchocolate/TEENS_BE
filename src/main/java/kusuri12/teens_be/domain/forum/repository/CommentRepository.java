package kusuri12.teens_be.domain.forum.repository;

import kusuri12.teens_be.domain.forum.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Slice<Comment> findByForumIdOrderByCreatedAtAsc(Long forumId, Pageable pageable);

    Long countByForumId(Long id);
}