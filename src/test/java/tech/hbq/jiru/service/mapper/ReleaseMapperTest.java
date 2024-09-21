package tech.hbq.jiru.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReleaseMapperTest {

    private ReleaseMapper releaseMapper;

    @BeforeEach
    public void setUp() {
        releaseMapper = new ReleaseMapperImpl();
    }
}
