package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * VoiceChannelOwner
 */


public class VoiceChannelOwner {

  /**
   * Gets or Sets ownerType
   */
  public enum OwnerTypeEnum {
    PLAYER("player"),
    
    PARTY("party"),
    
    GUILD("guild"),
    
    LOBBY("lobby"),
    
    RAID("raid");

    private final String value;

    OwnerTypeEnum(String value) {
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
    public static OwnerTypeEnum fromValue(String value) {
      for (OwnerTypeEnum b : OwnerTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private OwnerTypeEnum ownerType;

  private String ownerId;

  public VoiceChannelOwner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceChannelOwner(OwnerTypeEnum ownerType, String ownerId) {
    this.ownerType = ownerType;
    this.ownerId = ownerId;
  }

  public VoiceChannelOwner ownerType(OwnerTypeEnum ownerType) {
    this.ownerType = ownerType;
    return this;
  }

  /**
   * Get ownerType
   * @return ownerType
   */
  @NotNull 
  @Schema(name = "ownerType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ownerType")
  public OwnerTypeEnum getOwnerType() {
    return ownerType;
  }

  public void setOwnerType(OwnerTypeEnum ownerType) {
    this.ownerType = ownerType;
  }

  public VoiceChannelOwner ownerId(String ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * Get ownerId
   * @return ownerId
   */
  @NotNull 
  @Schema(name = "ownerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ownerId")
  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceChannelOwner voiceChannelOwner = (VoiceChannelOwner) o;
    return Objects.equals(this.ownerType, voiceChannelOwner.ownerType) &&
        Objects.equals(this.ownerId, voiceChannelOwner.ownerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ownerType, ownerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceChannelOwner {\n");
    sb.append("    ownerType: ").append(toIndentedString(ownerType)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
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

