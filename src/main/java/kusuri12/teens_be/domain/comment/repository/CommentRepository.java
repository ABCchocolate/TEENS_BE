package kusuri12.teens_be.domain.comment.repository;

import kusuri12.teens_be.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Long countByForumId(Long forumId);

    Long countByUserId(Long userId);

    List<Comment> findByForumIdOrderByCreatedAtAsc(Long forumId);
}