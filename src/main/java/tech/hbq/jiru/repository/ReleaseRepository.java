package tech.hbq.jiru.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.hbq.jiru.domain.Release;

/**
 * Spring Data JPA repository for the Release entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long> {}
