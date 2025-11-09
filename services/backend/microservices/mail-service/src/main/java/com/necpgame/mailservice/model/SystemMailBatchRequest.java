package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.mailservice.model.SystemMailRequest;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SystemMailBatchRequest
 */


public class SystemMailBatchRequest {

  private String batchId;

  private SystemMailRequest systemMail;

  private @Nullable String idempotencyKey;

  public SystemMailBatchRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SystemMailBatchRequest(String batchId, SystemMailRequest systemMail) {
    this.batchId = batchId;
    this.systemMail = systemMail;
  }

  public SystemMailBatchRequest batchId(String batchId) {
    this.batchId = batchId;
    return this;
  }

  /**
   * Get batchId
   * @return batchId
   */
  @NotNull 
  @Schema(name = "batchId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("batchId")
  public String getBatchId() {
    return batchId;
  }

  public void setBatchId(String batchId) {
    this.batchId = batchId;
  }

  public SystemMailBatchRequest systemMail(SystemMailRequest systemMail) {
    this.systemMail = systemMail;
    return this;
  }

  /**
   * Get systemMail
   * @return systemMail
   */
  @NotNull @Valid 
  @Schema(name = "systemMail", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("systemMail")
  public SystemMailRequest getSystemMail() {
    return systemMail;
  }

  public void setSystemMail(SystemMailRequest systemMail) {
    this.systemMail = systemMail;
  }

  public SystemMailBatchRequest idempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
    return this;
  }

  /**
   * Get idempotencyKey
   * @return idempotencyKey
   */
  
  @Schema(name = "idempotencyKey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("idempotencyKey")
  public @Nullable String getIdempotencyKey() {
    return idempotencyKey;
  }

  public void setIdempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemMailBatchRequest systemMailBatchRequest = (SystemMailBatchRequest) o;
    return Objects.equals(this.batchId, systemMailBatchRequest.batchId) &&
        Objects.equals(this.systemMail, systemMailBatchRequest.systemMail) &&
        Objects.equals(this.idempotencyKey, systemMailBatchRequest.idempotencyKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(batchId, systemMail, idempotencyKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemMailBatchRequest {\n");
    sb.append("    batchId: ").append(toIndentedString(batchId)).append("\n");
    sb.append("    systemMail: ").append(toIndentedString(systemMail)).append("\n");
    sb.append("    idempotencyKey: ").append(toIndentedString(idempotencyKey)).append("\n");
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

