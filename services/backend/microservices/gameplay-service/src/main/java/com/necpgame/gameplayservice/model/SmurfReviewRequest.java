package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SmurfReviewRequest
 */


public class SmurfReviewRequest {

  private UUID playerId;

  /**
   * Gets or Sets verdict
   */
  public enum VerdictEnum {
    CLEAN("CLEAN"),
    
    WARN("WARN"),
    
    BAN_RECOMMENDED("BAN_RECOMMENDED");

    private final String value;

    VerdictEnum(String value) {
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
    public static VerdictEnum fromValue(String value) {
      for (VerdictEnum b : VerdictEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private VerdictEnum verdict;

  private @Nullable String notes;

  private @Nullable UUID reviewerId;

  public SmurfReviewRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SmurfReviewRequest(UUID playerId, VerdictEnum verdict) {
    this.playerId = playerId;
    this.verdict = verdict;
  }

  public SmurfReviewRequest playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public SmurfReviewRequest verdict(VerdictEnum verdict) {
    this.verdict = verdict;
    return this;
  }

  /**
   * Get verdict
   * @return verdict
   */
  @NotNull 
  @Schema(name = "verdict", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("verdict")
  public VerdictEnum getVerdict() {
    return verdict;
  }

  public void setVerdict(VerdictEnum verdict) {
    this.verdict = verdict;
  }

  public SmurfReviewRequest notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  @Size(max = 500) 
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  public SmurfReviewRequest reviewerId(@Nullable UUID reviewerId) {
    this.reviewerId = reviewerId;
    return this;
  }

  /**
   * Get reviewerId
   * @return reviewerId
   */
  @Valid 
  @Schema(name = "reviewerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reviewerId")
  public @Nullable UUID getReviewerId() {
    return reviewerId;
  }

  public void setReviewerId(@Nullable UUID reviewerId) {
    this.reviewerId = reviewerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmurfReviewRequest smurfReviewRequest = (SmurfReviewRequest) o;
    return Objects.equals(this.playerId, smurfReviewRequest.playerId) &&
        Objects.equals(this.verdict, smurfReviewRequest.verdict) &&
        Objects.equals(this.notes, smurfReviewRequest.notes) &&
        Objects.equals(this.reviewerId, smurfReviewRequest.reviewerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, verdict, notes, reviewerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SmurfReviewRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    verdict: ").append(toIndentedString(verdict)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
    sb.append("    reviewerId: ").append(toIndentedString(reviewerId)).append("\n");
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

