package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.UIFeature;
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
 * GetUIFeatures200Response
 */

@JsonTypeName("getUIFeatures_200_response")

public class GetUIFeatures200Response {

  @Valid
  private List<@Valid UIFeature> features = new ArrayList<>();

  public GetUIFeatures200Response features(List<@Valid UIFeature> features) {
    this.features = features;
    return this;
  }

  public GetUIFeatures200Response addFeaturesItem(UIFeature featuresItem) {
    if (this.features == null) {
      this.features = new ArrayList<>();
    }
    this.features.add(featuresItem);
    return this;
  }

  /**
   * Get features
   * @return features
   */
  @Valid 
  @Schema(name = "features", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("features")
  public List<@Valid UIFeature> getFeatures() {
    return features;
  }

  public void setFeatures(List<@Valid UIFeature> features) {
    this.features = features;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetUIFeatures200Response getUIFeatures200Response = (GetUIFeatures200Response) o;
    return Objects.equals(this.features, getUIFeatures200Response.features);
  }

  @Override
  public int hashCode() {
    return Objects.hash(features);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetUIFeatures200Response {\n");
    sb.append("    features: ").append(toIndentedString(features)).append("\n");
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

