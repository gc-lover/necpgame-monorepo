package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * MediaAsset
 */


public class MediaAsset {

  private String assetId;

  private URI url;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    IMAGE("image"),
    
    VIDEO("video"),
    
    AUDIO("audio"),
    
    FILE("file");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable String altText;

  public MediaAsset() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MediaAsset(String assetId, URI url) {
    this.assetId = assetId;
    this.url = url;
  }

  public MediaAsset assetId(String assetId) {
    this.assetId = assetId;
    return this;
  }

  /**
   * Get assetId
   * @return assetId
   */
  @NotNull 
  @Schema(name = "assetId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("assetId")
  public String getAssetId() {
    return assetId;
  }

  public void setAssetId(String assetId) {
    this.assetId = assetId;
  }

  public MediaAsset url(URI url) {
    this.url = url;
    return this;
  }

  /**
   * Get url
   * @return url
   */
  @NotNull @Valid 
  @Schema(name = "url", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("url")
  public URI getUrl() {
    return url;
  }

  public void setUrl(URI url) {
    this.url = url;
  }

  public MediaAsset type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public MediaAsset altText(@Nullable String altText) {
    this.altText = altText;
    return this;
  }

  /**
   * Get altText
   * @return altText
   */
  
  @Schema(name = "altText", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("altText")
  public @Nullable String getAltText() {
    return altText;
  }

  public void setAltText(@Nullable String altText) {
    this.altText = altText;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaAsset mediaAsset = (MediaAsset) o;
    return Objects.equals(this.assetId, mediaAsset.assetId) &&
        Objects.equals(this.url, mediaAsset.url) &&
        Objects.equals(this.type, mediaAsset.type) &&
        Objects.equals(this.altText, mediaAsset.altText);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assetId, url, type, altText);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaAsset {\n");
    sb.append("    assetId: ").append(toIndentedString(assetId)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    altText: ").append(toIndentedString(altText)).append("\n");
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

