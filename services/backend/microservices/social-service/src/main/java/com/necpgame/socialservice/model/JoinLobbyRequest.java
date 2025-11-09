package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.VoiceSettings;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * JoinLobbyRequest
 */


public class JoinLobbyRequest {

  private String playerId;

  private String preferredRole;

  private @Nullable Integer rating;

  private @Nullable VoiceSettings voiceSettingsOverrides;

  private @Nullable String subchannelPreference;

  private @Nullable Boolean partySync;

  public JoinLobbyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public JoinLobbyRequest(String playerId, String preferredRole) {
    this.playerId = playerId;
    this.preferredRole = preferredRole;
  }

  public JoinLobbyRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public JoinLobbyRequest preferredRole(String preferredRole) {
    this.preferredRole = preferredRole;
    return this;
  }

  /**
   * Get preferredRole
   * @return preferredRole
   */
  @NotNull 
  @Schema(name = "preferredRole", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("preferredRole")
  public String getPreferredRole() {
    return preferredRole;
  }

  public void setPreferredRole(String preferredRole) {
    this.preferredRole = preferredRole;
  }

  public JoinLobbyRequest rating(@Nullable Integer rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Get rating
   * @return rating
   */
  
  @Schema(name = "rating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rating")
  public @Nullable Integer getRating() {
    return rating;
  }

  public void setRating(@Nullable Integer rating) {
    this.rating = rating;
  }

  public JoinLobbyRequest voiceSettingsOverrides(@Nullable VoiceSettings voiceSettingsOverrides) {
    this.voiceSettingsOverrides = voiceSettingsOverrides;
    return this;
  }

  /**
   * Get voiceSettingsOverrides
   * @return voiceSettingsOverrides
   */
  @Valid 
  @Schema(name = "voiceSettingsOverrides", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voiceSettingsOverrides")
  public @Nullable VoiceSettings getVoiceSettingsOverrides() {
    return voiceSettingsOverrides;
  }

  public void setVoiceSettingsOverrides(@Nullable VoiceSettings voiceSettingsOverrides) {
    this.voiceSettingsOverrides = voiceSettingsOverrides;
  }

  public JoinLobbyRequest subchannelPreference(@Nullable String subchannelPreference) {
    this.subchannelPreference = subchannelPreference;
    return this;
  }

  /**
   * Get subchannelPreference
   * @return subchannelPreference
   */
  
  @Schema(name = "subchannelPreference", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subchannelPreference")
  public @Nullable String getSubchannelPreference() {
    return subchannelPreference;
  }

  public void setSubchannelPreference(@Nullable String subchannelPreference) {
    this.subchannelPreference = subchannelPreference;
  }

  public JoinLobbyRequest partySync(@Nullable Boolean partySync) {
    this.partySync = partySync;
    return this;
  }

  /**
   * Get partySync
   * @return partySync
   */
  
  @Schema(name = "partySync", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partySync")
  public @Nullable Boolean getPartySync() {
    return partySync;
  }

  public void setPartySync(@Nullable Boolean partySync) {
    this.partySync = partySync;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JoinLobbyRequest joinLobbyRequest = (JoinLobbyRequest) o;
    return Objects.equals(this.playerId, joinLobbyRequest.playerId) &&
        Objects.equals(this.preferredRole, joinLobbyRequest.preferredRole) &&
        Objects.equals(this.rating, joinLobbyRequest.rating) &&
        Objects.equals(this.voiceSettingsOverrides, joinLobbyRequest.voiceSettingsOverrides) &&
        Objects.equals(this.subchannelPreference, joinLobbyRequest.subchannelPreference) &&
        Objects.equals(this.partySync, joinLobbyRequest.partySync);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, preferredRole, rating, voiceSettingsOverrides, subchannelPreference, partySync);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JoinLobbyRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    preferredRole: ").append(toIndentedString(preferredRole)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    voiceSettingsOverrides: ").append(toIndentedString(voiceSettingsOverrides)).append("\n");
    sb.append("    subchannelPreference: ").append(toIndentedString(subchannelPreference)).append("\n");
    sb.append("    partySync: ").append(toIndentedString(partySync)).append("\n");
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

