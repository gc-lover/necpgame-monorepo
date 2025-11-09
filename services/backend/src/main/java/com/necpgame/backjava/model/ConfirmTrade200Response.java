package com.necpgame.backjava.model;

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
 * ConfirmTrade200Response
 */

@JsonTypeName("confirmTrade_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ConfirmTrade200Response {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    WAITING_OTHER("waiting_other"),
    
    COMPLETED("completed");

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

  private @Nullable Boolean tradeCompleted;

  public ConfirmTrade200Response status(@Nullable StatusEnum status) {
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

  public ConfirmTrade200Response tradeCompleted(@Nullable Boolean tradeCompleted) {
    this.tradeCompleted = tradeCompleted;
    return this;
  }

  /**
   * Get tradeCompleted
   * @return tradeCompleted
   */
  
  @Schema(name = "trade_completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trade_completed")
  public @Nullable Boolean getTradeCompleted() {
    return tradeCompleted;
  }

  public void setTradeCompleted(@Nullable Boolean tradeCompleted) {
    this.tradeCompleted = tradeCompleted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfirmTrade200Response confirmTrade200Response = (ConfirmTrade200Response) o;
    return Objects.equals(this.status, confirmTrade200Response.status) &&
        Objects.equals(this.tradeCompleted, confirmTrade200Response.tradeCompleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, tradeCompleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfirmTrade200Response {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    tradeCompleted: ").append(toIndentedString(tradeCompleted)).append("\n");
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

