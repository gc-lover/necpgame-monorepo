package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.GetNetworkSecurity200ResponseIceProgramsInner;
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
 * GetNetworkSecurity200Response
 */

@JsonTypeName("getNetworkSecurity_200_response")

public class GetNetworkSecurity200Response {

  private @Nullable String networkId;

  private @Nullable Integer securityLevel;

  /**
   * Gets or Sets securityTier
   */
  public enum SecurityTierEnum {
    SIMPLE("simple"),
    
    MEDIUM("medium"),
    
    COMPLEX("complex"),
    
    CORPORATE("corporate"),
    
    GOVERNMENT("government");

    private final String value;

    SecurityTierEnum(String value) {
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
    public static SecurityTierEnum fromValue(String value) {
      for (SecurityTierEnum b : SecurityTierEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SecurityTierEnum securityTier;

  @Valid
  private List<@Valid GetNetworkSecurity200ResponseIceProgramsInner> icePrograms = new ArrayList<>();

  private @Nullable Integer alertLevel;

  private @Nullable Boolean traceActive;

  public GetNetworkSecurity200Response networkId(@Nullable String networkId) {
    this.networkId = networkId;
    return this;
  }

  /**
   * Get networkId
   * @return networkId
   */
  
  @Schema(name = "network_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("network_id")
  public @Nullable String getNetworkId() {
    return networkId;
  }

  public void setNetworkId(@Nullable String networkId) {
    this.networkId = networkId;
  }

  public GetNetworkSecurity200Response securityLevel(@Nullable Integer securityLevel) {
    this.securityLevel = securityLevel;
    return this;
  }

  /**
   * Get securityLevel
   * minimum: 1
   * maximum: 10
   * @return securityLevel
   */
  @Min(value = 1) @Max(value = 10) 
  @Schema(name = "security_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("security_level")
  public @Nullable Integer getSecurityLevel() {
    return securityLevel;
  }

  public void setSecurityLevel(@Nullable Integer securityLevel) {
    this.securityLevel = securityLevel;
  }

  public GetNetworkSecurity200Response securityTier(@Nullable SecurityTierEnum securityTier) {
    this.securityTier = securityTier;
    return this;
  }

  /**
   * Get securityTier
   * @return securityTier
   */
  
  @Schema(name = "security_tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("security_tier")
  public @Nullable SecurityTierEnum getSecurityTier() {
    return securityTier;
  }

  public void setSecurityTier(@Nullable SecurityTierEnum securityTier) {
    this.securityTier = securityTier;
  }

  public GetNetworkSecurity200Response icePrograms(List<@Valid GetNetworkSecurity200ResponseIceProgramsInner> icePrograms) {
    this.icePrograms = icePrograms;
    return this;
  }

  public GetNetworkSecurity200Response addIceProgramsItem(GetNetworkSecurity200ResponseIceProgramsInner iceProgramsItem) {
    if (this.icePrograms == null) {
      this.icePrograms = new ArrayList<>();
    }
    this.icePrograms.add(iceProgramsItem);
    return this;
  }

  /**
   * Get icePrograms
   * @return icePrograms
   */
  @Valid 
  @Schema(name = "ice_programs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ice_programs")
  public List<@Valid GetNetworkSecurity200ResponseIceProgramsInner> getIcePrograms() {
    return icePrograms;
  }

  public void setIcePrograms(List<@Valid GetNetworkSecurity200ResponseIceProgramsInner> icePrograms) {
    this.icePrograms = icePrograms;
  }

  public GetNetworkSecurity200Response alertLevel(@Nullable Integer alertLevel) {
    this.alertLevel = alertLevel;
    return this;
  }

  /**
   * Текущий уровень тревоги (0-5)
   * @return alertLevel
   */
  
  @Schema(name = "alert_level", description = "Текущий уровень тревоги (0-5)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alert_level")
  public @Nullable Integer getAlertLevel() {
    return alertLevel;
  }

  public void setAlertLevel(@Nullable Integer alertLevel) {
    this.alertLevel = alertLevel;
  }

  public GetNetworkSecurity200Response traceActive(@Nullable Boolean traceActive) {
    this.traceActive = traceActive;
    return this;
  }

  /**
   * Get traceActive
   * @return traceActive
   */
  
  @Schema(name = "trace_active", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trace_active")
  public @Nullable Boolean getTraceActive() {
    return traceActive;
  }

  public void setTraceActive(@Nullable Boolean traceActive) {
    this.traceActive = traceActive;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetNetworkSecurity200Response getNetworkSecurity200Response = (GetNetworkSecurity200Response) o;
    return Objects.equals(this.networkId, getNetworkSecurity200Response.networkId) &&
        Objects.equals(this.securityLevel, getNetworkSecurity200Response.securityLevel) &&
        Objects.equals(this.securityTier, getNetworkSecurity200Response.securityTier) &&
        Objects.equals(this.icePrograms, getNetworkSecurity200Response.icePrograms) &&
        Objects.equals(this.alertLevel, getNetworkSecurity200Response.alertLevel) &&
        Objects.equals(this.traceActive, getNetworkSecurity200Response.traceActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(networkId, securityLevel, securityTier, icePrograms, alertLevel, traceActive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetNetworkSecurity200Response {\n");
    sb.append("    networkId: ").append(toIndentedString(networkId)).append("\n");
    sb.append("    securityLevel: ").append(toIndentedString(securityLevel)).append("\n");
    sb.append("    securityTier: ").append(toIndentedString(securityTier)).append("\n");
    sb.append("    icePrograms: ").append(toIndentedString(icePrograms)).append("\n");
    sb.append("    alertLevel: ").append(toIndentedString(alertLevel)).append("\n");
    sb.append("    traceActive: ").append(toIndentedString(traceActive)).append("\n");
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

