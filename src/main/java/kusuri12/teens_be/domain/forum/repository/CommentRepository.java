package kusuri12.teens_be.domain.comment.repository;

import kusuri12.teens_be.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    int countByUserId(Long userId);

    List<Comment> findByForumIdOrderByCreatedAtAsc(Long forumId);

    Long countByForumId(Long id);
}