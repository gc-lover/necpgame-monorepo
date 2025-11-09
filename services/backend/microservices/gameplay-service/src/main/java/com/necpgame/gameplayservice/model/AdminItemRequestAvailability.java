package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AdminItemRequestAvailability
 */

@JsonTypeName("AdminItemRequest_availability")

public class AdminItemRequestAvailability {

  private @Nullable Boolean purchasable;

  @Valid
  private List<String> rotationTags = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endAt;

  public AdminItemRequestAvailability purchasable(@Nullable Boolean purchasable) {
    this.purchasable = purchasable;
    return this;
  }

  /**
   * Get purchasable
   * @return purchasable
   */
  
  @Schema(name = "purchasable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("purchasable")
  public @Nullable Boolean getPurchasable() {
    return purchasable;
  }

  public void setPurchasable(@Nullable Boolean purchasable) {
    this.purchasable = purchasable;
  }

  public AdminItemRequestAvailability rotationTags(List<String> rotationTags) {
    this.rotationTags = rotationTags;
    return this;
  }

  public AdminItemRequestAvailability addRotationTagsItem(String rotationTagsItem) {
    if (this.rotationTags == null) {
      this.rotationTags = new ArrayList<>();
    }
    this.rotationTags.add(rotationTagsItem);
    return this;
  }

  /**
   * Get rotationTags
   * @return rotationTags
   */
  
  @Schema(name = "rotationTags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rotationTags")
  public List<String> getRotationTags() {
    return rotationTags;
  }

  public void setRotationTags(List<String> rotationTags) {
    this.rotationTags = rotationTags;
  }

  public AdminItemRequestAvailability startAt(@Nullable OffsetDateTime startAt) {
    this.startAt = startAt;
    return this;
  }

  /**
   * Get startAt
   * @return startAt
   */
  @Valid 
  @Schema(name = "startAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startAt")
  public @Nullable OffsetDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(@Nullable OffsetDateTime startAt) {
    this.startAt = startAt;
  }

  public AdminItemRequestAvailability endAt(@Nullable OffsetDateTime endAt) {
    this.endAt = endAt;
    return this;
  }

  /**
   * Get endAt
   * @return endAt
   */
  @Valid 
  @Schema(name = "endAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endAt")
  public @Nullable OffsetDateTime getEndAt() {
    return endAt;
  }

  public void setEndAt(@Nullable OffsetDateTime endAt) {
    this.endAt = endAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminItemRequestAvailability adminItemRequestAvailability = (AdminItemRequestAvailability) o;
    return Objects.equals(this.purchasable, adminItemRequestAvailability.purchasable) &&
        Objects.equals(this.rotationTags, adminItemRequestAvailability.rotationTags) &&
        Objects.equals(this.startAt, adminItemRequestAvailability.startAt) &&
        Objects.equals(this.endAt, adminItemRequestAvailability.endAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(purchasable, rotationTags, startAt, endAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminItemRequestAvailability {\n");
    sb.append("    purchasable: ").append(toIndentedString(purchasable)).append("\n");
    sb.append("    rotationTags: ").append(toIndentedString(rotationTags)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
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

