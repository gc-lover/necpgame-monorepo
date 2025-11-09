package com.necpgame.backjava.model;

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
 * VoiceChannelPermissions
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class VoiceChannelPermissions {

  private @Nullable Boolean allowInvite;

  private @Nullable Boolean allowRecording;

  private @Nullable Boolean allowSpectators;

  public VoiceChannelPermissions allowInvite(@Nullable Boolean allowInvite) {
    this.allowInvite = allowInvite;
    return this;
  }

  /**
   * Get allowInvite
   * @return allowInvite
   */
  
  @Schema(name = "allowInvite", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowInvite")
  public @Nullable Boolean getAllowInvite() {
    return allowInvite;
  }

  public void setAllowInvite(@Nullable Boolean allowInvite) {
    this.allowInvite = allowInvite;
  }

  public VoiceChannelPermissions allowRecording(@Nullable Boolean allowRecording) {
    this.allowRecording = allowRecording;
    return this;
  }

  /**
   * Get allowRecording
   * @return allowRecording
   */
  
  @Schema(name = "allowRecording", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowRecording")
  public @Nullable Boolean getAllowRecording() {
    return allowRecording;
  }

  public void setAllowRecording(@Nullable Boolean allowRecording) {
    this.allowRecording = allowRecording;
  }

  public VoiceChannelPermissions allowSpectators(@Nullable Boolean allowSpectators) {
    this.allowSpectators = allowSpectators;
    return this;
  }

  /**
   * Get allowSpectators
   * @return allowSpectators
   */
  
  @Schema(name = "allowSpectators", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowSpectators")
  public @Nullable Boolean getAllowSpectators() {
    return allowSpectators;
  }

  public void setAllowSpectators(@Nullable Boolean allowSpectators) {
    this.allowSpectators = allowSpectators;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceChannelPermissions voiceChannelPermissions = (VoiceChannelPermissions) o;
    return Objects.equals(this.allowInvite, voiceChannelPermissions.allowInvite) &&
        Objects.equals(this.allowRecording, voiceChannelPermissions.allowRecording) &&
        Objects.equals(this.allowSpectators, voiceChannelPermissions.allowSpectators);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowInvite, allowRecording, allowSpectators);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceChannelPermissions {\n");
    sb.append("    allowInvite: ").append(toIndentedString(allowInvite)).append("\n");
    sb.append("    allowRecording: ").append(toIndentedString(allowRecording)).append("\n");
    sb.append("    allowSpectators: ").append(toIndentedString(allowSpectators)).append("\n");
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

