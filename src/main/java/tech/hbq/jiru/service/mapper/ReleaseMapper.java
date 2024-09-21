package tech.hbq.jiru.service.mapper;

import org.mapstruct.*;
import tech.hbq.jiru.domain.Release;
import tech.hbq.jiru.service.dto.ReleaseDTO;

/**
 * Mapper for the entity {@link Release} and its DTO {@link ReleaseDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReleaseMapper extends EntityMapper<ReleaseDTO, Release> {}
