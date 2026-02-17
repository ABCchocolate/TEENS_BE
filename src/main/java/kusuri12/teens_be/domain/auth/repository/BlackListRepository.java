package kusuri12.teens_be.domain.auth.repository;

import kusuri12.teens_be.domain.auth.domain.BlackList;
import org.springframework.data.repository.CrudRepository;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
    boolean existsByUsername(String username);
}
