package kusuri12.teens_be.domain.comment.exception;

public class ForumNotFoundException extends RuntimeException {
  public ForumNotFoundException(String message) {
    super(message);
  }
}
