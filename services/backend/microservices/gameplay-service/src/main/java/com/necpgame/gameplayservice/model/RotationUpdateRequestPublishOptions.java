package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * RotationUpdateRequestPublishOptions
 */

@JsonTypeName("RotationUpdateRequest_publishOptions")

public class RotationUpdateRequestPublishOptions {

  private @Nullable Boolean notifyPlayers;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime publishAt;

  public RotationUpdateRequestPublishOptions notifyPlayers(@Nullable Boolean notifyPlayers) {
    this.notifyPlayers = notifyPlayers;
    return this;
  }

  /**
   * Get notifyPlayers
   * @return notifyPlayers
   */
  
  @Schema(name = "notifyPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyPlayers")
  public @Nullable Boolean getNotifyPlayers() {
    return notifyPlayers;
  }

  public void setNotifyPlayers(@Nullable Boolean notifyPlayers) {
    this.notifyPlayers = notifyPlayers;
  }

  public RotationUpdateRequestPublishOptions publishAt(@Nullable OffsetDateTime publishAt) {
    this.publishAt = publishAt;
    return this;
  }

  /**
   * Get publishAt
   * @return publishAt
   */
  @Valid 
  @Schema(name = "publishAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("publishAt")
  public @Nullable OffsetDateTime getPublishAt() {
    return publishAt;
  }

  public void setPublishAt(@Nullable OffsetDateTime publishAt) {
    this.publishAt = publishAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RotationUpdateRequestPublishOptions rotationUpdateRequestPublishOptions = (RotationUpdateRequestPublishOptions) o;
    return Objects.equals(this.notifyPlayers, rotationUpdateRequestPublishOptions.notifyPlayers) &&
        Objects.equals(this.publishAt, rotationUpdateRequestPublishOptions.publishAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(notifyPlayers, publishAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RotationUpdateRequestPublishOptions {\n");
    sb.append("    notifyPlayers: ").append(toIndentedString(notifyPlayers)).append("\n");
    sb.append("    publishAt: ").append(toIndentedString(publishAt)).append("\n");
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

