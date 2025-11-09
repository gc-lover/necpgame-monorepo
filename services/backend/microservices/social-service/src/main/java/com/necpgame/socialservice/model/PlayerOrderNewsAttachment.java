package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerOrderNewsAttachment
 */


public class PlayerOrderNewsAttachment {

  private @Nullable UUID attachmentId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    IMAGE("image"),
    
    VIDEO("video"),
    
    AUDIO("audio"),
    
    DOCUMENT("document"),
    
    MAP("map");

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

  private @Nullable URI url;

  private @Nullable String caption;

  public PlayerOrderNewsAttachment attachmentId(@Nullable UUID attachmentId) {
    this.attachmentId = attachmentId;
    return this;
  }

  /**
   * Get attachmentId
   * @return attachmentId
   */
  @Valid 
  @Schema(name = "attachmentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachmentId")
  public @Nullable UUID getAttachmentId() {
    return attachmentId;
  }

  public void setAttachmentId(@Nullable UUID attachmentId) {
    this.attachmentId = attachmentId;
  }

  public PlayerOrderNewsAttachment type(@Nullable TypeEnum type) {
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

  public PlayerOrderNewsAttachment url(@Nullable URI url) {
    this.url = url;
    return this;
  }

  /**
   * Get url
   * @return url
   */
  @Valid 
  @Schema(name = "url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("url")
  public @Nullable URI getUrl() {
    return url;
  }

  public void setUrl(@Nullable URI url) {
    this.url = url;
  }

  public PlayerOrderNewsAttachment caption(@Nullable String caption) {
    this.caption = caption;
    return this;
  }

  /**
   * Get caption
   * @return caption
   */
  
  @Schema(name = "caption", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("caption")
  public @Nullable String getCaption() {
    return caption;
  }

  public void setCaption(@Nullable String caption) {
    this.caption = caption;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderNewsAttachment playerOrderNewsAttachment = (PlayerOrderNewsAttachment) o;
    return Objects.equals(this.attachmentId, playerOrderNewsAttachment.attachmentId) &&
        Objects.equals(this.type, playerOrderNewsAttachment.type) &&
        Objects.equals(this.url, playerOrderNewsAttachment.url) &&
        Objects.equals(this.caption, playerOrderNewsAttachment.caption);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attachmentId, type, url, caption);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderNewsAttachment {\n");
    sb.append("    attachmentId: ").append(toIndentedString(attachmentId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    caption: ").append(toIndentedString(caption)).append("\n");
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

