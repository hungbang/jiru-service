package tech.hbq.jiru.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.hbq.jiru.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
