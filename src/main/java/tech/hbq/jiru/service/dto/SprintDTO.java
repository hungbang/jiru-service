package tech.hbq.jiru.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import tech.hbq.jiru.domain.enumeration.SprintStatus;

/**
 * A DTO for the {@link tech.hbq.jiru.domain.Sprint} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SprintDTO implements Serializable {

    private Long id;

    private String title;

    private Instant startDate;

    private Instant endDate;

    private SprintStatus status;

    private String goal;

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

    public SprintStatus getStatus() {
        return status;
    }

    public void setStatus(SprintStatus status) {
        this.status = status;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SprintDTO)) {
            return false;
        }

        SprintDTO sprintDTO = (SprintDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sprintDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SprintDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", goal='" + getGoal() + "'" +
            "}";
    }
}
