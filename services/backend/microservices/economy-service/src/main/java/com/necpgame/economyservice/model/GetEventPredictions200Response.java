package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.EventPrediction;
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
 * GetEventPredictions200Response
 */

@JsonTypeName("getEventPredictions_200_response")

public class GetEventPredictions200Response {

  @Valid
  private List<@Valid EventPrediction> predictions = new ArrayList<>();

  public GetEventPredictions200Response predictions(List<@Valid EventPrediction> predictions) {
    this.predictions = predictions;
    return this;
  }

  public GetEventPredictions200Response addPredictionsItem(EventPrediction predictionsItem) {
    if (this.predictions == null) {
      this.predictions = new ArrayList<>();
    }
    this.predictions.add(predictionsItem);
    return this;
  }

  /**
   * Get predictions
   * @return predictions
   */
  @Valid 
  @Schema(name = "predictions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("predictions")
  public List<@Valid EventPrediction> getPredictions() {
    return predictions;
  }

  public void setPredictions(List<@Valid EventPrediction> predictions) {
    this.predictions = predictions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetEventPredictions200Response getEventPredictions200Response = (GetEventPredictions200Response) o;
    return Objects.equals(this.predictions, getEventPredictions200Response.predictions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(predictions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetEventPredictions200Response {\n");
    sb.append("    predictions: ").append(toIndentedString(predictions)).append("\n");
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

