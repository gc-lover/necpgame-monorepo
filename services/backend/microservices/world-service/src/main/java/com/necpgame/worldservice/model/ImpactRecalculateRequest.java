package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ImpactRecalculateRequest
 */


public class ImpactRecalculateRequest {

  /**
   * Gets or Sets scope
   */
  public enum ScopeEnum {
    GLOBAL("global"),
    
    CITIES("cities"),
    
    FACTIONS("factions"),
    
    CUSTOM("custom");

    private final String value;

    ScopeEnum(String value) {
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
    public static ScopeEnum fromValue(String value) {
      for (ScopeEnum b : ScopeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ScopeEnum scope;

  @Valid
  private List<@Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$")String> cityIds = new ArrayList<>();

  @Valid
  private List<@Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$")String> factionIds = new ArrayList<>();

  /**
   * Gets or Sets period
   */
  public enum PeriodEnum {
    _24H("24h"),
    
    _7D("7d"),
    
    _30D("30d"),
    
    SEASON("season"),
    
    YEAR("year");

    private final String value;

    PeriodEnum(String value) {
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
    public static PeriodEnum fromValue(String value) {
      for (PeriodEnum b : PeriodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PeriodEnum period;

  /**
   * Gets or Sets triggerSource
   */
  public enum TriggerSourceEnum {
    SCHEDULED("scheduled"),
    
    MANUAL("manual"),
    
    ANOMALY_DETECTED("anomaly_detected"),
    
    CRISIS_ESCALATION("crisis_escalation");

    private final String value;

    TriggerSourceEnum(String value) {
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
    public static TriggerSourceEnum fromValue(String value) {
      for (TriggerSourceEnum b : TriggerSourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TriggerSourceEnum triggerSource;

  private Boolean force = false;

  private @Nullable UUID correlationId;

  private @Nullable UUID requestedBy;

  public ImpactRecalculateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImpactRecalculateRequest(ScopeEnum scope, PeriodEnum period, TriggerSourceEnum triggerSource) {
    this.scope = scope;
    this.period = period;
    this.triggerSource = triggerSource;
  }

  public ImpactRecalculateRequest scope(ScopeEnum scope) {
    this.scope = scope;
    return this;
  }

  /**
   * Get scope
   * @return scope
   */
  @NotNull 
  @Schema(name = "scope", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("scope")
  public ScopeEnum getScope() {
    return scope;
  }

  public void setScope(ScopeEnum scope) {
    this.scope = scope;
  }

  public ImpactRecalculateRequest cityIds(List<@Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$")String> cityIds) {
    this.cityIds = cityIds;
    return this;
  }

  public ImpactRecalculateRequest addCityIdsItem(String cityIdsItem) {
    if (this.cityIds == null) {
      this.cityIds = new ArrayList<>();
    }
    this.cityIds.add(cityIdsItem);
    return this;
  }

  /**
   * Get cityIds
   * @return cityIds
   */
  @Size(min = 1) 
  @Schema(name = "cityIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cityIds")
  public List<@Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$")String> getCityIds() {
    return cityIds;
  }

  public void setCityIds(List<@Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$")String> cityIds) {
    this.cityIds = cityIds;
  }

  public ImpactRecalculateRequest factionIds(List<@Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$")String> factionIds) {
    this.factionIds = factionIds;
    return this;
  }

  public ImpactRecalculateRequest addFactionIdsItem(String factionIdsItem) {
    if (this.factionIds == null) {
      this.factionIds = new ArrayList<>();
    }
    this.factionIds.add(factionIdsItem);
    return this;
  }

  /**
   * Get factionIds
   * @return factionIds
   */
  
  @Schema(name = "factionIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionIds")
  public List<@Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$")String> getFactionIds() {
    return factionIds;
  }

  public void setFactionIds(List<@Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$")String> factionIds) {
    this.factionIds = factionIds;
  }

  public ImpactRecalculateRequest period(PeriodEnum period) {
    this.period = period;
    return this;
  }

  /**
   * Get period
   * @return period
   */
  @NotNull 
  @Schema(name = "period", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("period")
  public PeriodEnum getPeriod() {
    return period;
  }

  public void setPeriod(PeriodEnum period) {
    this.period = period;
  }

  public ImpactRecalculateRequest triggerSource(TriggerSourceEnum triggerSource) {
    this.triggerSource = triggerSource;
    return this;
  }

  /**
   * Get triggerSource
   * @return triggerSource
   */
  @NotNull 
  @Schema(name = "triggerSource", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("triggerSource")
  public TriggerSourceEnum getTriggerSource() {
    return triggerSource;
  }

  public void setTriggerSource(TriggerSourceEnum triggerSource) {
    this.triggerSource = triggerSource;
  }

  public ImpactRecalculateRequest force(Boolean force) {
    this.force = force;
    return this;
  }

  /**
   * Get force
   * @return force
   */
  
  @Schema(name = "force", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("force")
  public Boolean getForce() {
    return force;
  }

  public void setForce(Boolean force) {
    this.force = force;
  }

  public ImpactRecalculateRequest correlationId(@Nullable UUID correlationId) {
    this.correlationId = correlationId;
    return this;
  }

  /**
   * Get correlationId
   * @return correlationId
   */
  @Valid 
  @Schema(name = "correlationId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("correlationId")
  public @Nullable UUID getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(@Nullable UUID correlationId) {
    this.correlationId = correlationId;
  }

  public ImpactRecalculateRequest requestedBy(@Nullable UUID requestedBy) {
    this.requestedBy = requestedBy;
    return this;
  }

  /**
   * Get requestedBy
   * @return requestedBy
   */
  @Valid 
  @Schema(name = "requestedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requestedBy")
  public @Nullable UUID getRequestedBy() {
    return requestedBy;
  }

  public void setRequestedBy(@Nullable UUID requestedBy) {
    this.requestedBy = requestedBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImpactRecalculateRequest impactRecalculateRequest = (ImpactRecalculateRequest) o;
    return Objects.equals(this.scope, impactRecalculateRequest.scope) &&
        Objects.equals(this.cityIds, impactRecalculateRequest.cityIds) &&
        Objects.equals(this.factionIds, impactRecalculateRequest.factionIds) &&
        Objects.equals(this.period, impactRecalculateRequest.period) &&
        Objects.equals(this.triggerSource, impactRecalculateRequest.triggerSource) &&
        Objects.equals(this.force, impactRecalculateRequest.force) &&
        Objects.equals(this.correlationId, impactRecalculateRequest.correlationId) &&
        Objects.equals(this.requestedBy, impactRecalculateRequest.requestedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scope, cityIds, factionIds, period, triggerSource, force, correlationId, requestedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImpactRecalculateRequest {\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    cityIds: ").append(toIndentedString(cityIds)).append("\n");
    sb.append("    factionIds: ").append(toIndentedString(factionIds)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    triggerSource: ").append(toIndentedString(triggerSource)).append("\n");
    sb.append("    force: ").append(toIndentedString(force)).append("\n");
    sb.append("    correlationId: ").append(toIndentedString(correlationId)).append("\n");
    sb.append("    requestedBy: ").append(toIndentedString(requestedBy)).append("\n");
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

