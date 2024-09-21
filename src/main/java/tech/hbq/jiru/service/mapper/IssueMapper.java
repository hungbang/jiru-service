package tech.hbq.jiru.service.mapper;

import org.mapstruct.*;
import tech.hbq.jiru.domain.Issue;
import tech.hbq.jiru.service.dto.IssueDTO;

/**
 * Mapper for the entity {@link Issue} and its DTO {@link IssueDTO}.
 */
@Mapper(componentModel = "spring")
public interface IssueMapper extends EntityMapper<IssueDTO, Issue> {}
