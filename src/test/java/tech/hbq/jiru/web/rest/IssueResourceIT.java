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
import tech.hbq.jiru.domain.Issue;
import tech.hbq.jiru.domain.enumeration.Priority;
import tech.hbq.jiru.repository.IssueRepository;
import tech.hbq.jiru.service.dto.IssueDTO;
import tech.hbq.jiru.service.mapper.IssueMapper;

/**
 * Integration tests for the {@link IssueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IssueResourceIT {

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ESTIMATE_HOUR = 1;
    private static final Integer UPDATED_ESTIMATE_HOUR = 2;

    private static final Integer DEFAULT_ESTIMATE_DAY = 1;
    private static final Integer UPDATED_ESTIMATE_DAY = 2;

    private static final Integer DEFAULT_STORY_POINT = 1;
    private static final Integer UPDATED_STORY_POINT = 2;

    private static final String DEFAULT_ASSIGNEE = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNEE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSIGNEE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNEE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_BLOCKER = "AAAAAAAAAA";
    private static final String UPDATED_BLOCKER = "BBBBBBBBBB";

    private static final String DEFAULT_PULL_REQUEST = "AAAAAAAAAA";
    private static final String UPDATED_PULL_REQUEST = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEEK_NUMBER = 1;
    private static final Integer UPDATED_WEEK_NUMBER = 2;

    private static final String DEFAULT_SPRINT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SPRINT_TITLE = "BBBBBBBBBB";

    private static final Long DEFAULT_SPRINT_ID = 1L;
    private static final Long UPDATED_SPRINT_ID = 2L;

    private static final Priority DEFAULT_PRIOTIRY = Priority.CRITICAL;
    private static final Priority UPDATED_PRIOTIRY = Priority.MAJOR;

    private static final String DEFAULT_RELEASE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_TITLE = "BBBBBBBBBB";

    private static final Long DEFAULT_RELEASE_ID = 1L;
    private static final Long UPDATED_RELEASE_ID = 2L;

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long UPDATED_PROJECT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/issues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIssueMockMvc;

    private Issue issue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Issue createEntity(EntityManager em) {
        Issue issue = new Issue()
            .summary(DEFAULT_SUMMARY)
            .description(DEFAULT_DESCRIPTION)
            .label(DEFAULT_LABEL)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .dueDate(DEFAULT_DUE_DATE)
            .estimateHour(DEFAULT_ESTIMATE_HOUR)
            .estimateDay(DEFAULT_ESTIMATE_DAY)
            .storyPoint(DEFAULT_STORY_POINT)
            .assignee(DEFAULT_ASSIGNEE)
            .assigneeId(DEFAULT_ASSIGNEE_ID)
            .blocker(DEFAULT_BLOCKER)
            .pullRequest(DEFAULT_PULL_REQUEST)
            .weekNumber(DEFAULT_WEEK_NUMBER)
            .sprintTitle(DEFAULT_SPRINT_TITLE)
            .sprintId(DEFAULT_SPRINT_ID)
            .priotiry(DEFAULT_PRIOTIRY)
            .releaseTitle(DEFAULT_RELEASE_TITLE)
            .releaseId(DEFAULT_RELEASE_ID)
            .projectName(DEFAULT_PROJECT_NAME)
            .projectId(DEFAULT_PROJECT_ID);
        return issue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Issue createUpdatedEntity(EntityManager em) {
        Issue issue = new Issue()
            .summary(UPDATED_SUMMARY)
            .description(UPDATED_DESCRIPTION)
            .label(UPDATED_LABEL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .estimateHour(UPDATED_ESTIMATE_HOUR)
            .estimateDay(UPDATED_ESTIMATE_DAY)
            .storyPoint(UPDATED_STORY_POINT)
            .assignee(UPDATED_ASSIGNEE)
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .blocker(UPDATED_BLOCKER)
            .pullRequest(UPDATED_PULL_REQUEST)
            .weekNumber(UPDATED_WEEK_NUMBER)
            .sprintTitle(UPDATED_SPRINT_TITLE)
            .sprintId(UPDATED_SPRINT_ID)
            .priotiry(UPDATED_PRIOTIRY)
            .releaseTitle(UPDATED_RELEASE_TITLE)
            .releaseId(UPDATED_RELEASE_ID)
            .projectName(UPDATED_PROJECT_NAME)
            .projectId(UPDATED_PROJECT_ID);
        return issue;
    }

    @BeforeEach
    public void initTest() {
        issue = createEntity(em);
    }

    @Test
    @Transactional
    void createIssue() throws Exception {
        int databaseSizeBeforeCreate = issueRepository.findAll().size();
        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);
        restIssueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isCreated());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeCreate + 1);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testIssue.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIssue.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testIssue.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testIssue.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testIssue.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testIssue.getEstimateHour()).isEqualTo(DEFAULT_ESTIMATE_HOUR);
        assertThat(testIssue.getEstimateDay()).isEqualTo(DEFAULT_ESTIMATE_DAY);
        assertThat(testIssue.getStoryPoint()).isEqualTo(DEFAULT_STORY_POINT);
        assertThat(testIssue.getAssignee()).isEqualTo(DEFAULT_ASSIGNEE);
        assertThat(testIssue.getAssigneeId()).isEqualTo(DEFAULT_ASSIGNEE_ID);
        assertThat(testIssue.getBlocker()).isEqualTo(DEFAULT_BLOCKER);
        assertThat(testIssue.getPullRequest()).isEqualTo(DEFAULT_PULL_REQUEST);
        assertThat(testIssue.getWeekNumber()).isEqualTo(DEFAULT_WEEK_NUMBER);
        assertThat(testIssue.getSprintTitle()).isEqualTo(DEFAULT_SPRINT_TITLE);
        assertThat(testIssue.getSprintId()).isEqualTo(DEFAULT_SPRINT_ID);
        assertThat(testIssue.getPriotiry()).isEqualTo(DEFAULT_PRIOTIRY);
        assertThat(testIssue.getReleaseTitle()).isEqualTo(DEFAULT_RELEASE_TITLE);
        assertThat(testIssue.getReleaseId()).isEqualTo(DEFAULT_RELEASE_ID);
        assertThat(testIssue.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testIssue.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    void createIssueWithExistingId() throws Exception {
        // Create the Issue with an existing ID
        issue.setId(1L);
        IssueDTO issueDTO = issueMapper.toDto(issue);

        int databaseSizeBeforeCreate = issueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIssues() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList
        restIssueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issue.getId().intValue())))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimateHour").value(hasItem(DEFAULT_ESTIMATE_HOUR)))
            .andExpect(jsonPath("$.[*].estimateDay").value(hasItem(DEFAULT_ESTIMATE_DAY)))
            .andExpect(jsonPath("$.[*].storyPoint").value(hasItem(DEFAULT_STORY_POINT)))
            .andExpect(jsonPath("$.[*].assignee").value(hasItem(DEFAULT_ASSIGNEE)))
            .andExpect(jsonPath("$.[*].assigneeId").value(hasItem(DEFAULT_ASSIGNEE_ID)))
            .andExpect(jsonPath("$.[*].blocker").value(hasItem(DEFAULT_BLOCKER)))
            .andExpect(jsonPath("$.[*].pullRequest").value(hasItem(DEFAULT_PULL_REQUEST)))
            .andExpect(jsonPath("$.[*].weekNumber").value(hasItem(DEFAULT_WEEK_NUMBER)))
            .andExpect(jsonPath("$.[*].sprintTitle").value(hasItem(DEFAULT_SPRINT_TITLE)))
            .andExpect(jsonPath("$.[*].sprintId").value(hasItem(DEFAULT_SPRINT_ID.intValue())))
            .andExpect(jsonPath("$.[*].priotiry").value(hasItem(DEFAULT_PRIOTIRY.toString())))
            .andExpect(jsonPath("$.[*].releaseTitle").value(hasItem(DEFAULT_RELEASE_TITLE)))
            .andExpect(jsonPath("$.[*].releaseId").value(hasItem(DEFAULT_RELEASE_ID.intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));
    }

    @Test
    @Transactional
    void getIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get the issue
        restIssueMockMvc
            .perform(get(ENTITY_API_URL_ID, issue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(issue.getId().intValue()))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.estimateHour").value(DEFAULT_ESTIMATE_HOUR))
            .andExpect(jsonPath("$.estimateDay").value(DEFAULT_ESTIMATE_DAY))
            .andExpect(jsonPath("$.storyPoint").value(DEFAULT_STORY_POINT))
            .andExpect(jsonPath("$.assignee").value(DEFAULT_ASSIGNEE))
            .andExpect(jsonPath("$.assigneeId").value(DEFAULT_ASSIGNEE_ID))
            .andExpect(jsonPath("$.blocker").value(DEFAULT_BLOCKER))
            .andExpect(jsonPath("$.pullRequest").value(DEFAULT_PULL_REQUEST))
            .andExpect(jsonPath("$.weekNumber").value(DEFAULT_WEEK_NUMBER))
            .andExpect(jsonPath("$.sprintTitle").value(DEFAULT_SPRINT_TITLE))
            .andExpect(jsonPath("$.sprintId").value(DEFAULT_SPRINT_ID.intValue()))
            .andExpect(jsonPath("$.priotiry").value(DEFAULT_PRIOTIRY.toString()))
            .andExpect(jsonPath("$.releaseTitle").value(DEFAULT_RELEASE_TITLE))
            .andExpect(jsonPath("$.releaseId").value(DEFAULT_RELEASE_ID.intValue()))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingIssue() throws Exception {
        // Get the issue
        restIssueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        int databaseSizeBeforeUpdate = issueRepository.findAll().size();

        // Update the issue
        Issue updatedIssue = issueRepository.findById(issue.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIssue are not directly saved in db
        em.detach(updatedIssue);
        updatedIssue
            .summary(UPDATED_SUMMARY)
            .description(UPDATED_DESCRIPTION)
            .label(UPDATED_LABEL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .estimateHour(UPDATED_ESTIMATE_HOUR)
            .estimateDay(UPDATED_ESTIMATE_DAY)
            .storyPoint(UPDATED_STORY_POINT)
            .assignee(UPDATED_ASSIGNEE)
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .blocker(UPDATED_BLOCKER)
            .pullRequest(UPDATED_PULL_REQUEST)
            .weekNumber(UPDATED_WEEK_NUMBER)
            .sprintTitle(UPDATED_SPRINT_TITLE)
            .sprintId(UPDATED_SPRINT_ID)
            .priotiry(UPDATED_PRIOTIRY)
            .releaseTitle(UPDATED_RELEASE_TITLE)
            .releaseId(UPDATED_RELEASE_ID)
            .projectName(UPDATED_PROJECT_NAME)
            .projectId(UPDATED_PROJECT_ID);
        IssueDTO issueDTO = issueMapper.toDto(updatedIssue);

        restIssueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, issueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(issueDTO))
            )
            .andExpect(status().isOk());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testIssue.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIssue.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testIssue.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testIssue.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testIssue.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testIssue.getEstimateHour()).isEqualTo(UPDATED_ESTIMATE_HOUR);
        assertThat(testIssue.getEstimateDay()).isEqualTo(UPDATED_ESTIMATE_DAY);
        assertThat(testIssue.getStoryPoint()).isEqualTo(UPDATED_STORY_POINT);
        assertThat(testIssue.getAssignee()).isEqualTo(UPDATED_ASSIGNEE);
        assertThat(testIssue.getAssigneeId()).isEqualTo(UPDATED_ASSIGNEE_ID);
        assertThat(testIssue.getBlocker()).isEqualTo(UPDATED_BLOCKER);
        assertThat(testIssue.getPullRequest()).isEqualTo(UPDATED_PULL_REQUEST);
        assertThat(testIssue.getWeekNumber()).isEqualTo(UPDATED_WEEK_NUMBER);
        assertThat(testIssue.getSprintTitle()).isEqualTo(UPDATED_SPRINT_TITLE);
        assertThat(testIssue.getSprintId()).isEqualTo(UPDATED_SPRINT_ID);
        assertThat(testIssue.getPriotiry()).isEqualTo(UPDATED_PRIOTIRY);
        assertThat(testIssue.getReleaseTitle()).isEqualTo(UPDATED_RELEASE_TITLE);
        assertThat(testIssue.getReleaseId()).isEqualTo(UPDATED_RELEASE_ID);
        assertThat(testIssue.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testIssue.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
    }

    @Test
    @Transactional
    void putNonExistingIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().size();
        issue.setId(count.incrementAndGet());

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIssueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, issueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(issueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().size();
        issue.setId(count.incrementAndGet());

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIssueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(issueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().size();
        issue.setId(count.incrementAndGet());

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIssueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIssueWithPatch() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        int databaseSizeBeforeUpdate = issueRepository.findAll().size();

        // Update the issue using partial update
        Issue partialUpdatedIssue = new Issue();
        partialUpdatedIssue.setId(issue.getId());

        partialUpdatedIssue
            .summary(UPDATED_SUMMARY)
            .description(UPDATED_DESCRIPTION)
            .label(UPDATED_LABEL)
            .endDate(UPDATED_END_DATE)
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .weekNumber(UPDATED_WEEK_NUMBER)
            .sprintTitle(UPDATED_SPRINT_TITLE)
            .sprintId(UPDATED_SPRINT_ID)
            .priotiry(UPDATED_PRIOTIRY)
            .projectId(UPDATED_PROJECT_ID);

        restIssueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIssue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIssue))
            )
            .andExpect(status().isOk());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testIssue.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIssue.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testIssue.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testIssue.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testIssue.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testIssue.getEstimateHour()).isEqualTo(DEFAULT_ESTIMATE_HOUR);
        assertThat(testIssue.getEstimateDay()).isEqualTo(DEFAULT_ESTIMATE_DAY);
        assertThat(testIssue.getStoryPoint()).isEqualTo(DEFAULT_STORY_POINT);
        assertThat(testIssue.getAssignee()).isEqualTo(DEFAULT_ASSIGNEE);
        assertThat(testIssue.getAssigneeId()).isEqualTo(UPDATED_ASSIGNEE_ID);
        assertThat(testIssue.getBlocker()).isEqualTo(DEFAULT_BLOCKER);
        assertThat(testIssue.getPullRequest()).isEqualTo(DEFAULT_PULL_REQUEST);
        assertThat(testIssue.getWeekNumber()).isEqualTo(UPDATED_WEEK_NUMBER);
        assertThat(testIssue.getSprintTitle()).isEqualTo(UPDATED_SPRINT_TITLE);
        assertThat(testIssue.getSprintId()).isEqualTo(UPDATED_SPRINT_ID);
        assertThat(testIssue.getPriotiry()).isEqualTo(UPDATED_PRIOTIRY);
        assertThat(testIssue.getReleaseTitle()).isEqualTo(DEFAULT_RELEASE_TITLE);
        assertThat(testIssue.getReleaseId()).isEqualTo(DEFAULT_RELEASE_ID);
        assertThat(testIssue.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testIssue.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
    }

    @Test
    @Transactional
    void fullUpdateIssueWithPatch() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        int databaseSizeBeforeUpdate = issueRepository.findAll().size();

        // Update the issue using partial update
        Issue partialUpdatedIssue = new Issue();
        partialUpdatedIssue.setId(issue.getId());

        partialUpdatedIssue
            .summary(UPDATED_SUMMARY)
            .description(UPDATED_DESCRIPTION)
            .label(UPDATED_LABEL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .estimateHour(UPDATED_ESTIMATE_HOUR)
            .estimateDay(UPDATED_ESTIMATE_DAY)
            .storyPoint(UPDATED_STORY_POINT)
            .assignee(UPDATED_ASSIGNEE)
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .blocker(UPDATED_BLOCKER)
            .pullRequest(UPDATED_PULL_REQUEST)
            .weekNumber(UPDATED_WEEK_NUMBER)
            .sprintTitle(UPDATED_SPRINT_TITLE)
            .sprintId(UPDATED_SPRINT_ID)
            .priotiry(UPDATED_PRIOTIRY)
            .releaseTitle(UPDATED_RELEASE_TITLE)
            .releaseId(UPDATED_RELEASE_ID)
            .projectName(UPDATED_PROJECT_NAME)
            .projectId(UPDATED_PROJECT_ID);

        restIssueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIssue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIssue))
            )
            .andExpect(status().isOk());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testIssue.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIssue.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testIssue.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testIssue.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testIssue.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testIssue.getEstimateHour()).isEqualTo(UPDATED_ESTIMATE_HOUR);
        assertThat(testIssue.getEstimateDay()).isEqualTo(UPDATED_ESTIMATE_DAY);
        assertThat(testIssue.getStoryPoint()).isEqualTo(UPDATED_STORY_POINT);
        assertThat(testIssue.getAssignee()).isEqualTo(UPDATED_ASSIGNEE);
        assertThat(testIssue.getAssigneeId()).isEqualTo(UPDATED_ASSIGNEE_ID);
        assertThat(testIssue.getBlocker()).isEqualTo(UPDATED_BLOCKER);
        assertThat(testIssue.getPullRequest()).isEqualTo(UPDATED_PULL_REQUEST);
        assertThat(testIssue.getWeekNumber()).isEqualTo(UPDATED_WEEK_NUMBER);
        assertThat(testIssue.getSprintTitle()).isEqualTo(UPDATED_SPRINT_TITLE);
        assertThat(testIssue.getSprintId()).isEqualTo(UPDATED_SPRINT_ID);
        assertThat(testIssue.getPriotiry()).isEqualTo(UPDATED_PRIOTIRY);
        assertThat(testIssue.getReleaseTitle()).isEqualTo(UPDATED_RELEASE_TITLE);
        assertThat(testIssue.getReleaseId()).isEqualTo(UPDATED_RELEASE_ID);
        assertThat(testIssue.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testIssue.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().size();
        issue.setId(count.incrementAndGet());

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIssueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, issueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(issueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().size();
        issue.setId(count.incrementAndGet());

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIssueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(issueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().size();
        issue.setId(count.incrementAndGet());

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIssueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        int databaseSizeBeforeDelete = issueRepository.findAll().size();

        // Delete the issue
        restIssueMockMvc
            .perform(delete(ENTITY_API_URL_ID, issue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
