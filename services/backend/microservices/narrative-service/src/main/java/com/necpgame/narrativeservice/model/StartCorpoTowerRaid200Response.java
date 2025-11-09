package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * StartCorpoTowerRaid200Response
 */

@JsonTypeName("startCorpoTowerRaid_200_response")

public class StartCorpoTowerRaid200Response {

  private @Nullable String raidId;

  private @Nullable String partyId;

  private @Nullable String targetCorporation;

  /**
   * Gets or Sets phase
   */
  public enum PhaseEnum {
    INFILTRATION("infiltration"),
    
    COMBAT_FLOORS("combat_floors"),
    
    CEO_BOSS("ceo_boss");

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

  private @Nullable String approach;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  public StartCorpoTowerRaid200Response raidId(@Nullable String raidId) {
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

  public StartCorpoTowerRaid200Response partyId(@Nullable String partyId) {
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

  public StartCorpoTowerRaid200Response targetCorporation(@Nullable String targetCorporation) {
    this.targetCorporation = targetCorporation;
    return this;
  }

  /**
   * Get targetCorporation
   * @return targetCorporation
   */
  
  @Schema(name = "target_corporation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_corporation")
  public @Nullable String getTargetCorporation() {
    return targetCorporation;
  }

  public void setTargetCorporation(@Nullable String targetCorporation) {
    this.targetCorporation = targetCorporation;
  }

  public StartCorpoTowerRaid200Response phase(PhaseEnum phase) {
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

  public StartCorpoTowerRaid200Response approach(@Nullable String approach) {
    this.approach = approach;
    return this;
  }

  /**
   * Get approach
   * @return approach
   */
  
  @Schema(name = "approach", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approach")
  public @Nullable String getApproach() {
    return approach;
  }

  public void setApproach(@Nullable String approach) {
    this.approach = approach;
  }

  public StartCorpoTowerRaid200Response startedAt(@Nullable OffsetDateTime startedAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartCorpoTowerRaid200Response startCorpoTowerRaid200Response = (StartCorpoTowerRaid200Response) o;
    return Objects.equals(this.raidId, startCorpoTowerRaid200Response.raidId) &&
        Objects.equals(this.partyId, startCorpoTowerRaid200Response.partyId) &&
        Objects.equals(this.targetCorporation, startCorpoTowerRaid200Response.targetCorporation) &&
        Objects.equals(this.phase, startCorpoTowerRaid200Response.phase) &&
        Objects.equals(this.approach, startCorpoTowerRaid200Response.approach) &&
        Objects.equals(this.startedAt, startCorpoTowerRaid200Response.startedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(raidId, partyId, targetCorporation, phase, approach, startedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartCorpoTowerRaid200Response {\n");
    sb.append("    raidId: ").append(toIndentedString(raidId)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    targetCorporation: ").append(toIndentedString(targetCorporation)).append("\n");
    sb.append("    phase: ").append(toIndentedString(phase)).append("\n");
    sb.append("    approach: ").append(toIndentedString(approach)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
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

