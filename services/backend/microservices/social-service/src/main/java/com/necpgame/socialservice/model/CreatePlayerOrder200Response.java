package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreatePlayerOrder200Response
 */

@JsonTypeName("createPlayerOrder_200_response")

public class CreatePlayerOrder200Response {

  private @Nullable String orderId;

  public CreatePlayerOrder200Response orderId(@Nullable String orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  
  @Schema(name = "order_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order_id")
  public @Nullable String getOrderId() {
    return orderId;
  }

  public void setOrderId(@Nullable String orderId) {
    this.orderId = orderId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreatePlayerOrder200Response createPlayerOrder200Response = (CreatePlayerOrder200Response) o;
    return Objects.equals(this.orderId, createPlayerOrder200Response.orderId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreatePlayerOrder200Response {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
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

