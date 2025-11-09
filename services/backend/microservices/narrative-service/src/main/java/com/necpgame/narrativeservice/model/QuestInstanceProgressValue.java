package com.necpgame.narrativeservice.model;

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
 * QuestInstanceProgressValue
 */

@JsonTypeName("QuestInstance_progress_value")

public class QuestInstanceProgressValue {

  private @Nullable Integer current;

  private @Nullable Integer target;

  private @Nullable Boolean completed;

  public QuestInstanceProgressValue current(@Nullable Integer current) {
    this.current = current;
    return this;
  }

  /**
   * Get current
   * @return current
   */
  
  @Schema(name = "current", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current")
  public @Nullable Integer getCurrent() {
    return current;
  }

  public void setCurrent(@Nullable Integer current) {
    this.current = current;
  }

  public QuestInstanceProgressValue target(@Nullable Integer target) {
    this.target = target;
    return this;
  }

  /**
   * Get target
   * @return target
   */
  
  @Schema(name = "target", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target")
  public @Nullable Integer getTarget() {
    return target;
  }

  public void setTarget(@Nullable Integer target) {
    this.target = target;
  }

  public QuestInstanceProgressValue completed(@Nullable Boolean completed) {
    this.completed = completed;
    return this;
  }

  /**
   * Get completed
   * @return completed
   */
  
  @Schema(name = "completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completed")
  public @Nullable Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(@Nullable Boolean completed) {
    this.completed = completed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestInstanceProgressValue questInstanceProgressValue = (QuestInstanceProgressValue) o;
    return Objects.equals(this.current, questInstanceProgressValue.current) &&
        Objects.equals(this.target, questInstanceProgressValue.target) &&
        Objects.equals(this.completed, questInstanceProgressValue.completed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(current, target, completed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestInstanceProgressValue {\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    completed: ").append(toIndentedString(completed)).append("\n");
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

