package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MatchmakingRequest
 */


public class MatchmakingRequest {

  private String playerId;

  @Valid
  private List<String> roles = new ArrayList<>();

  private @Nullable Integer rating;

  private @Nullable String activityCode;

  private @Nullable String availabilityWindow;

  private @Nullable String language;

  private @Nullable String partyId;

  private @Nullable Boolean allowNewLobby;

  public MatchmakingRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchmakingRequest(String playerId, List<String> roles) {
    this.playerId = playerId;
    this.roles = roles;
  }

  public MatchmakingRequest playerId(String playerId) {
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

  public MatchmakingRequest roles(List<String> roles) {
    this.roles = roles;
    return this;
  }

  public MatchmakingRequest addRolesItem(String rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * Get roles
   * @return roles
   */
  @NotNull @Size(min = 1) 
  @Schema(name = "roles", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roles")
  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public MatchmakingRequest rating(@Nullable Integer rating) {
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

  public MatchmakingRequest activityCode(@Nullable String activityCode) {
    this.activityCode = activityCode;
    return this;
  }

  /**
   * Get activityCode
   * @return activityCode
   */
  
  @Schema(name = "activityCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activityCode")
  public @Nullable String getActivityCode() {
    return activityCode;
  }

  public void setActivityCode(@Nullable String activityCode) {
    this.activityCode = activityCode;
  }

  public MatchmakingRequest availabilityWindow(@Nullable String availabilityWindow) {
    this.availabilityWindow = availabilityWindow;
    return this;
  }

  /**
   * Get availabilityWindow
   * @return availabilityWindow
   */
  
  @Schema(name = "availabilityWindow", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availabilityWindow")
  public @Nullable String getAvailabilityWindow() {
    return availabilityWindow;
  }

  public void setAvailabilityWindow(@Nullable String availabilityWindow) {
    this.availabilityWindow = availabilityWindow;
  }

  public MatchmakingRequest language(@Nullable String language) {
    this.language = language;
    return this;
  }

  /**
   * Get language
   * @return language
   */
  
  @Schema(name = "language", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("language")
  public @Nullable String getLanguage() {
    return language;
  }

  public void setLanguage(@Nullable String language) {
    this.language = language;
  }

  public MatchmakingRequest partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
  }

  public MatchmakingRequest allowNewLobby(@Nullable Boolean allowNewLobby) {
    this.allowNewLobby = allowNewLobby;
    return this;
  }

  /**
   * Get allowNewLobby
   * @return allowNewLobby
   */
  
  @Schema(name = "allowNewLobby", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowNewLobby")
  public @Nullable Boolean getAllowNewLobby() {
    return allowNewLobby;
  }

  public void setAllowNewLobby(@Nullable Boolean allowNewLobby) {
    this.allowNewLobby = allowNewLobby;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchmakingRequest matchmakingRequest = (MatchmakingRequest) o;
    return Objects.equals(this.playerId, matchmakingRequest.playerId) &&
        Objects.equals(this.roles, matchmakingRequest.roles) &&
        Objects.equals(this.rating, matchmakingRequest.rating) &&
        Objects.equals(this.activityCode, matchmakingRequest.activityCode) &&
        Objects.equals(this.availabilityWindow, matchmakingRequest.availabilityWindow) &&
        Objects.equals(this.language, matchmakingRequest.language) &&
        Objects.equals(this.partyId, matchmakingRequest.partyId) &&
        Objects.equals(this.allowNewLobby, matchmakingRequest.allowNewLobby);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, roles, rating, activityCode, availabilityWindow, language, partyId, allowNewLobby);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchmakingRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    activityCode: ").append(toIndentedString(activityCode)).append("\n");
    sb.append("    availabilityWindow: ").append(toIndentedString(availabilityWindow)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    allowNewLobby: ").append(toIndentedString(allowNewLobby)).append("\n");
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

