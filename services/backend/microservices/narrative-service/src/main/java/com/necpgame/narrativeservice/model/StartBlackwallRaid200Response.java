package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
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
 * StartBlackwallRaid200Response
 */

@JsonTypeName("startBlackwallRaid_200_response")

public class StartBlackwallRaid200Response {

  private @Nullable String raidId;

  private @Nullable String partyId;

  /**
   * Gets or Sets phase
   */
  public enum PhaseEnum {
    INFILTRATION("infiltration"),
    
    DEEP_ZONE("deep_zone"),
    
    CORE_BREACH("core_breach");

    private final String value;

    PhaseEnum(String value) {
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
    public static PhaseEnum fromValue(String value) {
      for (PhaseEnum b : PhaseEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PhaseEnum phase = PhaseEnum.INFILTRATION;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  private @Nullable BigDecimal estimatedDuration;

  public StartBlackwallRaid200Response raidId(@Nullable String raidId) {
    this.raidId = raidId;
    return this;
  }

  /**
   * Get raidId
   * @return raidId
   */
  
  @Schema(name = "raid_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("raid_id")
  public @Nullable String getRaidId() {
    return raidId;
  }

  public void setRaidId(@Nullable String raidId) {
    this.raidId = raidId;
  }

  public StartBlackwallRaid200Response partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  
  @Schema(name = "party_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("party_id")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
  }

  public StartBlackwallRaid200Response phase(PhaseEnum phase) {
    this.phase = phase;
    return this;
  }

  /**
   * Get phase
   * @return phase
   */
  
  @Schema(name = "phase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phase")
  public PhaseEnum getPhase() {
    return phase;
  }

  public void setPhase(PhaseEnum phase) {
    this.phase = phase;
  }

  public StartBlackwallRaid200Response startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "started_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("started_at")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public StartBlackwallRaid200Response estimatedDuration(@Nullable BigDecimal estimatedDuration) {
    this.estimatedDuration = estimatedDuration;
    return this;
  }

  /**
   * Оценка длительности (минуты)
   * @return estimatedDuration
   */
  @Valid 
  @Schema(name = "estimated_duration", description = "Оценка длительности (минуты)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_duration")
  public @Nullable BigDecimal getEstimatedDuration() {
    return estimatedDuration;
  }

  public void setEstimatedDuration(@Nullable BigDecimal estimatedDuration) {
    this.estimatedDuration = estimatedDuration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartBlackwallRaid200Response startBlackwallRaid200Response = (StartBlackwallRaid200Response) o;
    return Objects.equals(this.raidId, startBlackwallRaid200Response.raidId) &&
        Objects.equals(this.partyId, startBlackwallRaid200Response.partyId) &&
        Objects.equals(this.phase, startBlackwallRaid200Response.phase) &&
        Objects.equals(this.startedAt, startBlackwallRaid200Response.startedAt) &&
        Objects.equals(this.estimatedDuration, startBlackwallRaid200Response.estimatedDuration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(raidId, partyId, phase, startedAt, estimatedDuration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartBlackwallRaid200Response {\n");
    sb.append("    raidId: ").append(toIndentedString(raidId)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    phase: ").append(toIndentedString(phase)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    estimatedDuration: ").append(toIndentedString(estimatedDuration)).append("\n");
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

