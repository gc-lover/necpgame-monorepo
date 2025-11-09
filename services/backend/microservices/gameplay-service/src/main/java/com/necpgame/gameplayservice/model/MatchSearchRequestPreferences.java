package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * MatchSearchRequestPreferences
 */

@JsonTypeName("MatchSearchRequest_preferences")

public class MatchSearchRequestPreferences {

  private Boolean voiceLobby = true;

  @Valid
  private List<UUID> avoidPlayers = new ArrayList<>();

  private @Nullable String preferredLatencyRegion;

  public MatchSearchRequestPreferences voiceLobby(Boolean voiceLobby) {
    this.voiceLobby = voiceLobby;
    return this;
  }

  /**
   * Get voiceLobby
   * @return voiceLobby
   */
  
  @Schema(name = "voiceLobby", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voiceLobby")
  public Boolean getVoiceLobby() {
    return voiceLobby;
  }

  public void setVoiceLobby(Boolean voiceLobby) {
    this.voiceLobby = voiceLobby;
  }

  public MatchSearchRequestPreferences avoidPlayers(List<UUID> avoidPlayers) {
    this.avoidPlayers = avoidPlayers;
    return this;
  }

  public MatchSearchRequestPreferences addAvoidPlayersItem(UUID avoidPlayersItem) {
    if (this.avoidPlayers == null) {
      this.avoidPlayers = new ArrayList<>();
    }
    this.avoidPlayers.add(avoidPlayersItem);
    return this;
  }

  /**
   * Get avoidPlayers
   * @return avoidPlayers
   */
  @Valid 
  @Schema(name = "avoidPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avoidPlayers")
  public List<UUID> getAvoidPlayers() {
    return avoidPlayers;
  }

  public void setAvoidPlayers(List<UUID> avoidPlayers) {
    this.avoidPlayers = avoidPlayers;
  }

  public MatchSearchRequestPreferences preferredLatencyRegion(@Nullable String preferredLatencyRegion) {
    this.preferredLatencyRegion = preferredLatencyRegion;
    return this;
  }

  /**
   * Get preferredLatencyRegion
   * @return preferredLatencyRegion
   */
  
  @Schema(name = "preferredLatencyRegion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preferredLatencyRegion")
  public @Nullable String getPreferredLatencyRegion() {
    return preferredLatencyRegion;
  }

  public void setPreferredLatencyRegion(@Nullable String preferredLatencyRegion) {
    this.preferredLatencyRegion = preferredLatencyRegion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchSearchRequestPreferences matchSearchRequestPreferences = (MatchSearchRequestPreferences) o;
    return Objects.equals(this.voiceLobby, matchSearchRequestPreferences.voiceLobby) &&
        Objects.equals(this.avoidPlayers, matchSearchRequestPreferences.avoidPlayers) &&
        Objects.equals(this.preferredLatencyRegion, matchSearchRequestPreferences.preferredLatencyRegion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(voiceLobby, avoidPlayers, preferredLatencyRegion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchSearchRequestPreferences {\n");
    sb.append("    voiceLobby: ").append(toIndentedString(voiceLobby)).append("\n");
    sb.append("    avoidPlayers: ").append(toIndentedString(avoidPlayers)).append("\n");
    sb.append("    preferredLatencyRegion: ").append(toIndentedString(preferredLatencyRegion)).append("\n");
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

