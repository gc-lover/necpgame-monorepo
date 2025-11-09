package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.RatingMetricSet;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Нормализованные показатели, участвующие в расчёте рейтинга.
 */

@Schema(name = "RatingMetrics", description = "Нормализованные показатели, участвующие в расчёте рейтинга.")

public class RatingMetrics {

  private @Nullable RatingMetricSet executor;

  private @Nullable RatingMetricSet client;

  public RatingMetrics executor(@Nullable RatingMetricSet executor) {
    this.executor = executor;
    return this;
  }

  /**
   * Get executor
   * @return executor
   */
  @Valid 
  @Schema(name = "executor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("executor")
  public @Nullable RatingMetricSet getExecutor() {
    return executor;
  }

  public void setExecutor(@Nullable RatingMetricSet executor) {
    this.executor = executor;
  }

  public RatingMetrics client(@Nullable RatingMetricSet client) {
    this.client = client;
    return this;
  }

  /**
   * Get client
   * @return client
   */
  @Valid 
  @Schema(name = "client", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("client")
  public @Nullable RatingMetricSet getClient() {
    return client;
  }

  public void setClient(@Nullable RatingMetricSet client) {
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
    RatingMetrics ratingMetrics = (RatingMetrics) o;
    return Objects.equals(this.executor, ratingMetrics.executor) &&
        Objects.equals(this.client, ratingMetrics.client);
  }

  @Override
  public int hashCode() {
    return Objects.hash(executor, client);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingMetrics {\n");
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

