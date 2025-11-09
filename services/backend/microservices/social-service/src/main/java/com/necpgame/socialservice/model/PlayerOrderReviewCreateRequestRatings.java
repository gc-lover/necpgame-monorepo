package com.necpgame.socialservice.model;

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
 * PlayerOrderReviewCreateRequestRatings
 */

@JsonTypeName("PlayerOrderReviewCreateRequest_ratings")

public class PlayerOrderReviewCreateRequestRatings {

  private Integer quality;

  private Integer communication;

  public PlayerOrderReviewCreateRequestRatings() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderReviewCreateRequestRatings(Integer quality, Integer communication) {
    this.quality = quality;
    this.communication = communication;
  }

  public PlayerOrderReviewCreateRequestRatings quality(Integer quality) {
    this.quality = quality;
    return this;
  }

  /**
   * Get quality
   * minimum: 1
   * maximum: 5
   * @return quality
   */
  @NotNull @Min(value = 1) @Max(value = 5) 
  @Schema(name = "quality", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quality")
  public Integer getQuality() {
    return quality;
  }

  public void setQuality(Integer quality) {
    this.quality = quality;
  }

  public PlayerOrderReviewCreateRequestRatings communication(Integer communication) {
    this.communication = communication;
    return this;
  }

  /**
   * Get communication
   * minimum: 1
   * maximum: 5
   * @return communication
   */
  @NotNull @Min(value = 1) @Max(value = 5) 
  @Schema(name = "communication", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("communication")
  public Integer getCommunication() {
    return communication;
  }

  public void setCommunication(Integer communication) {
    this.communication = communication;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderReviewCreateRequestRatings playerOrderReviewCreateRequestRatings = (PlayerOrderReviewCreateRequestRatings) o;
    return Objects.equals(this.quality, playerOrderReviewCreateRequestRatings.quality) &&
        Objects.equals(this.communication, playerOrderReviewCreateRequestRatings.communication);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quality, communication);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderReviewCreateRequestRatings {\n");
    sb.append("    quality: ").append(toIndentedString(quality)).append("\n");
    sb.append("    communication: ").append(toIndentedString(communication)).append("\n");
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

