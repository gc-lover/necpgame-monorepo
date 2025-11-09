package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GuildApplicationDecisionRequest
 */


public class GuildApplicationDecisionRequest {

  /**
   * Gets or Sets decision
   */
  public enum DecisionEnum {
    APPROVE("approve"),
    
    DECLINE("decline");

    private final String value;

    DecisionEnum(String value) {
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
    public static DecisionEnum fromValue(String value) {
      for (DecisionEnum b : DecisionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DecisionEnum decision;

  private @Nullable String note;

  public GuildApplicationDecisionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GuildApplicationDecisionRequest(DecisionEnum decision) {
    this.decision = decision;
  }

  public GuildApplicationDecisionRequest decision(DecisionEnum decision) {
    this.decision = decision;
    return this;
  }

  /**
   * Get decision
   * @return decision
   */
  @NotNull 
  @Schema(name = "decision", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("decision")
  public DecisionEnum getDecision() {
    return decision;
  }

  public void setDecision(DecisionEnum decision) {
    this.decision = decision;
  }

  public GuildApplicationDecisionRequest note(@Nullable String note) {
    this.note = note;
    return this;
  }

  /**
   * Get note
   * @return note
   */
  
  @Schema(name = "note", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("note")
  public @Nullable String getNote() {
    return note;
  }

  public void setNote(@Nullable String note) {
    this.note = note;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildApplicationDecisionRequest guildApplicationDecisionRequest = (GuildApplicationDecisionRequest) o;
    return Objects.equals(this.decision, guildApplicationDecisionRequest.decision) &&
        Objects.equals(this.note, guildApplicationDecisionRequest.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(decision, note);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildApplicationDecisionRequest {\n");
    sb.append("    decision: ").append(toIndentedString(decision)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
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

