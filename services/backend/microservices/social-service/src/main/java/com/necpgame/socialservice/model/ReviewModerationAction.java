package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * ReviewModerationAction
 */


public class ReviewModerationAction {

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    BAN_AUTHOR("ban_author"),
    
    ARCHIVE_REVIEW("archive_review"),
    
    ESCALATE_TO_ARBITRATION("escalate_to_arbitration"),
    
    REQUEST_REVISION("request_revision");

    private final String value;

    ActionEnum(String value) {
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
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActionEnum action;

  private @Nullable UUID performedBy;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime performedAt;

  private @Nullable String notes;

  public ReviewModerationAction() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewModerationAction(ActionEnum action, OffsetDateTime performedAt) {
    this.action = action;
    this.performedAt = performedAt;
  }

  public ReviewModerationAction action(ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @NotNull 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public ReviewModerationAction performedBy(@Nullable UUID performedBy) {
    this.performedBy = performedBy;
    return this;
  }

  /**
   * Get performedBy
   * @return performedBy
   */
  @Valid 
  @Schema(name = "performedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performedBy")
  public @Nullable UUID getPerformedBy() {
    return performedBy;
  }

  public void setPerformedBy(@Nullable UUID performedBy) {
    this.performedBy = performedBy;
  }

  public ReviewModerationAction performedAt(OffsetDateTime performedAt) {
    this.performedAt = performedAt;
    return this;
  }

  /**
   * Get performedAt
   * @return performedAt
   */
  @NotNull @Valid 
  @Schema(name = "performedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("performedAt")
  public OffsetDateTime getPerformedAt() {
    return performedAt;
  }

  public void setPerformedAt(OffsetDateTime performedAt) {
    this.performedAt = performedAt;
  }

  public ReviewModerationAction notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  @Size(max = 1024) 
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewModerationAction reviewModerationAction = (ReviewModerationAction) o;
    return Objects.equals(this.action, reviewModerationAction.action) &&
        Objects.equals(this.performedBy, reviewModerationAction.performedBy) &&
        Objects.equals(this.performedAt, reviewModerationAction.performedAt) &&
        Objects.equals(this.notes, reviewModerationAction.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, performedBy, performedAt, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewModerationAction {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    performedBy: ").append(toIndentedString(performedBy)).append("\n");
    sb.append("    performedAt: ").append(toIndentedString(performedAt)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

