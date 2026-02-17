package kusuri12.teens_be.domain.auth.repository;

import kusuri12.teens_be.domain.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
