package com.necpgame.economyservice.model;

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
 * RiskContextPlayerRatings
 */

@JsonTypeName("RiskContext_playerRatings")

public class RiskContextPlayerRatings {

  private @Nullable Float executor;

  private @Nullable Float client;

  public RiskContextPlayerRatings executor(@Nullable Float executor) {
    this.executor = executor;
    return this;
  }

  /**
   * Get executor
   * @return executor
   */
  
  @Schema(name = "executor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("executor")
  public @Nullable Float getExecutor() {
    return executor;
  }

  public void setExecutor(@Nullable Float executor) {
    this.executor = executor;
  }

  public RiskContextPlayerRatings client(@Nullable Float client) {
    this.client = client;
    return this;
  }

  /**
   * Get client
   * @return client
   */
  
  @Schema(name = "client", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("client")
  public @Nullable Float getClient() {
    return client;
  }

  public void setClient(@Nullable Float client) {
    this.client = client;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskContextPlayerRatings riskContextPlayerRatings = (RiskContextPlayerRatings) o;
    return Objects.equals(this.executor, riskContextPlayerRatings.executor) &&
        Objects.equals(this.client, riskContextPlayerRatings.client);
  }

  @Override
  public int hashCode() {
    return Objects.hash(executor, client);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskContextPlayerRatings {\n");
    sb.append("    executor: ").append(toIndentedString(executor)).append("\n");
    sb.append("    client: ").append(toIndentedString(client)).append("\n");
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

