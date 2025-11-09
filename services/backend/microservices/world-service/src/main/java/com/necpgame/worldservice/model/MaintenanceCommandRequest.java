package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * MaintenanceCommandRequest
 */


public class MaintenanceCommandRequest {

  private String reason;

  private String initiatedBy;

  /**
   * Gets or Sets scope
   */
  public enum ScopeEnum {
    WORLD_SERVICE("world_service"),
    
    GAMEPLAY_SERVICE("gameplay_service"),
    
    COMBINED("combined");

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

  private ScopeEnum scope = ScopeEnum.COMBINED;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expectedResumeAt;

  @Valid
  private List<String> notifyChannels = new ArrayList<>();

  public MaintenanceCommandRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MaintenanceCommandRequest(String reason, String initiatedBy) {
    this.reason = reason;
    this.initiatedBy = initiatedBy;
  }

  public MaintenanceCommandRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public MaintenanceCommandRequest initiatedBy(String initiatedBy) {
    this.initiatedBy = initiatedBy;
    return this;
  }

  /**
   * Get initiatedBy
   * @return initiatedBy
   */
  @NotNull 
  @Schema(name = "initiatedBy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("initiatedBy")
  public String getInitiatedBy() {
    return initiatedBy;
  }

  public void setInitiatedBy(String initiatedBy) {
    this.initiatedBy = initiatedBy;
  }

  public MaintenanceCommandRequest scope(ScopeEnum scope) {
    this.scope = scope;
    return this;
  }

  /**
   * Get scope
   * @return scope
   */
  
  @Schema(name = "scope", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scope")
  public ScopeEnum getScope() {
    return scope;
  }

  public void setScope(ScopeEnum scope) {
    this.scope = scope;
  }

  public MaintenanceCommandRequest expectedResumeAt(@Nullable OffsetDateTime expectedResumeAt) {
    this.expectedResumeAt = expectedResumeAt;
    return this;
  }

  /**
   * Get expectedResumeAt
   * @return expectedResumeAt
   */
  @Valid 
  @Schema(name = "expectedResumeAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expectedResumeAt")
  public @Nullable OffsetDateTime getExpectedResumeAt() {
    return expectedResumeAt;
  }

  public void setExpectedResumeAt(@Nullable OffsetDateTime expectedResumeAt) {
    this.expectedResumeAt = expectedResumeAt;
  }

  public MaintenanceCommandRequest notifyChannels(List<String> notifyChannels) {
    this.notifyChannels = notifyChannels;
    return this;
  }

  public MaintenanceCommandRequest addNotifyChannelsItem(String notifyChannelsItem) {
    if (this.notifyChannels == null) {
      this.notifyChannels = new ArrayList<>();
    }
    this.notifyChannels.add(notifyChannelsItem);
    return this;
  }

  /**
   * Get notifyChannels
   * @return notifyChannels
   */
  
  @Schema(name = "notifyChannels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyChannels")
  public List<String> getNotifyChannels() {
    return notifyChannels;
  }

  public void setNotifyChannels(List<String> notifyChannels) {
    this.notifyChannels = notifyChannels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaintenanceCommandRequest maintenanceCommandRequest = (MaintenanceCommandRequest) o;
    return Objects.equals(this.reason, maintenanceCommandRequest.reason) &&
        Objects.equals(this.initiatedBy, maintenanceCommandRequest.initiatedBy) &&
        Objects.equals(this.scope, maintenanceCommandRequest.scope) &&
        Objects.equals(this.expectedResumeAt, maintenanceCommandRequest.expectedResumeAt) &&
        Objects.equals(this.notifyChannels, maintenanceCommandRequest.notifyChannels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, initiatedBy, scope, expectedResumeAt, notifyChannels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaintenanceCommandRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    initiatedBy: ").append(toIndentedString(initiatedBy)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    expectedResumeAt: ").append(toIndentedString(expectedResumeAt)).append("\n");
    sb.append("    notifyChannels: ").append(toIndentedString(notifyChannels)).append("\n");
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

