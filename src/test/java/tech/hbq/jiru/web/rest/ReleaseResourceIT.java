package tech.hbq.jiru.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tech.hbq.jiru.IntegrationTest;
import tech.hbq.jiru.domain.Release;
import tech.hbq.jiru.domain.enumeration.ReleaseStatus;
import tech.hbq.jiru.repository.ReleaseRepository;
import tech.hbq.jiru.service.dto.ReleaseDTO;
import tech.hbq.jiru.service.mapper.ReleaseMapper;

/**
 * Integration tests for the {@link ReleaseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReleaseResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ReleaseStatus DEFAULT_STATUS = ReleaseStatus.READY_FOR_RELEASE;
    private static final ReleaseStatus UPDATED_STATUS = ReleaseStatus.RELEASED;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long UPDATED_PROJECT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/releases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private ReleaseMapper releaseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReleaseMockMvc;

    private Release release;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Release createEntity(EntityManager em) {
        Release release = new Release()
            .title(DEFAULT_TITLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .note(DEFAULT_NOTE)
            .projectName(DEFAULT_PROJECT_NAME)
            .projectId(DEFAULT_PROJECT_ID);
        return release;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Release createUpdatedEntity(EntityManager em) {
        Release release = new Release()
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .note(UPDATED_NOTE)
            .projectName(UPDATED_PROJECT_NAME)
            .projectId(UPDATED_PROJECT_ID);
        return release;
    }

    @BeforeEach
    public void initTest() {
        release = createEntity(em);
    }

    @Test
    @Transactional
    void createRelease() throws Exception {
        int databaseSizeBeforeCreate = releaseRepository.findAll().size();
        // Create the Release
        ReleaseDTO releaseDTO = releaseMapper.toDto(release);
        restReleaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releaseDTO)))
            .andExpect(status().isCreated());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeCreate + 1);
        Release testRelease = releaseList.get(releaseList.size() - 1);
        assertThat(testRelease.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRelease.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testRelease.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testRelease.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRelease.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRelease.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testRelease.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testRelease.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    void createReleaseWithExistingId() throws Exception {
        // Create the Release with an existing ID
        release.setId(1L);
        ReleaseDTO releaseDTO = releaseMapper.toDto(release);

        int databaseSizeBeforeCreate = releaseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReleaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReleases() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get all the releaseList
        restReleaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(release.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));
    }

    @Test
    @Transactional
    void getRelease() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        // Get the release
        restReleaseMockMvc
            .perform(get(ENTITY_API_URL_ID, release.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(release.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRelease() throws Exception {
        // Get the release
        restReleaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRelease() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();

        // Update the release
        Release updatedRelease = releaseRepository.findById(release.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRelease are not directly saved in db
        em.detach(updatedRelease);
        updatedRelease
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .note(UPDATED_NOTE)
            .projectName(UPDATED_PROJECT_NAME)
            .projectId(UPDATED_PROJECT_ID);
        ReleaseDTO releaseDTO = releaseMapper.toDto(updatedRelease);

        restReleaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, releaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(releaseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);
        Release testRelease = releaseList.get(releaseList.size() - 1);
        assertThat(testRelease.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRelease.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testRelease.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testRelease.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRelease.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRelease.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testRelease.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testRelease.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
    }

    @Test
    @Transactional
    void putNonExistingRelease() throws Exception {
        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();
        release.setId(count.incrementAndGet());

        // Create the Release
        ReleaseDTO releaseDTO = releaseMapper.toDto(release);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, releaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(releaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRelease() throws Exception {
        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();
        release.setId(count.incrementAndGet());

        // Create the Release
        ReleaseDTO releaseDTO = releaseMapper.toDto(release);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(releaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRelease() throws Exception {
        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();
        release.setId(count.incrementAndGet());

        // Create the Release
        ReleaseDTO releaseDTO = releaseMapper.toDto(release);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releaseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReleaseWithPatch() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();

        // Update the release using partial update
        Release partialUpdatedRelease = new Release();
        partialUpdatedRelease.setId(release.getId());

        partialUpdatedRelease
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .projectId(UPDATED_PROJECT_ID);

        restReleaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelease.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelease))
            )
            .andExpect(status().isOk());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);
        Release testRelease = releaseList.get(releaseList.size() - 1);
        assertThat(testRelease.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRelease.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testRelease.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testRelease.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRelease.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRelease.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testRelease.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testRelease.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
    }

    @Test
    @Transactional
    void fullUpdateReleaseWithPatch() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();

        // Update the release using partial update
        Release partialUpdatedRelease = new Release();
        partialUpdatedRelease.setId(release.getId());

        partialUpdatedRelease
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .note(UPDATED_NOTE)
            .projectName(UPDATED_PROJECT_NAME)
            .projectId(UPDATED_PROJECT_ID);

        restReleaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelease.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelease))
            )
            .andExpect(status().isOk());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);
        Release testRelease = releaseList.get(releaseList.size() - 1);
        assertThat(testRelease.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRelease.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testRelease.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testRelease.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRelease.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRelease.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testRelease.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testRelease.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingRelease() throws Exception {
        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();
        release.setId(count.incrementAndGet());

        // Create the Release
        ReleaseDTO releaseDTO = releaseMapper.toDto(release);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, releaseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(releaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRelease() throws Exception {
        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();
        release.setId(count.incrementAndGet());

        // Create the Release
        ReleaseDTO releaseDTO = releaseMapper.toDto(release);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(releaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRelease() throws Exception {
        int databaseSizeBeforeUpdate = releaseRepository.findAll().size();
        release.setId(count.incrementAndGet());

        // Create the Release
        ReleaseDTO releaseDTO = releaseMapper.toDto(release);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleaseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(releaseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Release in the database
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRelease() throws Exception {
        // Initialize the database
        releaseRepository.saveAndFlush(release);

        int databaseSizeBeforeDelete = releaseRepository.findAll().size();

        // Delete the release
        restReleaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, release.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Release> releaseList = releaseRepository.findAll();
        assertThat(releaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
