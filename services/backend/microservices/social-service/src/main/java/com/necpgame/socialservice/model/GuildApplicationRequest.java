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
 * GuildApplicationRequest
 */


public class GuildApplicationRequest {

  private String guildId;

  private @Nullable String message;

  public GuildApplicationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GuildApplicationRequest(String guildId) {
    this.guildId = guildId;
  }

  public GuildApplicationRequest guildId(String guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  @NotNull 
  @Schema(name = "guildId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("guildId")
  public String getGuildId() {
    return guildId;
  }

  public void setGuildId(String guildId) {
    this.guildId = guildId;
  }

  public GuildApplicationRequest message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @Size(max = 256) 
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildApplicationRequest guildApplicationRequest = (GuildApplicationRequest) o;
    return Objects.equals(this.guildId, guildApplicationRequest.guildId) &&
        Objects.equals(this.message, guildApplicationRequest.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildId, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildApplicationRequest {\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

