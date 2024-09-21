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
import tech.hbq.jiru.domain.Sprint;
import tech.hbq.jiru.domain.enumeration.SprintStatus;
import tech.hbq.jiru.repository.SprintRepository;
import tech.hbq.jiru.service.dto.SprintDTO;
import tech.hbq.jiru.service.mapper.SprintMapper;

/**
 * Integration tests for the {@link SprintResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SprintResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final SprintStatus DEFAULT_STATUS = SprintStatus.ACTIVE;
    private static final SprintStatus UPDATED_STATUS = SprintStatus.COMPLETE;

    private static final String DEFAULT_GOAL = "AAAAAAAAAA";
    private static final String UPDATED_GOAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sprints";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private SprintMapper sprintMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSprintMockMvc;

    private Sprint sprint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sprint createEntity(EntityManager em) {
        Sprint sprint = new Sprint()
            .title(DEFAULT_TITLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .goal(DEFAULT_GOAL);
        return sprint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sprint createUpdatedEntity(EntityManager em) {
        Sprint sprint = new Sprint()
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .goal(UPDATED_GOAL);
        return sprint;
    }

    @BeforeEach
    public void initTest() {
        sprint = createEntity(em);
    }

    @Test
    @Transactional
    void createSprint() throws Exception {
        int databaseSizeBeforeCreate = sprintRepository.findAll().size();
        // Create the Sprint
        SprintDTO sprintDTO = sprintMapper.toDto(sprint);
        restSprintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isCreated());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeCreate + 1);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSprint.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSprint.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSprint.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSprint.getGoal()).isEqualTo(DEFAULT_GOAL);
    }

    @Test
    @Transactional
    void createSprintWithExistingId() throws Exception {
        // Create the Sprint with an existing ID
        sprint.setId(1L);
        SprintDTO sprintDTO = sprintMapper.toDto(sprint);

        int databaseSizeBeforeCreate = sprintRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSprintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSprints() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList
        restSprintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].goal").value(hasItem(DEFAULT_GOAL)));
    }

    @Test
    @Transactional
    void getSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get the sprint
        restSprintMockMvc
            .perform(get(ENTITY_API_URL_ID, sprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sprint.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.goal").value(DEFAULT_GOAL));
    }

    @Test
    @Transactional
    void getNonExistingSprint() throws Exception {
        // Get the sprint
        restSprintMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Update the sprint
        Sprint updatedSprint = sprintRepository.findById(sprint.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSprint are not directly saved in db
        em.detach(updatedSprint);
        updatedSprint
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .goal(UPDATED_GOAL);
        SprintDTO sprintDTO = sprintMapper.toDto(updatedSprint);

        restSprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sprintDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sprintDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSprint.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSprint.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSprint.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSprint.getGoal()).isEqualTo(UPDATED_GOAL);
    }

    @Test
    @Transactional
    void putNonExistingSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
        sprint.setId(count.incrementAndGet());

        // Create the Sprint
        SprintDTO sprintDTO = sprintMapper.toDto(sprint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sprintDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
        sprint.setId(count.incrementAndGet());

        // Create the Sprint
        SprintDTO sprintDTO = sprintMapper.toDto(sprint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
        sprint.setId(count.incrementAndGet());

        // Create the Sprint
        SprintDTO sprintDTO = sprintMapper.toDto(sprint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSprintMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sprintDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSprintWithPatch() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Update the sprint using partial update
        Sprint partialUpdatedSprint = new Sprint();
        partialUpdatedSprint.setId(sprint.getId());

        partialUpdatedSprint.goal(UPDATED_GOAL);

        restSprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSprint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSprint))
            )
            .andExpect(status().isOk());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSprint.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSprint.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSprint.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSprint.getGoal()).isEqualTo(UPDATED_GOAL);
    }

    @Test
    @Transactional
    void fullUpdateSprintWithPatch() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Update the sprint using partial update
        Sprint partialUpdatedSprint = new Sprint();
        partialUpdatedSprint.setId(sprint.getId());

        partialUpdatedSprint
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .goal(UPDATED_GOAL);

        restSprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSprint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSprint))
            )
            .andExpect(status().isOk());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSprint.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSprint.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSprint.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSprint.getGoal()).isEqualTo(UPDATED_GOAL);
    }

    @Test
    @Transactional
    void patchNonExistingSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
        sprint.setId(count.incrementAndGet());

        // Create the Sprint
        SprintDTO sprintDTO = sprintMapper.toDto(sprint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sprintDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
        sprint.setId(count.incrementAndGet());

        // Create the Sprint
        SprintDTO sprintDTO = sprintMapper.toDto(sprint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
        sprint.setId(count.incrementAndGet());

        // Create the Sprint
        SprintDTO sprintDTO = sprintMapper.toDto(sprint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSprintMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sprintDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        int databaseSizeBeforeDelete = sprintRepository.findAll().size();

        // Delete the sprint
        restSprintMockMvc
            .perform(delete(ENTITY_API_URL_ID, sprint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
