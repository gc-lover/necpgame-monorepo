package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * StartCorpoTowerRaidRequest
 */

@JsonTypeName("startCorpoTowerRaid_request")

public class StartCorpoTowerRaidRequest {

  private String partyId;

  private String leaderId;

  /**
   * Целевая корпорация для штурма
   */
  public enum TargetCorporationEnum {
    ARASAKA("arasaka"),
    
    MILITECH("militech");

    private final String value;

    TargetCorporationEnum(String value) {
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
    public static TargetCorporationEnum fromValue(String value) {
      for (TargetCorporationEnum b : TargetCorporationEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TargetCorporationEnum targetCorporation;

  private @Nullable String accessCard;

  /**
   * Подход к рейду
   */
  public enum ApproachEnum {
    STEALTH("stealth"),
    
    COMBAT("combat"),
    
    MIXED("mixed");

    private final String value;

    ApproachEnum(String value) {
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
    public static ApproachEnum fromValue(String value) {
      for (ApproachEnum b : ApproachEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ApproachEnum approach = ApproachEnum.MIXED;

  public StartCorpoTowerRaidRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StartCorpoTowerRaidRequest(String partyId, String leaderId, TargetCorporationEnum targetCorporation) {
    this.partyId = partyId;
    this.leaderId = leaderId;
    this.targetCorporation = targetCorporation;
  }

  public StartCorpoTowerRaidRequest partyId(String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  @NotNull 
  @Schema(name = "party_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("party_id")
  public String getPartyId() {
    return partyId;
  }

  public void setPartyId(String partyId) {
    this.partyId = partyId;
  }

  public StartCorpoTowerRaidRequest leaderId(String leaderId) {
    this.leaderId = leaderId;
    return this;
  }

  /**
   * Get leaderId
   * @return leaderId
   */
  @NotNull 
  @Schema(name = "leader_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("leader_id")
  public String getLeaderId() {
    return leaderId;
  }

  public void setLeaderId(String leaderId) {
    this.leaderId = leaderId;
  }

  public StartCorpoTowerRaidRequest targetCorporation(TargetCorporationEnum targetCorporation) {
    this.targetCorporation = targetCorporation;
    return this;
  }

  /**
   * Целевая корпорация для штурма
   * @return targetCorporation
   */
  @NotNull 
  @Schema(name = "target_corporation", description = "Целевая корпорация для штурма", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_corporation")
  public TargetCorporationEnum getTargetCorporation() {
    return targetCorporation;
  }

  public void setTargetCorporation(TargetCorporationEnum targetCorporation) {
    this.targetCorporation = targetCorporation;
  }

  public StartCorpoTowerRaidRequest accessCard(@Nullable String accessCard) {
    this.accessCard = accessCard;
    return this;
  }

  /**
   * Специальная карта доступа
   * @return accessCard
   */
  
  @Schema(name = "access_card", description = "Специальная карта доступа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("access_card")
  public @Nullable String getAccessCard() {
    return accessCard;
  }

  public void setAccessCard(@Nullable String accessCard) {
    this.accessCard = accessCard;
  }

  public StartCorpoTowerRaidRequest approach(ApproachEnum approach) {
    this.approach = approach;
    return this;
  }

  /**
   * Подход к рейду
   * @return approach
   */
  
  @Schema(name = "approach", description = "Подход к рейду", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approach")
  public ApproachEnum getApproach() {
    return approach;
  }

  public void setApproach(ApproachEnum approach) {
    this.approach = approach;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartCorpoTowerRaidRequest startCorpoTowerRaidRequest = (StartCorpoTowerRaidRequest) o;
    return Objects.equals(this.partyId, startCorpoTowerRaidRequest.partyId) &&
        Objects.equals(this.leaderId, startCorpoTowerRaidRequest.leaderId) &&
        Objects.equals(this.targetCorporation, startCorpoTowerRaidRequest.targetCorporation) &&
        Objects.equals(this.accessCard, startCorpoTowerRaidRequest.accessCard) &&
        Objects.equals(this.approach, startCorpoTowerRaidRequest.approach);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyId, leaderId, targetCorporation, accessCard, approach);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartCorpoTowerRaidRequest {\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    leaderId: ").append(toIndentedString(leaderId)).append("\n");
    sb.append("    targetCorporation: ").append(toIndentedString(targetCorporation)).append("\n");
    sb.append("    accessCard: ").append(toIndentedString(accessCard)).append("\n");
    sb.append("    approach: ").append(toIndentedString(approach)).append("\n");
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

