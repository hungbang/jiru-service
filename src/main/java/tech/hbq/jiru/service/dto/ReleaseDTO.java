package tech.hbq.jiru.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import tech.hbq.jiru.domain.enumeration.ReleaseStatus;

/**
 * A DTO for the {@link tech.hbq.jiru.domain.Release} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReleaseDTO implements Serializable {

    private Long id;

    private String title;

    private Instant startDate;

    private Instant endDate;

    private String description;

    private ReleaseStatus status;

    private String note;

    private String projectName;

    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReleaseStatus getStatus() {
        return status;
    }

    public void setStatus(ReleaseStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        if (!(o instanceof ReleaseDTO)) {
            return false;
        }

        ReleaseDTO releaseDTO = (ReleaseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, releaseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReleaseDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", note='" + getNote() + "'" +
            ", projectName='" + getProjectName() + "'" +
            ", projectId=" + getProjectId() +
            "}";
    }
}
