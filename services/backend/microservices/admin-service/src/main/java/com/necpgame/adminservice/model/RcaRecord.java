package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.ActionItem;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Сохраненная RCA запись.
 */

@Schema(name = "RcaRecord", description = "Сохраненная RCA запись.")

public class RcaRecord {

  private String id;

  private String incidentId;

  private String rootCause;

  @Valid
  private List<@Valid ActionItem> correctiveActions = new ArrayList<>();

  @Valid
  private List<String> lessonsLearned = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  private @Nullable String owner;

  public RcaRecord() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RcaRecord(String id, String incidentId, String rootCause, List<@Valid ActionItem> correctiveActions) {
    this.id = id;
    this.incidentId = incidentId;
    this.rootCause = rootCause;
    this.correctiveActions = correctiveActions;
  }

  public RcaRecord id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public RcaRecord incidentId(String incidentId) {
    this.incidentId = incidentId;
    return this;
  }

  /**
   * Get incidentId
   * @return incidentId
   */
  @NotNull 
  @Schema(name = "incident_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("incident_id")
  public String getIncidentId() {
    return incidentId;
  }

  public void setIncidentId(String incidentId) {
    this.incidentId = incidentId;
  }

  public RcaRecord rootCause(String rootCause) {
    this.rootCause = rootCause;
    return this;
  }

  /**
   * Get rootCause
   * @return rootCause
   */
  @NotNull 
  @Schema(name = "root_cause", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("root_cause")
  public String getRootCause() {
    return rootCause;
  }

  public void setRootCause(String rootCause) {
    this.rootCause = rootCause;
  }

  public RcaRecord correctiveActions(List<@Valid ActionItem> correctiveActions) {
    this.correctiveActions = correctiveActions;
    return this;
  }

  public RcaRecord addCorrectiveActionsItem(ActionItem correctiveActionsItem) {
    if (this.correctiveActions == null) {
      this.correctiveActions = new ArrayList<>();
    }
    this.correctiveActions.add(correctiveActionsItem);
    return this;
  }

  /**
   * Get correctiveActions
   * @return correctiveActions
   */
  @NotNull @Valid 
  @Schema(name = "corrective_actions", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("corrective_actions")
  public List<@Valid ActionItem> getCorrectiveActions() {
    return correctiveActions;
  }

  public void setCorrectiveActions(List<@Valid ActionItem> correctiveActions) {
    this.correctiveActions = correctiveActions;
  }

  public RcaRecord lessonsLearned(List<String> lessonsLearned) {
    this.lessonsLearned = lessonsLearned;
    return this;
  }

  public RcaRecord addLessonsLearnedItem(String lessonsLearnedItem) {
    if (this.lessonsLearned == null) {
      this.lessonsLearned = new ArrayList<>();
    }
    this.lessonsLearned.add(lessonsLearnedItem);
    return this;
  }

  /**
   * Get lessonsLearned
   * @return lessonsLearned
   */
  
  @Schema(name = "lessons_learned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lessons_learned")
  public List<String> getLessonsLearned() {
    return lessonsLearned;
  }

  public void setLessonsLearned(List<String> lessonsLearned) {
    this.lessonsLearned = lessonsLearned;
  }

  public RcaRecord createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public RcaRecord owner(@Nullable String owner) {
    this.owner = owner;
    return this;
  }

  /**
   * Get owner
   * @return owner
   */
  
  @Schema(name = "owner", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("owner")
  public @Nullable String getOwner() {
    return owner;
  }

  public void setOwner(@Nullable String owner) {
    this.owner = owner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RcaRecord rcaRecord = (RcaRecord) o;
    return Objects.equals(this.id, rcaRecord.id) &&
        Objects.equals(this.incidentId, rcaRecord.incidentId) &&
        Objects.equals(this.rootCause, rcaRecord.rootCause) &&
        Objects.equals(this.correctiveActions, rcaRecord.correctiveActions) &&
        Objects.equals(this.lessonsLearned, rcaRecord.lessonsLearned) &&
        Objects.equals(this.createdAt, rcaRecord.createdAt) &&
        Objects.equals(this.owner, rcaRecord.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, incidentId, rootCause, correctiveActions, lessonsLearned, createdAt, owner);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RcaRecord {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    incidentId: ").append(toIndentedString(incidentId)).append("\n");
    sb.append("    rootCause: ").append(toIndentedString(rootCause)).append("\n");
    sb.append("    correctiveActions: ").append(toIndentedString(correctiveActions)).append("\n");
    sb.append("    lessonsLearned: ").append(toIndentedString(lessonsLearned)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

