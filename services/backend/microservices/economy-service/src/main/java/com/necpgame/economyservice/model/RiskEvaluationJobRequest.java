package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * RiskEvaluationJobRequest
 */


public class RiskEvaluationJobRequest {

  private UUID requesterId;

  /**
   * Gets or Sets scope
   */
  public enum ScopeEnum {
    ORDERS("orders"),
    
    PLAYERS("players"),
    
    REGIONS("regions");

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
  private List<UUID> orderIds = new ArrayList<>();

  @Valid
  private List<UUID> playerIds = new ArrayList<>();

  @Valid
  private List<@Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$")String> regionIds = new ArrayList<>();

  @Valid
  private Map<String, Float> parameters = new HashMap<>();

  public RiskEvaluationJobRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RiskEvaluationJobRequest(UUID requesterId, ScopeEnum scope) {
    this.requesterId = requesterId;
    this.scope = scope;
  }

  public RiskEvaluationJobRequest requesterId(UUID requesterId) {
    this.requesterId = requesterId;
    return this;
  }

  /**
   * Get requesterId
   * @return requesterId
   */
  @NotNull @Valid 
  @Schema(name = "requesterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("requesterId")
  public UUID getRequesterId() {
    return requesterId;
  }

  public void setRequesterId(UUID requesterId) {
    this.requesterId = requesterId;
  }

  public RiskEvaluationJobRequest scope(ScopeEnum scope) {
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

  public RiskEvaluationJobRequest orderIds(List<UUID> orderIds) {
    this.orderIds = orderIds;
    return this;
  }

  public RiskEvaluationJobRequest addOrderIdsItem(UUID orderIdsItem) {
    if (this.orderIds == null) {
      this.orderIds = new ArrayList<>();
    }
    this.orderIds.add(orderIdsItem);
    return this;
  }

  /**
   * Get orderIds
   * @return orderIds
   */
  @Valid 
  @Schema(name = "orderIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("orderIds")
  public List<UUID> getOrderIds() {
    return orderIds;
  }

  public void setOrderIds(List<UUID> orderIds) {
    this.orderIds = orderIds;
  }

  public RiskEvaluationJobRequest playerIds(List<UUID> playerIds) {
    this.playerIds = playerIds;
    return this;
  }

  public RiskEvaluationJobRequest addPlayerIdsItem(UUID playerIdsItem) {
    if (this.playerIds == null) {
      this.playerIds = new ArrayList<>();
    }
    this.playerIds.add(playerIdsItem);
    return this;
  }

  /**
   * Get playerIds
   * @return playerIds
   */
  @Valid 
  @Schema(name = "playerIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerIds")
  public List<UUID> getPlayerIds() {
    return playerIds;
  }

  public void setPlayerIds(List<UUID> playerIds) {
    this.playerIds = playerIds;
  }

  public RiskEvaluationJobRequest regionIds(List<@Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$")String> regionIds) {
    this.regionIds = regionIds;
    return this;
  }

  public RiskEvaluationJobRequest addRegionIdsItem(String regionIdsItem) {
    if (this.regionIds == null) {
      this.regionIds = new ArrayList<>();
    }
    this.regionIds.add(regionIdsItem);
    return this;
  }

  /**
   * Get regionIds
   * @return regionIds
   */
  
  @Schema(name = "regionIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regionIds")
  public List<@Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$")String> getRegionIds() {
    return regionIds;
  }

  public void setRegionIds(List<@Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$")String> regionIds) {
    this.regionIds = regionIds;
  }

  public RiskEvaluationJobRequest parameters(Map<String, Float> parameters) {
    this.parameters = parameters;
    return this;
  }

  public RiskEvaluationJobRequest putParametersItem(String key, Float parametersItem) {
    if (this.parameters == null) {
      this.parameters = new HashMap<>();
    }
    this.parameters.put(key, parametersItem);
    return this;
  }

  /**
   * Get parameters
   * @return parameters
   */
  
  @Schema(name = "parameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parameters")
  public Map<String, Float> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Float> parameters) {
    this.parameters = parameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskEvaluationJobRequest riskEvaluationJobRequest = (RiskEvaluationJobRequest) o;
    return Objects.equals(this.requesterId, riskEvaluationJobRequest.requesterId) &&
        Objects.equals(this.scope, riskEvaluationJobRequest.scope) &&
        Objects.equals(this.orderIds, riskEvaluationJobRequest.orderIds) &&
        Objects.equals(this.playerIds, riskEvaluationJobRequest.playerIds) &&
        Objects.equals(this.regionIds, riskEvaluationJobRequest.regionIds) &&
        Objects.equals(this.parameters, riskEvaluationJobRequest.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requesterId, scope, orderIds, playerIds, regionIds, parameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskEvaluationJobRequest {\n");
    sb.append("    requesterId: ").append(toIndentedString(requesterId)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    orderIds: ").append(toIndentedString(orderIds)).append("\n");
    sb.append("    playerIds: ").append(toIndentedString(playerIds)).append("\n");
    sb.append("    regionIds: ").append(toIndentedString(regionIds)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
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

