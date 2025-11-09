package com.necpgame.backjava.model;

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
 * JoinVoiceRequestClientInfo
 */

@JsonTypeName("JoinVoiceRequest_clientInfo")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class JoinVoiceRequestClientInfo {

  /**
   * Gets or Sets platform
   */
  public enum PlatformEnum {
    PC("pc"),
    
    CONSOLE("console"),
    
    MOBILE("mobile");

    private final String value;

    PlatformEnum(String value) {
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
    public static PlatformEnum fromValue(String value) {
      for (PlatformEnum b : PlatformEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PlatformEnum platform;

  private @Nullable String build;

  public JoinVoiceRequestClientInfo platform(@Nullable PlatformEnum platform) {
    this.platform = platform;
    return this;
  }

  /**
   * Get platform
   * @return platform
   */
  
  @Schema(name = "platform", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("platform")
  public @Nullable PlatformEnum getPlatform() {
    return platform;
  }

  public void setPlatform(@Nullable PlatformEnum platform) {
    this.platform = platform;
  }

  public JoinVoiceRequestClientInfo build(@Nullable String build) {
    this.build = build;
    return this;
  }

  /**
   * Get build
   * @return build
   */
  
  @Schema(name = "build", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("build")
  public @Nullable String getBuild() {
    return build;
  }

  public void setBuild(@Nullable String build) {
    this.build = build;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JoinVoiceRequestClientInfo joinVoiceRequestClientInfo = (JoinVoiceRequestClientInfo) o;
    return Objects.equals(this.platform, joinVoiceRequestClientInfo.platform) &&
        Objects.equals(this.build, joinVoiceRequestClientInfo.build);
  }

  @Override
  public int hashCode() {
    return Objects.hash(platform, build);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JoinVoiceRequestClientInfo {\n");
    sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
    sb.append("    build: ").append(toIndentedString(build)).append("\n");
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

