package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.ParticipantResultStats;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ParticipantResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ParticipantResult {

  private @Nullable String participantId;

  private @Nullable String type;

  /**
   * Gets or Sets finalStatus
   */
  public enum FinalStatusEnum {
    ALIVE("ALIVE"),
    
    DOWNED("DOWNED"),
    
    DEAD("DEAD");

    private final String value;

    FinalStatusEnum(String value) {
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
    public static FinalStatusEnum fromValue(String value) {
      for (FinalStatusEnum b : FinalStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable FinalStatusEnum finalStatus;

  private @Nullable ParticipantResultStats stats;

  public ParticipantResult participantId(@Nullable String participantId) {
    this.participantId = participantId;
    return this;
  }

  /**
   * Get participantId
   * @return participantId
   */
  
  @Schema(name = "participant_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participant_id")
  public @Nullable String getParticipantId() {
    return participantId;
  }

  public void setParticipantId(@Nullable String participantId) {
    this.participantId = participantId;
  }

  public ParticipantResult type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public ParticipantResult finalStatus(@Nullable FinalStatusEnum finalStatus) {
    this.finalStatus = finalStatus;
    return this;
  }

  /**
   * Get finalStatus
   * @return finalStatus
   */
  
  @Schema(name = "final_status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("final_status")
  public @Nullable FinalStatusEnum getFinalStatus() {
    return finalStatus;
  }

  public void setFinalStatus(@Nullable FinalStatusEnum finalStatus) {
    this.finalStatus = finalStatus;
  }

  public ParticipantResult stats(@Nullable ParticipantResultStats stats) {
    this.stats = stats;
    return this;
  }

  /**
   * Get stats
   * @return stats
   */
  @Valid 
  @Schema(name = "stats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stats")
  public @Nullable ParticipantResultStats getStats() {
    return stats;
  }

  public void setStats(@Nullable ParticipantResultStats stats) {
    this.stats = stats;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParticipantResult participantResult = (ParticipantResult) o;
    return Objects.equals(this.participantId, participantResult.participantId) &&
        Objects.equals(this.type, participantResult.type) &&
        Objects.equals(this.finalStatus, participantResult.finalStatus) &&
        Objects.equals(this.stats, participantResult.stats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(participantId, type, finalStatus, stats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParticipantResult {\n");
    sb.append("    participantId: ").append(toIndentedString(participantId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    finalStatus: ").append(toIndentedString(finalStatus)).append("\n");
    sb.append("    stats: ").append(toIndentedString(stats)).append("\n");
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

