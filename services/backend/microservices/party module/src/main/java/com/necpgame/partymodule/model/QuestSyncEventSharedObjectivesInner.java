package com.necpgame.partymodule.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QuestSyncEventSharedObjectivesInner
 */

@JsonTypeName("QuestSyncEvent_sharedObjectives_inner")

public class QuestSyncEventSharedObjectivesInner {

  private @Nullable String objectiveId;

  private @Nullable String state;

  public QuestSyncEventSharedObjectivesInner objectiveId(@Nullable String objectiveId) {
    this.objectiveId = objectiveId;
    return this;
  }

  /**
   * Get objectiveId
   * @return objectiveId
   */
  
  @Schema(name = "objectiveId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objectiveId")
  public @Nullable String getObjectiveId() {
    return objectiveId;
  }

  public void setObjectiveId(@Nullable String objectiveId) {
    this.objectiveId = objectiveId;
  }

  public QuestSyncEventSharedObjectivesInner state(@Nullable String state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  
  @Schema(name = "state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public @Nullable String getState() {
    return state;
  }

  public void setState(@Nullable String state) {
    this.state = state;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestSyncEventSharedObjectivesInner questSyncEventSharedObjectivesInner = (QuestSyncEventSharedObjectivesInner) o;
    return Objects.equals(this.objectiveId, questSyncEventSharedObjectivesInner.objectiveId) &&
        Objects.equals(this.state, questSyncEventSharedObjectivesInner.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectiveId, state);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestSyncEventSharedObjectivesInner {\n");
    sb.append("    objectiveId: ").append(toIndentedString(objectiveId)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
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

