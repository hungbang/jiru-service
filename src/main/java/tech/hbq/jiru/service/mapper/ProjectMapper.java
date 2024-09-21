package tech.hbq.jiru.service.mapper;

import org.mapstruct.*;
import tech.hbq.jiru.domain.Project;
import tech.hbq.jiru.service.dto.ProjectDTO;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {}
