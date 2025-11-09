package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AdminItemRequestAssets
 */

@JsonTypeName("AdminItemRequest_assets")

public class AdminItemRequestAssets {

  private @Nullable URI iconUrl;

  private @Nullable URI previewUrl;

  private @Nullable URI hologramUrl;

  public AdminItemRequestAssets iconUrl(@Nullable URI iconUrl) {
    this.iconUrl = iconUrl;
    return this;
  }

  /**
   * Get iconUrl
   * @return iconUrl
   */
  @Valid 
  @Schema(name = "iconUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("iconUrl")
  public @Nullable URI getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(@Nullable URI iconUrl) {
    this.iconUrl = iconUrl;
  }

  public AdminItemRequestAssets previewUrl(@Nullable URI previewUrl) {
    this.previewUrl = previewUrl;
    return this;
  }

  /**
   * Get previewUrl
   * @return previewUrl
   */
  @Valid 
  @Schema(name = "previewUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previewUrl")
  public @Nullable URI getPreviewUrl() {
    return previewUrl;
  }

  public void setPreviewUrl(@Nullable URI previewUrl) {
    this.previewUrl = previewUrl;
  }

  public AdminItemRequestAssets hologramUrl(@Nullable URI hologramUrl) {
    this.hologramUrl = hologramUrl;
    return this;
  }

  /**
   * Get hologramUrl
   * @return hologramUrl
   */
  @Valid 
  @Schema(name = "hologramUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hologramUrl")
  public @Nullable URI getHologramUrl() {
    return hologramUrl;
  }

  public void setHologramUrl(@Nullable URI hologramUrl) {
    this.hologramUrl = hologramUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminItemRequestAssets adminItemRequestAssets = (AdminItemRequestAssets) o;
    return Objects.equals(this.iconUrl, adminItemRequestAssets.iconUrl) &&
        Objects.equals(this.previewUrl, adminItemRequestAssets.previewUrl) &&
        Objects.equals(this.hologramUrl, adminItemRequestAssets.hologramUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iconUrl, previewUrl, hologramUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminItemRequestAssets {\n");
    sb.append("    iconUrl: ").append(toIndentedString(iconUrl)).append("\n");
    sb.append("    previewUrl: ").append(toIndentedString(previewUrl)).append("\n");
    sb.append("    hologramUrl: ").append(toIndentedString(hologramUrl)).append("\n");
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

