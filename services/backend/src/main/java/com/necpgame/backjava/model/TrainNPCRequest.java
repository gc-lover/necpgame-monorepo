package com.necpgame.backjava.model;

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
 * TrainNPCRequest
 */

@JsonTypeName("trainNPC_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class TrainNPCRequest {

  private @Nullable String skill;

  private @Nullable Integer trainingDurationHours;

  private @Nullable Integer cost;

  public TrainNPCRequest skill(@Nullable String skill) {
    this.skill = skill;
    return this;
  }

  /**
   * Get skill
   * @return skill
   */
  
  @Schema(name = "skill", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill")
  public @Nullable String getSkill() {
    return skill;
  }

  public void setSkill(@Nullable String skill) {
    this.skill = skill;
  }

  public TrainNPCRequest trainingDurationHours(@Nullable Integer trainingDurationHours) {
    this.trainingDurationHours = trainingDurationHours;
    return this;
  }

  /**
   * Get trainingDurationHours
   * @return trainingDurationHours
   */
  
  @Schema(name = "training_duration_hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("training_duration_hours")
  public @Nullable Integer getTrainingDurationHours() {
    return trainingDurationHours;
  }

  public void setTrainingDurationHours(@Nullable Integer trainingDurationHours) {
    this.trainingDurationHours = trainingDurationHours;
  }

  public TrainNPCRequest cost(@Nullable Integer cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get cost
   * @return cost
   */
  
  @Schema(name = "cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost")
  public @Nullable Integer getCost() {
    return cost;
  }

  public void setCost(@Nullable Integer cost) {
    this.cost = cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrainNPCRequest trainNPCRequest = (TrainNPCRequest) o;
    return Objects.equals(this.skill, trainNPCRequest.skill) &&
        Objects.equals(this.trainingDurationHours, trainNPCRequest.trainingDurationHours) &&
        Objects.equals(this.cost, trainNPCRequest.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skill, trainingDurationHours, cost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TrainNPCRequest {\n");
    sb.append("    skill: ").append(toIndentedString(skill)).append("\n");
    sb.append("    trainingDurationHours: ").append(toIndentedString(trainingDurationHours)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
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

