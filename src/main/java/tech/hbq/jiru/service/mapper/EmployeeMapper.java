package tech.hbq.jiru.service.mapper;

import org.mapstruct.*;
import tech.hbq.jiru.domain.Employee;
import tech.hbq.jiru.service.dto.EmployeeDTO;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {}
