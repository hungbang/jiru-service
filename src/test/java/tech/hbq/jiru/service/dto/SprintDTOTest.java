package tech.hbq.jiru.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tech.hbq.jiru.web.rest.TestUtil;

class SprintDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SprintDTO.class);
        SprintDTO sprintDTO1 = new SprintDTO();
        sprintDTO1.setId(1L);
        SprintDTO sprintDTO2 = new SprintDTO();
        assertThat(sprintDTO1).isNotEqualTo(sprintDTO2);
        sprintDTO2.setId(sprintDTO1.getId());
        assertThat(sprintDTO1).isEqualTo(sprintDTO2);
        sprintDTO2.setId(2L);
        assertThat(sprintDTO1).isNotEqualTo(sprintDTO2);
        sprintDTO1.setId(null);
        assertThat(sprintDTO1).isNotEqualTo(sprintDTO2);
    }
}
