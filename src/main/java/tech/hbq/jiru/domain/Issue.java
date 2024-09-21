package tech.hbq.jiru.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tech.hbq.jiru.domain.enumeration.Priority;

/**
 * A Issue.
 */
@Entity
@Table(name = "issue")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "summary")
    private String summary;

    @Column(name = "description")
    private String description;

    @Column(name = "label")
    private String label;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "estimate_hour")
    private Integer estimateHour;

    @Column(name = "estimate_day")
    private Integer estimateDay;

    @Column(name = "story_point")
    private Integer storyPoint;

    @Column(name = "assignee")
    private String assignee;

    @Column(name = "assignee_id")
    private String assigneeId;

    @Column(name = "blocker")
    private String blocker;

    @Column(name = "pull_request")
    private String pullRequest;

    @Column(name = "week_number")
    private Integer weekNumber;

    @Column(name = "sprint_title")
    private String sprintTitle;

    @Column(name = "sprint_id")
    private Long sprintId;

    @Enumerated(EnumType.STRING)
    @Column(name = "priotiry")
    private Priority priotiry;

    @Column(name = "release_title")
    private String releaseTitle;

    @Column(name = "release_id")
    private Long releaseId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_id")
    private Long projectId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Issue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return this.summary;
    }

    public Issue summary(String summary) {
        this.setSummary(summary);
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return this.description;
    }

    public Issue description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return this.label;
    }

    public Issue label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Issue startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Issue endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Instant getDueDate() {
        return this.dueDate;
    }

    public Issue dueDate(Instant dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getEstimateHour() {
        return this.estimateHour;
    }

    public Issue estimateHour(Integer estimateHour) {
        this.setEstimateHour(estimateHour);
        return this;
    }

    public void setEstimateHour(Integer estimateHour) {
        this.estimateHour = estimateHour;
    }

    public Integer getEstimateDay() {
        return this.estimateDay;
    }

    public Issue estimateDay(Integer estimateDay) {
        this.setEstimateDay(estimateDay);
        return this;
    }

    public void setEstimateDay(Integer estimateDay) {
        this.estimateDay = estimateDay;
    }

    public Integer getStoryPoint() {
        return this.storyPoint;
    }

    public Issue storyPoint(Integer storyPoint) {
        this.setStoryPoint(storyPoint);
        return this;
    }

    public void setStoryPoint(Integer storyPoint) {
        this.storyPoint = storyPoint;
    }

    public String getAssignee() {
        return this.assignee;
    }

    public Issue assignee(String assignee) {
        this.setAssignee(assignee);
        return this;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAssigneeId() {
        return this.assigneeId;
    }

    public Issue assigneeId(String assigneeId) {
        this.setAssigneeId(assigneeId);
        return this;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getBlocker() {
        return this.blocker;
    }

    public Issue blocker(String blocker) {
        this.setBlocker(blocker);
        return this;
    }

    public void setBlocker(String blocker) {
        this.blocker = blocker;
    }

    public String getPullRequest() {
        return this.pullRequest;
    }

    public Issue pullRequest(String pullRequest) {
        this.setPullRequest(pullRequest);
        return this;
    }

    public void setPullRequest(String pullRequest) {
        this.pullRequest = pullRequest;
    }

    public Integer getWeekNumber() {
        return this.weekNumber;
    }

    public Issue weekNumber(Integer weekNumber) {
        this.setWeekNumber(weekNumber);
        return this;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getSprintTitle() {
        return this.sprintTitle;
    }

    public Issue sprintTitle(String sprintTitle) {
        this.setSprintTitle(sprintTitle);
        return this;
    }

    public void setSprintTitle(String sprintTitle) {
        this.sprintTitle = sprintTitle;
    }

    public Long getSprintId() {
        return this.sprintId;
    }

    public Issue sprintId(Long sprintId) {
        this.setSprintId(sprintId);
        return this;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    public Priority getPriotiry() {
        return this.priotiry;
    }

    public Issue priotiry(Priority priotiry) {
        this.setPriotiry(priotiry);
        return this;
    }

    public void setPriotiry(Priority priotiry) {
        this.priotiry = priotiry;
    }

    public String getReleaseTitle() {
        return this.releaseTitle;
    }

    public Issue releaseTitle(String releaseTitle) {
        this.setReleaseTitle(releaseTitle);
        return this;
    }

    public void setReleaseTitle(String releaseTitle) {
        this.releaseTitle = releaseTitle;
    }

    public Long getReleaseId() {
        return this.releaseId;
    }

    public Issue releaseId(Long releaseId) {
        this.setReleaseId(releaseId);
        return this;
    }

    public void setReleaseId(Long releaseId) {
        this.releaseId = releaseId;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public Issue projectName(String projectName) {
        this.setProjectName(projectName);
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public Issue projectId(Long projectId) {
        this.setProjectId(projectId);
        return this;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Issue)) {
            return false;
        }
        return id != null && id.equals(((Issue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Issue{" +
            "id=" + getId() +
            ", summary='" + getSummary() + "'" +
            ", description='" + getDescription() + "'" +
            ", label='" + getLabel() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", estimateHour=" + getEstimateHour() +
            ", estimateDay=" + getEstimateDay() +
            ", storyPoint=" + getStoryPoint() +
            ", assignee='" + getAssignee() + "'" +
            ", assigneeId='" + getAssigneeId() + "'" +
            ", blocker='" + getBlocker() + "'" +
            ", pullRequest='" + getPullRequest() + "'" +
            ", weekNumber=" + getWeekNumber() +
            ", sprintTitle='" + getSprintTitle() + "'" +
            ", sprintId=" + getSprintId() +
            ", priotiry='" + getPriotiry() + "'" +
            ", releaseTitle='" + getReleaseTitle() + "'" +
            ", releaseId=" + getReleaseId() +
            ", projectName='" + getProjectName() + "'" +
            ", projectId=" + getProjectId() +
            "}";
    }
}
