package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
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
 * HackNetwork200Response
 */

@JsonTypeName("hackNetwork_200_response")

public class HackNetwork200Response {

  private @Nullable Boolean success;

  private @Nullable String networkId;

  /**
   * Gets or Sets accessLevel
   */
  public enum AccessLevelEnum {
    READ("read"),
    
    CONTROL("control"),
    
    ADMIN("admin");

    private final String value;

    AccessLevelEnum(String value) {
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
    public static AccessLevelEnum fromValue(String value) {
      for (AccessLevelEnum b : AccessLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AccessLevelEnum accessLevel;

  @Valid
  private List<String> compromisedNodes = new ArrayList<>();

  private @Nullable BigDecimal traceRisk;

  private @Nullable BigDecimal timeTaken;

  public HackNetwork200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public HackNetwork200Response networkId(@Nullable String networkId) {
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

  public HackNetwork200Response accessLevel(@Nullable AccessLevelEnum accessLevel) {
    this.accessLevel = accessLevel;
    return this;
  }

  /**
   * Get accessLevel
   * @return accessLevel
   */
  
  @Schema(name = "access_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("access_level")
  public @Nullable AccessLevelEnum getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(@Nullable AccessLevelEnum accessLevel) {
    this.accessLevel = accessLevel;
  }

  public HackNetwork200Response compromisedNodes(List<String> compromisedNodes) {
    this.compromisedNodes = compromisedNodes;
    return this;
  }

  public HackNetwork200Response addCompromisedNodesItem(String compromisedNodesItem) {
    if (this.compromisedNodes == null) {
      this.compromisedNodes = new ArrayList<>();
    }
    this.compromisedNodes.add(compromisedNodesItem);
    return this;
  }

  /**
   * Get compromisedNodes
   * @return compromisedNodes
   */
  
  @Schema(name = "compromised_nodes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compromised_nodes")
  public List<String> getCompromisedNodes() {
    return compromisedNodes;
  }

  public void setCompromisedNodes(List<String> compromisedNodes) {
    this.compromisedNodes = compromisedNodes;
  }

  public HackNetwork200Response traceRisk(@Nullable BigDecimal traceRisk) {
    this.traceRisk = traceRisk;
    return this;
  }

  /**
   * Риск отследить (%)
   * @return traceRisk
   */
  @Valid 
  @Schema(name = "trace_risk", description = "Риск отследить (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trace_risk")
  public @Nullable BigDecimal getTraceRisk() {
    return traceRisk;
  }

  public void setTraceRisk(@Nullable BigDecimal traceRisk) {
    this.traceRisk = traceRisk;
  }

  public HackNetwork200Response timeTaken(@Nullable BigDecimal timeTaken) {
    this.timeTaken = timeTaken;
    return this;
  }

  /**
   * Время взлома (секунды)
   * @return timeTaken
   */
  @Valid 
  @Schema(name = "time_taken", description = "Время взлома (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_taken")
  public @Nullable BigDecimal getTimeTaken() {
    return timeTaken;
  }

  public void setTimeTaken(@Nullable BigDecimal timeTaken) {
    this.timeTaken = timeTaken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HackNetwork200Response hackNetwork200Response = (HackNetwork200Response) o;
    return Objects.equals(this.success, hackNetwork200Response.success) &&
        Objects.equals(this.networkId, hackNetwork200Response.networkId) &&
        Objects.equals(this.accessLevel, hackNetwork200Response.accessLevel) &&
        Objects.equals(this.compromisedNodes, hackNetwork200Response.compromisedNodes) &&
        Objects.equals(this.traceRisk, hackNetwork200Response.traceRisk) &&
        Objects.equals(this.timeTaken, hackNetwork200Response.timeTaken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, networkId, accessLevel, compromisedNodes, traceRisk, timeTaken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HackNetwork200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    networkId: ").append(toIndentedString(networkId)).append("\n");
    sb.append("    accessLevel: ").append(toIndentedString(accessLevel)).append("\n");
    sb.append("    compromisedNodes: ").append(toIndentedString(compromisedNodes)).append("\n");
    sb.append("    traceRisk: ").append(toIndentedString(traceRisk)).append("\n");
    sb.append("    timeTaken: ").append(toIndentedString(timeTaken)).append("\n");
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

