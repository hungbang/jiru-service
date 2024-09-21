package tech.hbq.jiru.service.mapper;

import org.mapstruct.*;
import tech.hbq.jiru.domain.Sprint;
import tech.hbq.jiru.service.dto.SprintDTO;

/**
 * Mapper for the entity {@link Sprint} and its DTO {@link SprintDTO}.
 */
@Mapper(componentModel = "spring")
public interface SprintMapper extends EntityMapper<SprintDTO, Sprint> {}
