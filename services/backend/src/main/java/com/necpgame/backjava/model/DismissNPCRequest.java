package com.necpgame.backjava.model;

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
 * DismissNPCRequest
 */

@JsonTypeName("dismissNPC_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class DismissNPCRequest {

  private @Nullable String reason;

  private @Nullable Integer severancePay;

  public DismissNPCRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public DismissNPCRequest severancePay(@Nullable Integer severancePay) {
    this.severancePay = severancePay;
    return this;
  }

  /**
   * Выходное пособие
   * @return severancePay
   */
  
  @Schema(name = "severance_pay", description = "Выходное пособие", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severance_pay")
  public @Nullable Integer getSeverancePay() {
    return severancePay;
  }

  public void setSeverancePay(@Nullable Integer severancePay) {
    this.severancePay = severancePay;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DismissNPCRequest dismissNPCRequest = (DismissNPCRequest) o;
    return Objects.equals(this.reason, dismissNPCRequest.reason) &&
        Objects.equals(this.severancePay, dismissNPCRequest.severancePay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, severancePay);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DismissNPCRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    severancePay: ").append(toIndentedString(severancePay)).append("\n");
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

