package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.ActionItem;
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
 * Структура RCA с action items.
 */

@Schema(name = "RcaRecordCreate", description = "Структура RCA с action items.")

public class RcaRecordCreate {

  private String rootCause;

  @Valid
  private List<String> contributingFactors = new ArrayList<>();

  @Valid
  private List<@Valid ActionItem> correctiveActions = new ArrayList<>();

  @Valid
  private List<String> lessonsLearned = new ArrayList<>();

  public RcaRecordCreate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RcaRecordCreate(String rootCause, List<@Valid ActionItem> correctiveActions) {
    this.rootCause = rootCause;
    this.correctiveActions = correctiveActions;
  }

  public RcaRecordCreate rootCause(String rootCause) {
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

  public RcaRecordCreate contributingFactors(List<String> contributingFactors) {
    this.contributingFactors = contributingFactors;
    return this;
  }

  public RcaRecordCreate addContributingFactorsItem(String contributingFactorsItem) {
    if (this.contributingFactors == null) {
      this.contributingFactors = new ArrayList<>();
    }
    this.contributingFactors.add(contributingFactorsItem);
    return this;
  }

  /**
   * Get contributingFactors
   * @return contributingFactors
   */
  
  @Schema(name = "contributing_factors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contributing_factors")
  public List<String> getContributingFactors() {
    return contributingFactors;
  }

  public void setContributingFactors(List<String> contributingFactors) {
    this.contributingFactors = contributingFactors;
  }

  public RcaRecordCreate correctiveActions(List<@Valid ActionItem> correctiveActions) {
    this.correctiveActions = correctiveActions;
    return this;
  }

  public RcaRecordCreate addCorrectiveActionsItem(ActionItem correctiveActionsItem) {
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

  public RcaRecordCreate lessonsLearned(List<String> lessonsLearned) {
    this.lessonsLearned = lessonsLearned;
    return this;
  }

  public RcaRecordCreate addLessonsLearnedItem(String lessonsLearnedItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RcaRecordCreate rcaRecordCreate = (RcaRecordCreate) o;
    return Objects.equals(this.rootCause, rcaRecordCreate.rootCause) &&
        Objects.equals(this.contributingFactors, rcaRecordCreate.contributingFactors) &&
        Objects.equals(this.correctiveActions, rcaRecordCreate.correctiveActions) &&
        Objects.equals(this.lessonsLearned, rcaRecordCreate.lessonsLearned);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rootCause, contributingFactors, correctiveActions, lessonsLearned);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RcaRecordCreate {\n");
    sb.append("    rootCause: ").append(toIndentedString(rootCause)).append("\n");
    sb.append("    contributingFactors: ").append(toIndentedString(contributingFactors)).append("\n");
    sb.append("    correctiveActions: ").append(toIndentedString(correctiveActions)).append("\n");
    sb.append("    lessonsLearned: ").append(toIndentedString(lessonsLearned)).append("\n");
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

