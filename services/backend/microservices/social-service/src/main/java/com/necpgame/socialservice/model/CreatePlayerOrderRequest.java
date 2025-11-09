package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreatePlayerOrderRequest
 */

@JsonTypeName("createPlayerOrder_request")

public class CreatePlayerOrderRequest {

  private String creatorId;

  private String orderType;

  private String description;

  private BigDecimal reward;

  public CreatePlayerOrderRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreatePlayerOrderRequest(String creatorId, String orderType, String description, BigDecimal reward) {
    this.creatorId = creatorId;
    this.orderType = orderType;
    this.description = description;
    this.reward = reward;
  }

  public CreatePlayerOrderRequest creatorId(String creatorId) {
    this.creatorId = creatorId;
    return this;
  }

  /**
   * Get creatorId
   * @return creatorId
   */
  @NotNull 
  @Schema(name = "creator_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("creator_id")
  public String getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(String creatorId) {
    this.creatorId = creatorId;
  }

  public CreatePlayerOrderRequest orderType(String orderType) {
    this.orderType = orderType;
    return this;
  }

  /**
   * Get orderType
   * @return orderType
   */
  @NotNull 
  @Schema(name = "order_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("order_type")
  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public CreatePlayerOrderRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CreatePlayerOrderRequest reward(BigDecimal reward) {
    this.reward = reward;
    return this;
  }

  /**
   * Get reward
   * @return reward
   */
  @NotNull @Valid 
  @Schema(name = "reward", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reward")
  public BigDecimal getReward() {
    return reward;
  }

  public void setReward(BigDecimal reward) {
    this.reward = reward;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreatePlayerOrderRequest createPlayerOrderRequest = (CreatePlayerOrderRequest) o;
    return Objects.equals(this.creatorId, createPlayerOrderRequest.creatorId) &&
        Objects.equals(this.orderType, createPlayerOrderRequest.orderType) &&
        Objects.equals(this.description, createPlayerOrderRequest.description) &&
        Objects.equals(this.reward, createPlayerOrderRequest.reward);
  }

  @Override
  public int hashCode() {
    return Objects.hash(creatorId, orderType, description, reward);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreatePlayerOrderRequest {\n");
    sb.append("    creatorId: ").append(toIndentedString(creatorId)).append("\n");
    sb.append("    orderType: ").append(toIndentedString(orderType)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    reward: ").append(toIndentedString(reward)).append("\n");
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

