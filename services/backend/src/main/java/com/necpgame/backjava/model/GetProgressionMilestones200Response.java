package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.ProgressionMilestone;
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
 * GetProgressionMilestones200Response
 */

@JsonTypeName("getProgressionMilestones_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetProgressionMilestones200Response {

  @Valid
  private List<@Valid ProgressionMilestone> milestones = new ArrayList<>();

  public GetProgressionMilestones200Response milestones(List<@Valid ProgressionMilestone> milestones) {
    this.milestones = milestones;
    return this;
  }

  public GetProgressionMilestones200Response addMilestonesItem(ProgressionMilestone milestonesItem) {
    if (this.milestones == null) {
      this.milestones = new ArrayList<>();
    }
    this.milestones.add(milestonesItem);
    return this;
  }

  /**
   * Get milestones
   * @return milestones
   */
  @Valid 
  @Schema(name = "milestones", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("milestones")
  public List<@Valid ProgressionMilestone> getMilestones() {
    return milestones;
  }

  public void setMilestones(List<@Valid ProgressionMilestone> milestones) {
    this.milestones = milestones;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetProgressionMilestones200Response getProgressionMilestones200Response = (GetProgressionMilestones200Response) o;
    return Objects.equals(this.milestones, getProgressionMilestones200Response.milestones);
  }

  @Override
  public int hashCode() {
    return Objects.hash(milestones);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetProgressionMilestones200Response {\n");
    sb.append("    milestones: ").append(toIndentedString(milestones)).append("\n");
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

