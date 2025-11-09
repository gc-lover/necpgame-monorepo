package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.QuestObjective;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetQuestObjectives200Response
 */

@JsonTypeName("getQuestObjectives_200_response")

public class GetQuestObjectives200Response {

  @Valid
  private List<@Valid QuestObjective> objectives = new ArrayList<>();

  public GetQuestObjectives200Response objectives(List<@Valid QuestObjective> objectives) {
    this.objectives = objectives;
    return this;
  }

  public GetQuestObjectives200Response addObjectivesItem(QuestObjective objectivesItem) {
    if (this.objectives == null) {
      this.objectives = new ArrayList<>();
    }
    this.objectives.add(objectivesItem);
    return this;
  }

  /**
   * Get objectives
   * @return objectives
   */
  @Valid 
  @Schema(name = "objectives", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objectives")
  public List<@Valid QuestObjective> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<@Valid QuestObjective> objectives) {
    this.objectives = objectives;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetQuestObjectives200Response getQuestObjectives200Response = (GetQuestObjectives200Response) o;
    return Objects.equals(this.objectives, getQuestObjectives200Response.objectives);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectives);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetQuestObjectives200Response {\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
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

