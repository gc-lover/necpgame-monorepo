package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderValidationIssue;
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
 * PlayerOrderValidationChecklistItem
 */


public class PlayerOrderValidationChecklistItem {

  /**
   * Gets or Sets stage
   */
  public enum StageEnum {
    COMPLETENESS("completeness"),
    
    LEGAL("legal"),
    
    SANCTIONS("sanctions"),
    
    TOXICITY("toxicity"),
    
    BUDGET("budget");

    private final String value;

    StageEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StageEnum fromValue(String value) {
      for (StageEnum b : StageEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StageEnum stage;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PASSED("passed"),
    
    FAILED("failed"),
    
    WARNINGS("warnings");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  @Valid
  private List<@Valid PlayerOrderValidationIssue> issues = new ArrayList<>();

  public PlayerOrderValidationChecklistItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderValidationChecklistItem(StageEnum stage, StatusEnum status) {
    this.stage = stage;
    this.status = status;
  }

  public PlayerOrderValidationChecklistItem stage(StageEnum stage) {
    this.stage = stage;
    return this;
  }

  /**
   * Get stage
   * @return stage
   */
  @NotNull 
  @Schema(name = "stage", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stage")
  public StageEnum getStage() {
    return stage;
  }

  public void setStage(StageEnum stage) {
    this.stage = stage;
  }

  public PlayerOrderValidationChecklistItem status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public PlayerOrderValidationChecklistItem issues(List<@Valid PlayerOrderValidationIssue> issues) {
    this.issues = issues;
    return this;
  }

  public PlayerOrderValidationChecklistItem addIssuesItem(PlayerOrderValidationIssue issuesItem) {
    if (this.issues == null) {
      this.issues = new ArrayList<>();
    }
    this.issues.add(issuesItem);
    return this;
  }

  /**
   * Get issues
   * @return issues
   */
  @Valid 
  @Schema(name = "issues", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("issues")
  public List<@Valid PlayerOrderValidationIssue> getIssues() {
    return issues;
  }

  public void setIssues(List<@Valid PlayerOrderValidationIssue> issues) {
    this.issues = issues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderValidationChecklistItem playerOrderValidationChecklistItem = (PlayerOrderValidationChecklistItem) o;
    return Objects.equals(this.stage, playerOrderValidationChecklistItem.stage) &&
        Objects.equals(this.status, playerOrderValidationChecklistItem.status) &&
        Objects.equals(this.issues, playerOrderValidationChecklistItem.issues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stage, status, issues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderValidationChecklistItem {\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    issues: ").append(toIndentedString(issues)).append("\n");
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

