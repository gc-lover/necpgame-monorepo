package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * EnterCyberspace200Response
 */

@JsonTypeName("enterCyberspace_200_response")

public class EnterCyberspace200Response {

  private @Nullable Boolean success;

  private @Nullable String cyberspaceLocation;

  private @Nullable String avatarId;

  /**
   * Gets or Sets accessLevel
   */
  public enum AccessLevelEnum {
    BASIC("basic"),
    
    MEDIUM("medium"),
    
    ADVANCED("advanced");

    private final String value;

    AccessLevelEnum(String value) {
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
    public static AccessLevelEnum fromValue(String value) {
      for (AccessLevelEnum b : AccessLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AccessLevelEnum accessLevel;

  public EnterCyberspace200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public EnterCyberspace200Response cyberspaceLocation(@Nullable String cyberspaceLocation) {
    this.cyberspaceLocation = cyberspaceLocation;
    return this;
  }

  /**
   * Get cyberspaceLocation
   * @return cyberspaceLocation
   */
  
  @Schema(name = "cyberspace_location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cyberspace_location")
  public @Nullable String getCyberspaceLocation() {
    return cyberspaceLocation;
  }

  public void setCyberspaceLocation(@Nullable String cyberspaceLocation) {
    this.cyberspaceLocation = cyberspaceLocation;
  }

  public EnterCyberspace200Response avatarId(@Nullable String avatarId) {
    this.avatarId = avatarId;
    return this;
  }

  /**
   * Get avatarId
   * @return avatarId
   */
  
  @Schema(name = "avatar_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avatar_id")
  public @Nullable String getAvatarId() {
    return avatarId;
  }

  public void setAvatarId(@Nullable String avatarId) {
    this.avatarId = avatarId;
  }

  public EnterCyberspace200Response accessLevel(@Nullable AccessLevelEnum accessLevel) {
    this.accessLevel = accessLevel;
    return this;
  }

  /**
   * Get accessLevel
   * @return accessLevel
   */
  
  @Schema(name = "access_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("access_level")
  public @Nullable AccessLevelEnum getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(@Nullable AccessLevelEnum accessLevel) {
    this.accessLevel = accessLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnterCyberspace200Response enterCyberspace200Response = (EnterCyberspace200Response) o;
    return Objects.equals(this.success, enterCyberspace200Response.success) &&
        Objects.equals(this.cyberspaceLocation, enterCyberspace200Response.cyberspaceLocation) &&
        Objects.equals(this.avatarId, enterCyberspace200Response.avatarId) &&
        Objects.equals(this.accessLevel, enterCyberspace200Response.accessLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, cyberspaceLocation, avatarId, accessLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnterCyberspace200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    cyberspaceLocation: ").append(toIndentedString(cyberspaceLocation)).append("\n");
    sb.append("    avatarId: ").append(toIndentedString(avatarId)).append("\n");
    sb.append("    accessLevel: ").append(toIndentedString(accessLevel)).append("\n");
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

