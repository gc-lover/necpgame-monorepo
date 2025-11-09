package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * ActiveQuestProgressChoicesMadeInner
 */

@JsonTypeName("ActiveQuestProgress_choices_made_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ActiveQuestProgressChoicesMadeInner {

  private @Nullable String choiceId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  public ActiveQuestProgressChoicesMadeInner choiceId(@Nullable String choiceId) {
    this.choiceId = choiceId;
    return this;
  }

  /**
   * Get choiceId
   * @return choiceId
   */
  
  @Schema(name = "choice_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choice_id")
  public @Nullable String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(@Nullable String choiceId) {
    this.choiceId = choiceId;
  }

  public ActiveQuestProgressChoicesMadeInner timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActiveQuestProgressChoicesMadeInner activeQuestProgressChoicesMadeInner = (ActiveQuestProgressChoicesMadeInner) o;
    return Objects.equals(this.choiceId, activeQuestProgressChoicesMadeInner.choiceId) &&
        Objects.equals(this.timestamp, activeQuestProgressChoicesMadeInner.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(choiceId, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActiveQuestProgressChoicesMadeInner {\n");
    sb.append("    choiceId: ").append(toIndentedString(choiceId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

