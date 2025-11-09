package com.necpgame.economyservice.model;

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
 * InitiateTrade200Response
 */

@JsonTypeName("initiateTrade_200_response")

public class InitiateTrade200Response {

  private @Nullable String tradeSessionId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("pending"),
    
    ACCEPTED("accepted"),
    
    DECLINED("declined");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  public InitiateTrade200Response tradeSessionId(@Nullable String tradeSessionId) {
    this.tradeSessionId = tradeSessionId;
    return this;
  }

  /**
   * Get tradeSessionId
   * @return tradeSessionId
   */
  
  @Schema(name = "trade_session_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trade_session_id")
  public @Nullable String getTradeSessionId() {
    return tradeSessionId;
  }

  public void setTradeSessionId(@Nullable String tradeSessionId) {
    this.tradeSessionId = tradeSessionId;
  }

  public InitiateTrade200Response status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InitiateTrade200Response initiateTrade200Response = (InitiateTrade200Response) o;
    return Objects.equals(this.tradeSessionId, initiateTrade200Response.tradeSessionId) &&
        Objects.equals(this.status, initiateTrade200Response.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tradeSessionId, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InitiateTrade200Response {\n");
    sb.append("    tradeSessionId: ").append(toIndentedString(tradeSessionId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

