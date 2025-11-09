package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * PlayerOrderRiskProfile
 */


public class PlayerOrderRiskProfile {

  /**
   * Уровень риска заказа.
   */
  public enum RiskLevelEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    EXTREME("extreme");

    private final String value;

    RiskLevelEnum(String value) {
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
    public static RiskLevelEnum fromValue(String value) {
      for (RiskLevelEnum b : RiskLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RiskLevelEnum riskLevel;

  /**
   * Правовой статус операции в зоне выполнения.
   */
  public enum LegalStatusEnum {
    LEGAL("legal"),
    
    GREY("grey"),
    
    ILLEGAL("illegal");

    private final String value;

    LegalStatusEnum(String value) {
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
    public static LegalStatusEnum fromValue(String value) {
      for (LegalStatusEnum b : LegalStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private LegalStatusEnum legalStatus;

  @Valid
  private List<String> hostileFactions = new ArrayList<>();

  @Valid
  private List<String> zoneIds = new ArrayList<>();

  private @Nullable String notes;

  public PlayerOrderRiskProfile() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderRiskProfile(RiskLevelEnum riskLevel, LegalStatusEnum legalStatus) {
    this.riskLevel = riskLevel;
    this.legalStatus = legalStatus;
  }

  public PlayerOrderRiskProfile riskLevel(RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
    return this;
  }

  /**
   * Уровень риска заказа.
   * @return riskLevel
   */
  @NotNull 
  @Schema(name = "riskLevel", description = "Уровень риска заказа.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("riskLevel")
  public RiskLevelEnum getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
  }

  public PlayerOrderRiskProfile legalStatus(LegalStatusEnum legalStatus) {
    this.legalStatus = legalStatus;
    return this;
  }

  /**
   * Правовой статус операции в зоне выполнения.
   * @return legalStatus
   */
  @NotNull 
  @Schema(name = "legalStatus", description = "Правовой статус операции в зоне выполнения.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("legalStatus")
  public LegalStatusEnum getLegalStatus() {
    return legalStatus;
  }

  public void setLegalStatus(LegalStatusEnum legalStatus) {
    this.legalStatus = legalStatus;
  }

  public PlayerOrderRiskProfile hostileFactions(List<String> hostileFactions) {
    this.hostileFactions = hostileFactions;
    return this;
  }

  public PlayerOrderRiskProfile addHostileFactionsItem(String hostileFactionsItem) {
    if (this.hostileFactions == null) {
      this.hostileFactions = new ArrayList<>();
    }
    this.hostileFactions.add(hostileFactionsItem);
    return this;
  }

  /**
   * Перечень враждебных фракций.
   * @return hostileFactions
   */
  
  @Schema(name = "hostileFactions", description = "Перечень враждебных фракций.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hostileFactions")
  public List<String> getHostileFactions() {
    return hostileFactions;
  }

  public void setHostileFactions(List<String> hostileFactions) {
    this.hostileFactions = hostileFactions;
  }

  public PlayerOrderRiskProfile zoneIds(List<String> zoneIds) {
    this.zoneIds = zoneIds;
    return this;
  }

  public PlayerOrderRiskProfile addZoneIdsItem(String zoneIdsItem) {
    if (this.zoneIds == null) {
      this.zoneIds = new ArrayList<>();
    }
    this.zoneIds.add(zoneIdsItem);
    return this;
  }

  /**
   * Зоны выполнения заказа (идентификаторы world-service).
   * @return zoneIds
   */
  
  @Schema(name = "zoneIds", description = "Зоны выполнения заказа (идентификаторы world-service).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zoneIds")
  public List<String> getZoneIds() {
    return zoneIds;
  }

  public void setZoneIds(List<String> zoneIds) {
    this.zoneIds = zoneIds;
  }

  public PlayerOrderRiskProfile notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Дополнительные пояснения по рискам.
   * @return notes
   */
  
  @Schema(name = "notes", description = "Дополнительные пояснения по рискам.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    PlayerOrderRiskProfile playerOrderRiskProfile = (PlayerOrderRiskProfile) o;
    return Objects.equals(this.riskLevel, playerOrderRiskProfile.riskLevel) &&
        Objects.equals(this.legalStatus, playerOrderRiskProfile.legalStatus) &&
        Objects.equals(this.hostileFactions, playerOrderRiskProfile.hostileFactions) &&
        Objects.equals(this.zoneIds, playerOrderRiskProfile.zoneIds) &&
        Objects.equals(this.notes, playerOrderRiskProfile.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(riskLevel, legalStatus, hostileFactions, zoneIds, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderRiskProfile {\n");
    sb.append("    riskLevel: ").append(toIndentedString(riskLevel)).append("\n");
    sb.append("    legalStatus: ").append(toIndentedString(legalStatus)).append("\n");
    sb.append("    hostileFactions: ").append(toIndentedString(hostileFactions)).append("\n");
    sb.append("    zoneIds: ").append(toIndentedString(zoneIds)).append("\n");
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

