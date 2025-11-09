package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.RotationUpdateRequestPublishOptions;
import com.necpgame.gameplayservice.model.ShopRotation;
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
 * RotationUpdateRequest
 */


public class RotationUpdateRequest {

  private String region;

  @Valid
  private List<@Valid ShopRotation> rotations = new ArrayList<>();

  private @Nullable RotationUpdateRequestPublishOptions publishOptions;

  public RotationUpdateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RotationUpdateRequest(String region, List<@Valid ShopRotation> rotations) {
    this.region = region;
    this.rotations = rotations;
  }

  public RotationUpdateRequest region(String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  @NotNull 
  @Schema(name = "region", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("region")
  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public RotationUpdateRequest rotations(List<@Valid ShopRotation> rotations) {
    this.rotations = rotations;
    return this;
  }

  public RotationUpdateRequest addRotationsItem(ShopRotation rotationsItem) {
    if (this.rotations == null) {
      this.rotations = new ArrayList<>();
    }
    this.rotations.add(rotationsItem);
    return this;
  }

  /**
   * Get rotations
   * @return rotations
   */
  @NotNull @Valid 
  @Schema(name = "rotations", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rotations")
  public List<@Valid ShopRotation> getRotations() {
    return rotations;
  }

  public void setRotations(List<@Valid ShopRotation> rotations) {
    this.rotations = rotations;
  }

  public RotationUpdateRequest publishOptions(@Nullable RotationUpdateRequestPublishOptions publishOptions) {
    this.publishOptions = publishOptions;
    return this;
  }

  /**
   * Get publishOptions
   * @return publishOptions
   */
  @Valid 
  @Schema(name = "publishOptions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("publishOptions")
  public @Nullable RotationUpdateRequestPublishOptions getPublishOptions() {
    return publishOptions;
  }

  public void setPublishOptions(@Nullable RotationUpdateRequestPublishOptions publishOptions) {
    this.publishOptions = publishOptions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RotationUpdateRequest rotationUpdateRequest = (RotationUpdateRequest) o;
    return Objects.equals(this.region, rotationUpdateRequest.region) &&
        Objects.equals(this.rotations, rotationUpdateRequest.rotations) &&
        Objects.equals(this.publishOptions, rotationUpdateRequest.publishOptions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(region, rotations, publishOptions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RotationUpdateRequest {\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    rotations: ").append(toIndentedString(rotations)).append("\n");
    sb.append("    publishOptions: ").append(toIndentedString(publishOptions)).append("\n");
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

