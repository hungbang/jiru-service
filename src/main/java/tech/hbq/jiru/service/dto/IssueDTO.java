package tech.hbq.jiru.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import tech.hbq.jiru.domain.enumeration.Priority;

/**
 * A DTO for the {@link tech.hbq.jiru.domain.Issue} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IssueDTO implements Serializable {

    private Long id;

    private String summary;

    private String description;

    private String label;

    private Instant startDate;

    private Instant endDate;

    private Instant dueDate;

    private Integer estimateHour;

    private Integer estimateDay;

    private Integer storyPoint;

    private String assignee;

    private String assigneeId;

    private String blocker;

    private String pullRequest;

    private Integer weekNumber;

    private String sprintTitle;

    private Long sprintId;

    private Priority priotiry;

    private String releaseTitle;

    private Long releaseId;

    private String projectName;

    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getEstimateHour() {
        return estimateHour;
    }

    public void setEstimateHour(Integer estimateHour) {
        this.estimateHour = estimateHour;
    }

    public Integer getEstimateDay() {
        return estimateDay;
    }

    public void setEstimateDay(Integer estimateDay) {
        this.estimateDay = estimateDay;
    }

    public Integer getStoryPoint() {
        return storyPoint;
    }

    public void setStoryPoint(Integer storyPoint) {
        this.storyPoint = storyPoint;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getBlocker() {
        return blocker;
    }

    public void setBlocker(String blocker) {
        this.blocker = blocker;
    }

    public String getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(String pullRequest) {
        this.pullRequest = pullRequest;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getSprintTitle() {
        return sprintTitle;
    }

    public void setSprintTitle(String sprintTitle) {
        this.sprintTitle = sprintTitle;
    }

    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    public Priority getPriotiry() {
        return priotiry;
    }

    public void setPriotiry(Priority priotiry) {
        this.priotiry = priotiry;
    }

    public String getReleaseTitle() {
        return releaseTitle;
    }

    public void setReleaseTitle(String releaseTitle) {
        this.releaseTitle = releaseTitle;
    }

    public Long getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(Long releaseId) {
        this.releaseId = releaseId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IssueDTO)) {
            return false;
        }

        IssueDTO issueDTO = (IssueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, issueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IssueDTO{" +
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
