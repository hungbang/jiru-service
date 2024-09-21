package tech.hbq.jiru.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tech.hbq.jiru.domain.Sprint;

/**
 * Spring Data JPA repository for the Sprint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {}
