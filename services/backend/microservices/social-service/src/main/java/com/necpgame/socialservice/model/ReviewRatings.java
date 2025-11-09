package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReviewRatings
 */


public class ReviewRatings {

  private Integer quality;

  private Integer communication;

  private @Nullable Integer professionalism;

  private @Nullable Integer fairness;

  public ReviewRatings() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewRatings(Integer quality, Integer communication) {
    this.quality = quality;
    this.communication = communication;
  }

  public ReviewRatings quality(Integer quality) {
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

  public ReviewRatings communication(Integer communication) {
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

  public ReviewRatings professionalism(@Nullable Integer professionalism) {
    this.professionalism = professionalism;
    return this;
  }

  /**
   * Get professionalism
   * minimum: 1
   * maximum: 5
   * @return professionalism
   */
  @Min(value = 1) @Max(value = 5) 
  @Schema(name = "professionalism", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("professionalism")
  public @Nullable Integer getProfessionalism() {
    return professionalism;
  }

  public void setProfessionalism(@Nullable Integer professionalism) {
    this.professionalism = professionalism;
  }

  public ReviewRatings fairness(@Nullable Integer fairness) {
    this.fairness = fairness;
    return this;
  }

  /**
   * Get fairness
   * minimum: 1
   * maximum: 5
   * @return fairness
   */
  @Min(value = 1) @Max(value = 5) 
  @Schema(name = "fairness", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fairness")
  public @Nullable Integer getFairness() {
    return fairness;
  }

  public void setFairness(@Nullable Integer fairness) {
    this.fairness = fairness;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewRatings reviewRatings = (ReviewRatings) o;
    return Objects.equals(this.quality, reviewRatings.quality) &&
        Objects.equals(this.communication, reviewRatings.communication) &&
        Objects.equals(this.professionalism, reviewRatings.professionalism) &&
        Objects.equals(this.fairness, reviewRatings.fairness);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quality, communication, professionalism, fairness);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewRatings {\n");
    sb.append("    quality: ").append(toIndentedString(quality)).append("\n");
    sb.append("    communication: ").append(toIndentedString(communication)).append("\n");
    sb.append("    professionalism: ").append(toIndentedString(professionalism)).append("\n");
    sb.append("    fairness: ").append(toIndentedString(fairness)).append("\n");
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

