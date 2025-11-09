package com.necpgame.worldservice.model;

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
 * TerritoryRestriction
 */


public class TerritoryRestriction {

  private String zoneId;

  /**
   * Gets or Sets legalStatus
   */
  public enum LegalStatusEnum {
    ALLOWED("allowed"),
    
    RESTRICTED("restricted"),
    
    PROHIBITED("prohibited");

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
  private List<String> allowedTemplates = new ArrayList<>();

  @Valid
  private List<String> restrictedActions = new ArrayList<>();

  private @Nullable String curfew;

  /**
   * Gets or Sets hazardLevel
   */
  public enum HazardLevelEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    CRITICAL("critical");

    private final String value;

    HazardLevelEnum(String value) {
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
    public static HazardLevelEnum fromValue(String value) {
      for (HazardLevelEnum b : HazardLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable HazardLevelEnum hazardLevel;

  @Valid
  private List<String> requiredPermits = new ArrayList<>();

  public TerritoryRestriction() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TerritoryRestriction(String zoneId, LegalStatusEnum legalStatus) {
    this.zoneId = zoneId;
    this.legalStatus = legalStatus;
  }

  public TerritoryRestriction zoneId(String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  @NotNull 
  @Schema(name = "zoneId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("zoneId")
  public String getZoneId() {
    return zoneId;
  }

  public void setZoneId(String zoneId) {
    this.zoneId = zoneId;
  }

  public TerritoryRestriction legalStatus(LegalStatusEnum legalStatus) {
    this.legalStatus = legalStatus;
    return this;
  }

  /**
   * Get legalStatus
   * @return legalStatus
   */
  @NotNull 
  @Schema(name = "legalStatus", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("legalStatus")
  public LegalStatusEnum getLegalStatus() {
    return legalStatus;
  }

  public void setLegalStatus(LegalStatusEnum legalStatus) {
    this.legalStatus = legalStatus;
  }

  public TerritoryRestriction allowedTemplates(List<String> allowedTemplates) {
    this.allowedTemplates = allowedTemplates;
    return this;
  }

  public TerritoryRestriction addAllowedTemplatesItem(String allowedTemplatesItem) {
    if (this.allowedTemplates == null) {
      this.allowedTemplates = new ArrayList<>();
    }
    this.allowedTemplates.add(allowedTemplatesItem);
    return this;
  }

  /**
   * Get allowedTemplates
   * @return allowedTemplates
   */
  
  @Schema(name = "allowedTemplates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowedTemplates")
  public List<String> getAllowedTemplates() {
    return allowedTemplates;
  }

  public void setAllowedTemplates(List<String> allowedTemplates) {
    this.allowedTemplates = allowedTemplates;
  }

  public TerritoryRestriction restrictedActions(List<String> restrictedActions) {
    this.restrictedActions = restrictedActions;
    return this;
  }

  public TerritoryRestriction addRestrictedActionsItem(String restrictedActionsItem) {
    if (this.restrictedActions == null) {
      this.restrictedActions = new ArrayList<>();
    }
    this.restrictedActions.add(restrictedActionsItem);
    return this;
  }

  /**
   * Get restrictedActions
   * @return restrictedActions
   */
  
  @Schema(name = "restrictedActions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("restrictedActions")
  public List<String> getRestrictedActions() {
    return restrictedActions;
  }

  public void setRestrictedActions(List<String> restrictedActions) {
    this.restrictedActions = restrictedActions;
  }

  public TerritoryRestriction curfew(@Nullable String curfew) {
    this.curfew = curfew;
    return this;
  }

  /**
   * Get curfew
   * @return curfew
   */
  
  @Schema(name = "curfew", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("curfew")
  public @Nullable String getCurfew() {
    return curfew;
  }

  public void setCurfew(@Nullable String curfew) {
    this.curfew = curfew;
  }

  public TerritoryRestriction hazardLevel(@Nullable HazardLevelEnum hazardLevel) {
    this.hazardLevel = hazardLevel;
    return this;
  }

  /**
   * Get hazardLevel
   * @return hazardLevel
   */
  
  @Schema(name = "hazardLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hazardLevel")
  public @Nullable HazardLevelEnum getHazardLevel() {
    return hazardLevel;
  }

  public void setHazardLevel(@Nullable HazardLevelEnum hazardLevel) {
    this.hazardLevel = hazardLevel;
  }

  public TerritoryRestriction requiredPermits(List<String> requiredPermits) {
    this.requiredPermits = requiredPermits;
    return this;
  }

  public TerritoryRestriction addRequiredPermitsItem(String requiredPermitsItem) {
    if (this.requiredPermits == null) {
      this.requiredPermits = new ArrayList<>();
    }
    this.requiredPermits.add(requiredPermitsItem);
    return this;
  }

  /**
   * Get requiredPermits
   * @return requiredPermits
   */
  
  @Schema(name = "requiredPermits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredPermits")
  public List<String> getRequiredPermits() {
    return requiredPermits;
  }

  public void setRequiredPermits(List<String> requiredPermits) {
    this.requiredPermits = requiredPermits;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TerritoryRestriction territoryRestriction = (TerritoryRestriction) o;
    return Objects.equals(this.zoneId, territoryRestriction.zoneId) &&
        Objects.equals(this.legalStatus, territoryRestriction.legalStatus) &&
        Objects.equals(this.allowedTemplates, territoryRestriction.allowedTemplates) &&
        Objects.equals(this.restrictedActions, territoryRestriction.restrictedActions) &&
        Objects.equals(this.curfew, territoryRestriction.curfew) &&
        Objects.equals(this.hazardLevel, territoryRestriction.hazardLevel) &&
        Objects.equals(this.requiredPermits, territoryRestriction.requiredPermits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zoneId, legalStatus, allowedTemplates, restrictedActions, curfew, hazardLevel, requiredPermits);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TerritoryRestriction {\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    legalStatus: ").append(toIndentedString(legalStatus)).append("\n");
    sb.append("    allowedTemplates: ").append(toIndentedString(allowedTemplates)).append("\n");
    sb.append("    restrictedActions: ").append(toIndentedString(restrictedActions)).append("\n");
    sb.append("    curfew: ").append(toIndentedString(curfew)).append("\n");
    sb.append("    hazardLevel: ").append(toIndentedString(hazardLevel)).append("\n");
    sb.append("    requiredPermits: ").append(toIndentedString(requiredPermits)).append("\n");
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

