package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ShopRotationResponse
 */


public class ShopRotationResponse {

  private @Nullable String region;

  @Valid
  private List<@Valid ShopRotation> rotations = new ArrayList<>();

  public ShopRotationResponse region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public ShopRotationResponse rotations(List<@Valid ShopRotation> rotations) {
    this.rotations = rotations;
    return this;
  }

  public ShopRotationResponse addRotationsItem(ShopRotation rotationsItem) {
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
  @Valid 
  @Schema(name = "rotations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rotations")
  public List<@Valid ShopRotation> getRotations() {
    return rotations;
  }

  public void setRotations(List<@Valid ShopRotation> rotations) {
    this.rotations = rotations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShopRotationResponse shopRotationResponse = (ShopRotationResponse) o;
    return Objects.equals(this.region, shopRotationResponse.region) &&
        Objects.equals(this.rotations, shopRotationResponse.rotations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(region, rotations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShopRotationResponse {\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    rotations: ").append(toIndentedString(rotations)).append("\n");
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

